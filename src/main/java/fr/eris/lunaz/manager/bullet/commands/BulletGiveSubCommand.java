package fr.eris.lunaz.manager.bullet.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.inventory.ExtendInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BulletGiveSubCommand extends SubCommand {
    public BulletGiveSubCommand() {
        super("give", "lunaz.bullet.admin.give", true);
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
        List<Bullet> bulletList = LunaZ.getBulletManager().getAllBullet(false);
        for(Bullet bullet : bulletList) {
            extendInventory.addItem(new ClickableItem(bullet::getAsItem, (event) -> event.getWhoClicked().getInventory().addItem(event.getCurrentItem())));
        }
    }
}
