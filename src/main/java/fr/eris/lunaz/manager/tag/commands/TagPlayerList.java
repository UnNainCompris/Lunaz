package fr.eris.lunaz.manager.tag.commands;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import fr.eris.lunaz.manager.tag.data.Tag;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.NBTUtils;
import fr.eris.lunaz.utils.inventory.ExtendInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import fr.eris.lunaz.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class TagPlayerList extends SubCommand {
    public TagPlayerList() {
        super("list", "lunaz.tag.list", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = null;
        Player player = (Player) sender;
        if(args != null && args[0] != null && (target = Bukkit.getPlayer(args[0])) == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + args[0] + " &7is not online ! &8(Use '/gang show' and click on the target name)"));
            return;
        } else if(!player.hasPermission("lunaz.tag.list.admin")) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        }
        openInventory((target == null ? player : target), player);
    }

    public void openInventory(Player target, Player sender) {
        boolean isSelf = target.getName().equals(sender.getName());
        PlayerData playerData = PlayerData.getPlayerData(target.getUniqueId());
        ExtendInventory extendInventory = new ExtendInventory();
        extendInventory.setDynamicSize(true).setInventoryName("&6[&7Tag&6]" + (isSelf ? "" : target.getDisplayName()));

        for(String tagSystemName : playerData.getTag().getOwnedTag()) {
            Tag tag = LunaZ.getTagManager().getTag(tagSystemName);
            boolean isEquip = tag.getSystemName().equals(playerData.getTag().getCurrentTag());
            extendInventory.addItem(new ClickableItem(() -> {
                ItemBuilder itemBuilder = ItemBuilder.placeHolders(tag.getDisplayMaterial(), isEquip);
                itemBuilder.setDisplayName(tag.getDisplayName());
                if(tag.isDisplaySystemName()) itemBuilder.addLore("&8" + tag.getSystemName());
                itemBuilder.addLore("&a[✓] &7Set current tag to " + target.getDisplayName() + " ! &8[Left click]");
                if(sender.hasPermission("lunaz.tag.list.admin"))
                    itemBuilder.addLore("&c[✗] &7Revoke tag to " + target.getDisplayName() + " ! &8[Right click]");
                return new NBTUtils(itemBuilder.build()).set("TagSystemName", tag.getSystemName()).build();
            }, (event) -> {
                NBTItem nbtItem = NBTUtils.toNBT(event.getCurrentItem());
                String clickedTag = nbtItem.getString("TagSystemName");
                if(sender.hasPermission("lunaz.tag.list.admin") && event.getClick() == ClickType.RIGHT) {
                    playerData.getTag().getOwnedTag().remove(clickedTag);
                    if(playerData.getTag().getCurrentTag().equals(clickedTag))
                        playerData.getTag().setCurrentTag(null);
                } else if(event.getClick() == ClickType.LEFT) {
                    playerData.getTag().setCurrentTag(clickedTag);
                }
            }));
        }
    }
}
