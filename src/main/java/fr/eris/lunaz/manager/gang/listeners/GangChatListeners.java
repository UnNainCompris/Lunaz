package fr.eris.lunaz.manager.gang.listeners;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.enums.GangChatType;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GangChatListeners implements Listener {

    public GangChatListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getGang(gangPlayer.getGangID());

        if(gangPlayer.getCurrentChat() == GangChatType.GLOBAL) return;
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to be in a gang to talk in this chat ! &8(Use '/gang chat' to change chat type)"));
            return;
        }

        event.setCancelled(true);
        if(gangPlayer.getCurrentChat() == GangChatType.ALLY)
            playerGang.sendAllyChatMessage(player, event.getMessage());
        else if(gangPlayer.getCurrentChat() == GangChatType.GANG)
            playerGang.sendGangChatMessage(player, event.getMessage());
    }
}
