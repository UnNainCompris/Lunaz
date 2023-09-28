package fr.eris.lunaz.manager.magazine.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.utils.inventory.ExtendInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MagazineGiveSubCommand extends SubCommand {
    public MagazineGiveSubCommand() {
        super("give", "lunaz.magazine.admin.give", true);
        extendInventory = new ExtendInventory();
        extendInventory.setInventoryName("&7Magazine [%current_pages%/%max_page%]");
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
        List<Magazine> magazineList = LunaZ.getMagazineManager().getAllMagazine(false);
        for(Magazine magazine : magazineList) {
            extendInventory.addItem(new ClickableItem(magazine::getAsItem, (event) -> event.getWhoClicked().getInventory().addItem(event.getCurrentItem())));
        }
    }
}
