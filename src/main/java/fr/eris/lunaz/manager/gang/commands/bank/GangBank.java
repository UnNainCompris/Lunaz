package fr.eris.lunaz.manager.gang.commands.bank;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import fr.eris.lunaz.manager.playerdata.data.currency.CurrencyType;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.InventoryUtils;
import fr.eris.lunaz.utils.inventory.CustomInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import fr.eris.lunaz.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class GangBank extends SubCommand {
    public GangBank() {
        super("bank", "lunaz.gang.bank", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to access the gang bank ! &8(/gang create <GangName>)"));
            return;
        } if(!gangPlayer.havePermission(GangPermission.BANK_ACCESS, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        }
        player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully open the bank &7!"));
    }
    public void openBank(Player target) {
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(target);
        CustomInventory bankInventory = new CustomInventory();
        bankInventory.setInventorySize(27).setInventoryName("&6&l[&eBank&6&l] &7- " + playerGang.getGangDisplayName());
        InventoryUtils.aroundInventory(bankInventory.getInventory(),
                ItemBuilder.placeHolders(Material.ORANGE_STAINED_GLASS_PANE, false).build());


        bankInventory.setItem(11, new ClickableItem(
                () -> ItemBuilder.placeHolders(Material.RED_STAINED_GLASS_PANE, false).setDisplayName("&cWithdraw").build(),
        (event) -> {
            HumanEntity clicker = event.getWhoClicked();

            GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(clicker.getUniqueId()).getGangPlayer();
            if((!gangPlayer.havePermission(GangPermission.WITHDRAW_BANK_MONEY, true) &&
                    !gangPlayer.havePermission(GangPermission.WITHDRAW_BANK_LUNA, true)) || !(clicker instanceof Player)) {
                clicker.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
                return;
            }
            openWithdrawInventory((Player) clicker);
        }));

        bankInventory.setItem(12, new ClickableItem(() ->
                ItemBuilder.placeHolders(Material.OAK_WALL_SIGN, false).setDisplayName("&6Bank")
                        .setLore("&eMoney&7: " + playerGang.getGangMoneyBank().getValue(),
                                 "&dLuna&7: " + playerGang.getGangLunaBank().getValue()).build()));

        bankInventory.setItem(13, new ClickableItem(
                () -> ItemBuilder.placeHolders(Material.GREEN_STAINED_GLASS_PANE, false).setDisplayName("&aDeposit").build(),
        (event) -> {
            HumanEntity clicker = event.getWhoClicked();
            GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(clicker.getUniqueId()).getGangPlayer();
            if((!gangPlayer.havePermission(GangPermission.DEPOSIT_BANK_MONEY, true) &&
                    !gangPlayer.havePermission(GangPermission.DEPOSIT_BANK_LUNA, true)) || !(clicker instanceof Player)) {
                clicker.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
                return;
            }
            openDepositInventory((Player) clicker);
        }));

        bankInventory.setItem(15, new ClickableItem(
                () -> ItemBuilder.placeHolders(Material.CHEST, false).setDisplayName("&eItem bank").build(),
                (event) -> {
                    HumanEntity clicker = event.getWhoClicked();
                    GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(clicker.getUniqueId()).getGangPlayer();
                    if((!gangPlayer.havePermission(GangPermission.EDIT_BANK_ITEM, true) &&
                            !gangPlayer.havePermission(GangPermission.VIEW_ITEM_BANK, true)) || !(clicker instanceof Player)) {
                        clicker.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
                        return;
                    }
                    openItemBank((Player) clicker);
                }));
    }

    public void openDepositInventory(Player target) {
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(target);
        CustomInventory bankInventory = new CustomInventory();
        PlayerData targetData = LunaZ.getPlayerDataManager().getPlayerdata(target.getUniqueId());
        GangPlayer gangTarget = targetData.getGangPlayer();

        boolean depositMoneyPermission = gangTarget.havePermission(GangPermission.DEPOSIT_BANK_MONEY, true);
        boolean depositLunaPermission = gangTarget.havePermission(GangPermission.DEPOSIT_BANK_LUNA, true);

        int inventorySize = 36 + (depositMoneyPermission ? 9 : 0) + (depositLunaPermission ? 9 : 0);

        bankInventory.setInventorySize(inventorySize).setInventoryName("&6&l[&eBank Deposit&6&l] &7- " + playerGang.getGangDisplayName());
        InventoryUtils.aroundInventory(bankInventory.getInventory(),
                ItemBuilder.placeHolders(Material.ORANGE_STAINED_GLASS_PANE, false).build());

        bankInventory.setItem(13, currencyInformationItem(target));

        int currentLine = 3;
        if(depositMoneyPermission) {
            int startSlot = (currentLine - 1) * 9 - 1;
            bankInventory.setItem(startSlot + 3, depositItem(-1, Material.CHEST, CurrencyType.MONEY, bankInventory));
            bankInventory.setItem(startSlot + 4, depositItem(100, Material.GOLD_INGOT, CurrencyType.MONEY, bankInventory));
            bankInventory.setItem(startSlot + 5, depositItem(1000, Material.GOLD_ORE, CurrencyType.MONEY, bankInventory));
            bankInventory.setItem(startSlot + 6, depositItem(10000, Material.GOLD_BLOCK, CurrencyType.MONEY, bankInventory));
            bankInventory.setItem(startSlot + 7, depositItem(-1, Material.CHEST, CurrencyType.MONEY, bankInventory));
            currentLine++;
        } if(depositLunaPermission) {
            int startSlot = (currentLine - 1) * 9 - 1;
            bankInventory.setItem(startSlot + 3, depositItem(-1, Material.ENDER_CHEST, CurrencyType.LUNA, bankInventory));
            bankInventory.setItem(startSlot + 4, depositItem(10, Material.DIAMOND, CurrencyType.LUNA, bankInventory));
            bankInventory.setItem(startSlot + 5, depositItem(100, Material.DIAMOND_ORE, CurrencyType.LUNA, bankInventory));
            bankInventory.setItem(startSlot + 6, depositItem(1000, Material.DIAMOND_BLOCK, CurrencyType.LUNA, bankInventory));
            bankInventory.setItem(startSlot + 7, depositItem(-1, Material.ENDER_CHEST, CurrencyType.LUNA, bankInventory));
        }
    }

    public ClickableItem currencyInformationItem(Player player) {
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        PlayerData targetData = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId());
        GangPlayer gangTarget = targetData.getGangPlayer();

        return new ClickableItem(() -> ItemBuilder.placeHolders(Material.OAK_WALL_SIGN, false)
                .setLore(gangTarget.getGangPlayerDisplayName() + "&7:",
                        "   " + CurrencyType.LUNA.getDisplayName() + " &7: &d" + targetData.getLuna().getValue(),
                        "   " + CurrencyType.MONEY.getDisplayName() + " &7: &e" + targetData.getMoney().getValue(),
                        "&7 ",
                        playerGang.getGangDisplayName() + "&7:",
                        "   " + CurrencyType.LUNA.getDisplayName() + " &7: &d" + playerGang.getGangLunaBank().getValue(),
                        "   " + CurrencyType.MONEY.getDisplayName() + " &7: &e" + playerGang.getGangMoneyBank().getValue()).build());
    }

    public ClickableItem depositItem(long amount, Material displayMaterial, CurrencyType currencyType, CustomInventory customInventory) {
        return new ClickableItem(() -> ItemBuilder.placeHolders(displayMaterial, false)
                .setDisplayName("&6[" + currencyType.getDisplayName() + "&6] &a" + (amount == -1 ? "All" : String.valueOf(amount)) + " &8[Deposit]").build(),
                (event) -> {
                    deposit((Player) event.getWhoClicked(), amount, currencyType);
                    customInventory.update();
        });
    }

    public void deposit(Player player, long amount, CurrencyType currencyType) {
        Gang gang = LunaZ.getGangManager().getPlayerGang(player);
        PlayerData playerData = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId());
        if(amount == -1) {
            if(currencyType == CurrencyType.LUNA) amount = playerData.getLuna().getValue();
            else if(currencyType == CurrencyType.MONEY) amount = playerData.getMoney().getValue();
        }
        if(amount <= 0) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to deposit at least 1 " + currencyType.getDisplayName() + " &7!"));
            return;
        }

        if(gang.deposit(player, amount, currencyType)) {
            player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully deposit " + amount + " " + currencyType.getDisplayName() + " &7!"));
        } else {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You don't have enough " + currencyType.getDisplayName() + " &7!"));
        }
    }

    public void openWithdrawInventory(Player target) {
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(target);
        CustomInventory bankInventory = new CustomInventory();
        PlayerData targetData = LunaZ.getPlayerDataManager().getPlayerdata(target.getUniqueId());
        GangPlayer gangTarget = targetData.getGangPlayer();

        boolean withdrawMoneyPermission = gangTarget.havePermission(GangPermission.WITHDRAW_BANK_MONEY, true);
        boolean withdrawLunaPermission = gangTarget.havePermission(GangPermission.WITHDRAW_BANK_LUNA, true);

        int inventorySize = 36 + (withdrawMoneyPermission ? 9 : 0) + (withdrawLunaPermission ? 9 : 0);

        bankInventory.setInventorySize(inventorySize).setInventoryName("&6&l[&eBank Withdraw&6&l] &7- " + playerGang.getGangDisplayName());
        InventoryUtils.aroundInventory(bankInventory.getInventory(),
                ItemBuilder.placeHolders(Material.ORANGE_STAINED_GLASS_PANE, false).build());

        bankInventory.setItem(13, currencyInformationItem(target));

        int currentLine = 3;
        if(withdrawMoneyPermission) {
            int startSlot = (currentLine - 1) * 9 - 1;
            bankInventory.setItem(startSlot + 3, withdrawItem(-1, Material.CHEST, CurrencyType.MONEY, bankInventory));
            bankInventory.setItem(startSlot + 4, withdrawItem(100, Material.GOLD_INGOT, CurrencyType.MONEY, bankInventory));
            bankInventory.setItem(startSlot + 5, withdrawItem(1000, Material.GOLD_ORE, CurrencyType.MONEY, bankInventory));
            bankInventory.setItem(startSlot + 6, withdrawItem(10000, Material.GOLD_BLOCK, CurrencyType.MONEY, bankInventory));
            bankInventory.setItem(startSlot + 7, withdrawItem(-1, Material.CHEST, CurrencyType.MONEY, bankInventory));
            currentLine++;
        } if(withdrawLunaPermission) {
            int startSlot = (currentLine - 1) * 9 - 1;
            bankInventory.setItem(startSlot + 3, withdrawItem(-1, Material.ENDER_CHEST, CurrencyType.LUNA, bankInventory));
            bankInventory.setItem(startSlot + 4, withdrawItem(10, Material.DIAMOND, CurrencyType.LUNA, bankInventory));
            bankInventory.setItem(startSlot + 5, withdrawItem(100, Material.DIAMOND_ORE, CurrencyType.LUNA, bankInventory));
            bankInventory.setItem(startSlot + 6, withdrawItem(1000, Material.DIAMOND_BLOCK, CurrencyType.LUNA, bankInventory));
            bankInventory.setItem(startSlot + 7, withdrawItem(-1, Material.ENDER_CHEST, CurrencyType.LUNA, bankInventory));
        }
    }

    public void openItemBank(Player target) {
        Gang targetGang = LunaZ.getGangManager().getPlayerGang(target);
        if (targetGang.getChestUpgrade().getCurrentLevel() == 0) {
            target.sendMessage(ColorUtils.translate("&c[✗] &7You doesn't unlock this feature yet !"));
        } else {
            targetGang.getChestUpgrade().openInventory(target);
        }
    }

    public ClickableItem withdrawItem(long amount, Material displayMaterial, CurrencyType currencyType, CustomInventory customInventory) {
        return new ClickableItem(() -> ItemBuilder.placeHolders(displayMaterial, false)
                .setDisplayName("&6[" + currencyType.getDisplayName() + "&6] &c" + (amount == -1 ? "All" : String.valueOf(amount)) + " &8[Withdraw]").build(),
                (event) -> {
                    deposit((Player) event.getWhoClicked(), amount, currencyType);
                    customInventory.update();
                });
    }

    public void withdraw(Player player, long amount, CurrencyType currencyType) {
        Gang gang = LunaZ.getGangManager().getPlayerGang(player);
        if(amount == -1) {
            if(currencyType == CurrencyType.LUNA) amount = gang.getGangLunaBank().getValue();
            else if(currencyType == CurrencyType.MONEY) amount = gang.getGangMoneyBank().getValue();
        }
        if(amount <= 0) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to withdraw at least 1 " + currencyType.getDisplayName() + " &7!"));
            return;
        }

        if(gang.withdraw(player, amount, currencyType)) {
            player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully withdraw " + amount + " " + currencyType.getDisplayName() + " &7!"));
        } else {
            player.sendMessage(ColorUtils.translate("&c[✗] &7There is not enough " + currencyType.getDisplayName() + "&7 in the bank!"));
        }
    }
}
