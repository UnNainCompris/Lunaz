package fr.eris.lunaz.manager.gang.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.enums.GangChatType;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GangChat extends SubCommand {
    public GangChat() {
        super("chat", "lunaz.gang.chat", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) { // do chat part in listeners
        Player player = (Player) sender;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getGang(gangPlayer.getGangID());
        if(args == null || args[0] == null || playerGang == null) {
            if(playerGang != null) gangPlayer.setNextChatType();
            else gangPlayer.setCurrentChat(GangChatType.GLOBAL);

            player.sendMessage(ColorUtils.translate("&6[!] &7You are now talking in &6" + gangPlayer.getCurrentChat().toString().toLowerCase() + " &7chat !"));
            return;
        }
        String targetChat = args[0];
        GangChatType gangChatType = null;
        for(GangChatType currentGangChatType : GangChatType.values()) {
            if(currentGangChatType.toString().equalsIgnoreCase(targetChat))
                gangChatType = currentGangChatType;
        }

        if(gangChatType == null) {
            player.sendMessage(ColorUtils.translate("&6[!] &7The chat type '&6" + targetChat + "' &7don't exist &8(try: 'Global', 'Ally', 'Gang')!"));
            return;
        }

        if(args[1] == null) {
            gangPlayer.setCurrentChat(gangChatType);
            player.sendMessage(ColorUtils.translate("&6[!] &7You are now talking in &6" + gangPlayer.getCurrentChat().toString().toLowerCase() + " &7chat !"));
            return;
        }

        StringBuilder message = new StringBuilder();
        for(String messagePart : Arrays.copyOfRange(args, 1, args.length)) {
            message.append(messagePart);
        }

        if(gangPlayer.getCurrentChat() == GangChatType.ALLY)
            playerGang.sendAllyChatMessage(player, message.toString());
        else if(gangPlayer.getCurrentChat() == GangChatType.GANG)
            playerGang.sendGangChatMessage(player, message.toString());
    }
}
