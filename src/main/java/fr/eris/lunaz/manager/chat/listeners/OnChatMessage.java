package fr.eris.lunaz.manager.chat.listeners;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.gang.enums.GangChatType;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.MessageBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
import java.util.Collections;

public class OnChatMessage implements Listener {

    public OnChatMessage() {
        LunaZ.getInstance().getServer().getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        PlayerData senderPlayerData = LunaZ.getPlayerDataManager().getPlayerdata(sender.getUniqueId());
        if(senderPlayerData.getGangPlayer().getCurrentChat() != GangChatType.GLOBAL) return;

        String defaultMessage = event.getMessage();
        String newMessage = event.getMessage();

        event.setCancelled(true);
        for(Player target : event.getRecipients()) {
            if(isPinging(target, defaultMessage)) {
                newMessage = newMessage.replaceAll(target.getName(), ColorUtils.translate("&b@" + target.getName() + "&r"));
                if(senderPlayerData.getSettings().isAllowPing())
                    target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100000.0f, 100000.0f);
            }
        }

        MessageBuilder message = MessageBuilder.builder();
        message.addClickAndHoverEvent(sender.getDisplayName(), ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " ",
                HoverEvent.Action.SHOW_TEXT, Collections.singletonList(new Text(getPlayerInformation(sender))));
        message.sendMessage(event.getRecipients());
    }

    public String getPlayerInformation(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        String hoverMessage = player.getDisplayName() + "&r\n";
        if(playerData.getStaff().isStaff()) {
            hoverMessage += "\n&8This player is a member of the staff team,  if you have any issue don't talk directly to him but use /staffrequest";
        }
        return hoverMessage +
                "\n&7>> &6Rank: &r" + "LuckPermRank" +
                "\n&7>> &cGang: &r" + LunaZ.getGangManager().getPlayerGang(player).getGangName() +
                "\n&7>> &aMoney: &r" + playerData.getMoney().getValue() +
                "\n&7>> &5Luna: &r" + playerData.getLuna().getValue() +
                "\n&7>> &cKills: &r" + playerData.getStatistics().getPlayerKills() +
                "\n&7>> &d&lTag: &r" + playerData.getTag().getCurrentTag();
    }

    private boolean isPinging(Player target, String checkingMessage) {
        return ChatColor.stripColor(checkingMessage).contains(target.getName());
    }
}
