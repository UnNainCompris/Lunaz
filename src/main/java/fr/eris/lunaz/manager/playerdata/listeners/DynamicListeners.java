package fr.eris.lunaz.manager.playerdata.listeners;

import fr.eris.lunaz.LunaZ;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DynamicListeners implements Listener {

    public DynamicListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        LunaZ.getPlayerDataManager().emptyCache(event.getPlayer().getUniqueId());
    }
}
