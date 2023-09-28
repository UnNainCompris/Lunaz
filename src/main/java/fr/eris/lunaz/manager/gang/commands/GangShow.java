package fr.eris.lunaz.manager.gang.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.MessageBuilder;
import fr.eris.lunaz.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GangShow extends SubCommand {
    public GangShow() {
        super("show", "lunaz.gang.show", true);
        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Gang targetGang = LunaZ.getGangManager().getPlayerGang(player);
        if(args != null && args[0] != null) {
            Gang argsGang = LunaZ.getGangManager().getGangByName(args[0]);
            if(argsGang == null) {
                Player argsAsPlayer = Bukkit.getPlayer(args[0]);
                if(argsAsPlayer != null) targetGang = LunaZ.getGangManager().getPlayerGang(argsAsPlayer);
            }
            else targetGang = argsGang;
        }
        if(targetGang == null) return;

        MessageBuilder message = MessageBuilder.builder();
        message.addText("&8----------<" + targetGang.getGangDisplayName() + "&8>----------");
        message.addText("\n &4* &7Owner: &6" + LunaZ.getPlayerDataManager().getPlayerdata(targetGang.getLeader()).getGangPlayer().getGangPlayerDisplayName());
        addDescription(message, targetGang);
        addAlly(message, targetGang);
        addEnemy(message, targetGang);
        addOnlineMember(message, targetGang);
        addOfflineMember(message, targetGang);
        message.addText("&8-----------" + StringUtils.repeat("-", targetGang.getGangDisplayName().length())
                + "&8-----------");
    }

    private void addDescription(MessageBuilder message, Gang targetGang) {
        message.addText("\n &4* &7Description: &6");
        if(targetGang.getGangDescription() == null || targetGang.getGangDescription().isEmpty()) {
            message.addText("    &7 - This gang don't have any description ;(");
            return;
        }

        for(String string : targetGang.getGangDescription().split("\n")) {
            if(string.contains("<link=") && string.contains("</link>")) {
                int startIndex = string.indexOf("<link=") + "<link=".length();
                String stringFromStartIndex = string.substring(startIndex);
                int endIndex = stringFromStartIndex.indexOf("</link>") + "</link>".length();

                String before = string.substring(0, startIndex - "<link=".length());
                String link = stringFromStartIndex.substring(0, stringFromStartIndex.indexOf(">"));
                String inside = stringFromStartIndex.substring(stringFromStartIndex.indexOf(">") + 1, stringFromStartIndex.indexOf("<"));
                String after = stringFromStartIndex.substring(endIndex);

                message.addText("\n    " + before).addClickEvent(inside, ClickEvent.Action.OPEN_URL, link).addText(after);
            } else {
                message.addText("\n    " + string);
            }
        }
    }

    private void addAlly(MessageBuilder message, Gang targetGang) {
        if(!targetGang.haveAlly()) return;
        message.addText("\n&4* &7Ally: &6");
        for(UUID currentAllyUUID : targetGang.getAlly()) {
            Gang currentAllyGang = LunaZ.getGangManager().getGang(currentAllyUUID);
            message.addClickAndHoverEvent(currentAllyGang.getGangDisplayName(), ClickEvent.Action.RUN_COMMAND, "gang show " +
                    currentAllyGang.getGangName(), HoverEvent.Action.SHOW_TEXT,
                    Collections.singletonList(new Text(ColorUtils.translate("&7Click to see the show the faction"))));

            if(targetGang.getAlly().indexOf(currentAllyUUID) != targetGang.getAlly().size() - 1)
                message.addText("&7, &6");
            else message.addText("&7.");
        }
    }

    private void addEnemy(MessageBuilder message, Gang targetGang) {
        if(!targetGang.haveEnemy()) return;
        message.addText("\n&4* &7Enemy: &6");
        for(UUID currentEnemyUUID : targetGang.getEnemy()) {
            Gang currentEnemyGang = LunaZ.getGangManager().getGang(currentEnemyUUID);
            message.addClickAndHoverEvent(currentEnemyGang.getGangDisplayName(), ClickEvent.Action.RUN_COMMAND, "gang show " +
                    currentEnemyGang.getGangName(), HoverEvent.Action.SHOW_TEXT,
                    Collections.singletonList(new Text(ColorUtils.translate("&7Click to see the show the faction"))));

            if(targetGang.getAlly().indexOf(currentEnemyUUID) != targetGang.getEnemy().size() - 1)
                message.addText("&7, &6");
            else message.addText("&7.");
        }
    }

    private void addOnlineMember(MessageBuilder message, Gang targetGang) {
        if(!targetGang.haveAlly()) return;
        message.addText("\n&4* &7Online: &6");

        int onlineMemberCount = targetGang.getOnlineMemberCount();
        List<GangPlayer> onlineMember = targetGang.getOnlineMembers();
        for(GangPlayer currentOnlinePlayer : onlineMember) {
            message.addClickAndHoverEvent(currentOnlinePlayer.getGangPlayerDisplayName(),
                    ClickEvent.Action.RUN_COMMAND, "gang manage " + currentOnlinePlayer.getPlayerID(),
                    HoverEvent.Action.SHOW_TEXT, Collections.singletonList(new Text("Click here to manage the player !")));
            if(onlineMember.indexOf(currentOnlinePlayer) != onlineMemberCount - 1)
                message.addText("&7, &6");
            else message.addText("&7.");
        }
    }

    private void addOfflineMember(MessageBuilder message, Gang targetGang) {
        if(!targetGang.haveAlly()) return;
        message.addText("\n&4* &7Offline: &6");

        int offlineMemberCount = targetGang.getOfflineMembersCount();
        List<GangPlayer> offlineMember = targetGang.getOfflineMembers();
        for(GangPlayer currentOfflinePlayer : offlineMember) {
            message.addClickAndHoverEvent(currentOfflinePlayer.getGangPlayerDisplayName(),
                    ClickEvent.Action.RUN_COMMAND, "gang manage " + currentOfflinePlayer.getPlayerID(),
                    HoverEvent.Action.SHOW_TEXT, Collections.singletonList(new Text("Click here to manage the player !")));
            if(offlineMember.indexOf(currentOfflinePlayer) != offlineMemberCount - 1)
                message.addText("&7, &6");
            else message.addText("&7.");
        }
    }
}
