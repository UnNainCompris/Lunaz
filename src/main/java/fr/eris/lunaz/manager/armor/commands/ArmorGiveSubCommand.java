package fr.eris.lunaz.manager.armor.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.armor.data.Armor;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.inventory.ExtendInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ArmorGiveSubCommand extends SubCommand {
    public ArmorGiveSubCommand() {
        super("give", "lunaz.armor.admin.give", true);
        extendInventory = new ExtendInventory();
        extendInventory.setInventoryName("&aArmor [%current_pages%/%max_page%]");
        extendInventory.setInventorySize(27);
    }

    private final ExtendInventory extendInventory;

    @Override
    public void execute(CommandSender sender, String[] args) {
        extendInventory.removeAll();
        loadExtendInventory();
        extendInventory.update((Player)sender);
    }

    public void loadExtendInventory() {
        List<Armor> armorList = LunaZ.getArmorManager().getAllArmor(false);
        for(Armor armor : armorList) {
            extendInventory.addItem(new ClickableItem(armor::getAsDisplayItem,
            (event) -> {
                Armor armorToGive = LunaZ.getArmorManager().getArmor(event.getCurrentItem());
                if(!(event.getWhoClicked() instanceof Player) || armorToGive == null) return;
                Player player = (Player) event.getWhoClicked();
                player.getInventory().addItem(armor.getAsItem(player));
            }));
        }
    }
}
