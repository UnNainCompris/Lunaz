package fr.eris.lunaz.manager.gang.listeners;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.enums.GangRank;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GangConnexionListeners implements Listener {
    public GangConnexionListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {
        PlayerData playerData = PlayerData.getPlayerData(event.getPlayer().getUniqueId());
        Gang playerGang = LunaZ.getGangManager().getGang(playerData.getGangPlayer().getGangID());
        if(playerGang == null) return;
        playerGang.sendGangChatMessage("&7[&8" + playerGang.getOnlineMemberCount() + "&7/&8" + playerGang.getMemberCount() + "&7] " +
                playerData.getGangPlayer().getGangPlayerDisplayName() + " &7join !", false);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        PlayerData playerData = PlayerData.getPlayerData(event.getPlayer().getUniqueId());
        Gang playerGang = LunaZ.getGangManager().getGang(playerData.getGangPlayer().getGangID());
        if(playerData.getGangPlayer().getGangRank() == GangRank.ADMIN_STATUS) {
            if(playerGang != null) {
                playerGang.kickPlayer(event.getPlayer());
            } else {
                playerData.getGangPlayer().setGangRank(GangRank.RECRUIT);
            }
            return;
        }

        if(playerGang == null) return;
        playerGang.sendGangChatMessage("&7[&8" + playerGang.getOnlineMemberCount() + "&7/&8" + playerGang.getMemberCount() + "&7]" +
                playerData.getGangPlayer().getGangPlayerDisplayName() + " &7leave !", false);
    }
}
