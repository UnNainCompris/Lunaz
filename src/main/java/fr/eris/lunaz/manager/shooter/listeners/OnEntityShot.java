package fr.eris.lunaz.manager.shooter.listeners;

import fr.eris.event.listeners.Listener;
import fr.eris.event.manager.EventHandler;
import fr.eris.event.manager.EventPriority;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.shoot.ShootEntityEvent;

public class OnEntityShot implements Listener {
    public OnEntityShot() {
        LunaZ.getEventManager().registerListener(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityShoot(ShootEntityEvent event) {
        LunaZ.getDamageManager().applyWeaponDamage(event.getTarget(), event.getShooter(), event.getWeapon().getSecond());
    }
}
