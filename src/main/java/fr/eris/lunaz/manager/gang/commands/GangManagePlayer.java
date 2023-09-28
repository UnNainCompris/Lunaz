package fr.eris.lunaz.manager.gang.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.gang.enums.GangRank;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.InventoryUtils;
import fr.eris.lunaz.utils.StringUtils;
import fr.eris.lunaz.utils.inventory.CustomInventory;
import fr.eris.lunaz.utils.inventory.ExtendInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import fr.eris.lunaz.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GangManagePlayer extends SubCommand {
    public GangManagePlayer() {
        super("manageplayer", "lunaz.gang.manageplayer", true);
        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);

        GangPlayer gangTarget = null;
        UUID target = null;
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to manage a player ! &8(/gang create <GangName>)"));
            return;
        } if(!gangPlayer.havePermission(GangPermission.MANAGE_PLAYER, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        } if(args == null || args[0] == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument !"));
            return;
        } if(!StringUtils.isUUID(args[0])) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + args[0] + " &7is not a valid UUID !"));
            return;
        }
        target = UUID.fromString(args[0]);
        gangTarget = LunaZ.getPlayerDataManager().getPlayerdata(target).getGangPlayer();
        if(!playerGang.getAllMemberAsUuid().contains(target)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + Bukkit.getOfflinePlayer(target).getName() + " &7is not in your gang !"));
            return;
        } if(gangTarget.getGangRank().getRankForce() > gangPlayer.getGangRank().getRankForce()) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        }
        openManageInventory(player, target);
    }

    private void openManageInventory(Player player, UUID target) {
        CustomInventory manageInventory = new CustomInventory();
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        GangPlayer gangTarget = LunaZ.getPlayerDataManager().getPlayerdata(target).getGangPlayer();

        manageInventory.setInventoryName("&6[Gang Management] Player -> " + gangTarget.getGangPlayerDisplayName())
                .setInventorySize(45);
        InventoryUtils.aroundInventory(manageInventory.getInventory(),
                ItemBuilder.placeHolders(Material.GRAY_STAINED_GLASS_PANE, false).build());
        // do all the management stuff here (kick, promote, permission)
        manageInventory.setItem(20, new ClickableItem(() ->
            new ItemBuilder().setMaterial(Material.RED_CONCRETE).setDisplayName("&aKick " + gangTarget.getGangPlayerDisplayName()).build(),
            (event) -> {
                Bukkit.dispatchCommand(event.getWhoClicked(), "gang uuidkick " + target);
                event.getWhoClicked().closeInventory();
            }
        ));

        manageInventory.setItem(22, new ClickableItem(() ->
                new ItemBuilder().setMaterial(Material.BOOK).setDisplayName("&7Manage Permission of " + gangTarget.getGangPlayerDisplayName()).build(),
                (event) -> {
                    if(gangTarget.getGangRank().getRankForce() > gangPlayer.getGangRank().getRankForce() && gangPlayer.havePermission(GangPermission.MANAGE_PLAYER_PERMISSION, true))
                        openManagePermissionInventory(player, target);
                    else player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
                }
        ));

        manageInventory.setItem(24, new ClickableItem(() ->
                new ItemBuilder().setMaterial(Material.TOTEM_OF_UNDYING).setDisplayName("&7Manage rank of " + gangTarget.getGangPlayerDisplayName()).build(),
                (event) -> {
                    if(gangTarget.getGangRank().getRankForce() > gangPlayer.getGangRank().getRankForce() && gangPlayer.havePermission(GangPermission.MANAGE_PLAYER_RANK, true))
                        openManageRankInventory(player, target);
                    else player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
                }
        ));

        manageInventory.update(player);
    }

    // TODO to see if in another command or not
    public void openManageRankInventory(Player player, UUID target) {
        CustomInventory manageRankInventory = new CustomInventory();
        GangPlayer gangTarget = LunaZ.getPlayerDataManager().getPlayerdata(target).getGangPlayer();
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();

        manageRankInventory.setInventoryName("&6[Gang Management] Player &7[Rank] &6-> " + gangTarget.getGangPlayerDisplayName())
                .setInventorySize(45);
        InventoryUtils.aroundInventory(manageRankInventory.getInventory(),
                ItemBuilder.placeHolders(Material.GRAY_STAINED_GLASS_PANE, false).build());

        manageRankInventory.setItem(22, new ClickableItem(() ->
            new ItemBuilder().setMaterial((gangPlayer.getGangRank().getRankForce() >= GangRank.LEADER.getRankForce()) ? Material.RED_CONCRETE : Material.BEDROCK).setDisplayName("&7Leader" + gangTarget.getGangPlayerDisplayName()).build(),
            (event) -> {
                if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.BEDROCK) return;
                gangTarget.setGangRank(GangRank.LEADER);
                gangPlayer.setGangRank(GangRank.CO_LEADER);
                if(Bukkit.getPlayer(target) != null)
                    Bukkit.getPlayer(target).sendMessage(ColorUtils.translate("&a[✓] &7You was promote to co-leader !"));
                player.sendMessage(ColorUtils.translate("&c[✗] &7You was unranked to co-leader !"));
                player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully change the rank of " + gangTarget.getGangPlayerDisplayName() + " to leader&7!"));
            }
        ));

        changeRankItem(target, player, GangRank.CO_LEADER, 23, manageRankInventory);
        changeRankItem(target, player, GangRank.MOD, 24, manageRankInventory);
        changeRankItem(target, player, GangRank.MEMBER, 25, manageRankInventory);
        changeRankItem(target, player, GangRank.RECRUIT, 26, manageRankInventory);
        manageRankInventory.update(player);
    }

    public void changeRankItem(UUID target, Player player, GangRank rank, int slot, CustomInventory inventory) {
        GangPlayer gangTarget = LunaZ.getPlayerDataManager().getPlayerdata(target).getGangPlayer();
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        inventory.setItem(22, new ClickableItem(() ->
            new ItemBuilder().setMaterial((gangPlayer.getGangRank().getRankForce() > rank.getRankForce()) ? Material.RED_CONCRETE : Material.BEDROCK).setDisplayName("&7Leader" + gangTarget.getGangPlayerDisplayName()).build(),
            (event) -> {
                if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.BEDROCK) return;
                gangTarget.setGangRank(rank);
                String rankAsDisplay = rank.toString().toLowerCase().replace("_", " ");
                if(Bukkit.getPlayer(target) != null)
                    Bukkit.getPlayer(target).sendMessage(ColorUtils.translate("&a[✓] &7You was promote to " + rankAsDisplay + " !"));
                player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully change the rank of " + gangTarget.getGangPlayerDisplayName() + " to " + rankAsDisplay + "&7!"));
            }
        ));
    }

    // TODO to see if in another command or not
    public void openManagePermissionInventory(Player player, UUID target) {
        ExtendInventory managePermissionInventory = new ExtendInventory();
        GangPlayer gangTarget = LunaZ.getPlayerDataManager().getPlayerdata(target).getGangPlayer();
        Gang gang = LunaZ.getGangManager().getGang(gangTarget.getGangID());
        managePermissionInventory.setInventorySize(36).setInventoryName("&6[Gang Management] Player &7[Permission] &6-> " + gangTarget.getGangPlayerDisplayName());

        for(GangPermission permission : GangPermission.values()) {
            boolean havePermission = gangTarget.havePermission(permission, false);
            managePermissionInventory.addItem(new ClickableItem(() ->
                ItemBuilder.placeHolders(permission.getItemDisplayMaterial(), havePermission)
                        .setDisplayName(permission.getItemDisplayName()
                                + (havePermission ? " &a[✓]" : " &c[✗]")).setLore(permission.getDescription()).build(),
                (event) -> {
                    if(havePermission) gang.getPermissionManager().removePermission(target, permission);
                    else gang.getPermissionManager().addPermission(target, permission);
                }
            ));
        }

        managePermissionInventory.update(player);
    }
}
