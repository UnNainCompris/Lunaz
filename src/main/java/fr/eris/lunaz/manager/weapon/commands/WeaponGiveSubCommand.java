package fr.eris.lunaz.manager.weapon.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.weapon.WeaponManager;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.inventory.ExtendInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WeaponGiveSubCommand extends SubCommand {
    public WeaponGiveSubCommand() {
        super("give", "lunaz.weapon.admin.give", true);
        extendInventory = new ExtendInventory();
        extendInventory.setInventoryName("&7Weapon [%current_pages%/%max_page%]");
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
        List<Weapon> weaponList = LunaZ.getWeaponManager().getAllWeapon(false);
        for(Weapon weapon : weaponList) {
            extendInventory.addItem(new ClickableItem(weapon::getAsItem, (event) -> event.getWhoClicked().getInventory().addItem(event.getCurrentItem())));
        }
    }
}
