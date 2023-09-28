package fr.eris.lunaz.manager.scoreboard.listeners;

import fr.eris.lunaz.LunaZ;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DynamicListeners implements Listener {

    public DynamicListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        LunaZ.getScoreboardManager().registerPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        LunaZ.getScoreboardManager().unregisterPlayer(event.getPlayer());
    }

}
