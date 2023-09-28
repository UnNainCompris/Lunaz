package fr.eris.lunaz.manager.shooter.listeners;

import fr.eris.event.listeners.Listener;
import fr.eris.event.manager.EventHandler;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.shoot.ShootBlockEvent;

public class OnBlockShot implements Listener {
    public OnBlockShot() {
        LunaZ.getEventManager().registerListener(this);
    }

    @EventHandler
    public void onBlockShoot(ShootBlockEvent event) {

    }
}
