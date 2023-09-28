package fr.eris.lunaz.manager.shooter;

import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.manager.shooter.listeners.OnBlockShot;
import fr.eris.lunaz.manager.shooter.listeners.OnEntityShot;
import fr.eris.lunaz.manager.shooter.task.ShootTask;
import fr.eris.manager.Manager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShooterManager extends Manager {

    public void start() {
        new OnBlockShot();
        new OnEntityShot();
    }

    public ShootTask shot(Player shooter, Weapon weapon, ItemStack weaponItem) {
        return new ShootTask(shooter, weapon, weaponItem);
    }

}
