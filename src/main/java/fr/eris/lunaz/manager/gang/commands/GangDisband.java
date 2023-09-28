package fr.eris.lunaz.manager.gang.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.MessageBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GangDisband extends SubCommand {
    public GangDisband() {
        super("disband", "lunaz.gang.disband", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to disband it ! &8(/gang create <GangName>)"));
            return;
        } if(!gangPlayer.havePermission(GangPermission.DISBAND, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        }
        if(args != null && args[0] != null && args[0].equals("confirm") && args[1] == null) {
            playerGang.disbandGang(false);
            player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully disband your gang !"));
            return;
        }

        MessageBuilder message = MessageBuilder.builder();
        message.addClickEvent("&6[?] &7Do you really want to disband your rank ? &8&o[Click me if Yes]",
                ClickEvent.Action.SUGGEST_COMMAND, "/gang disband confirm [RemoveMe:D]");
        message.sendMessage(player);
    }
}
