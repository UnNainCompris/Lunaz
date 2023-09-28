package fr.eris.lunaz.manager.gang.commands.invite;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.MessageBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GangInvite extends SubCommand {
    public GangInvite() {
        super("invite", "lunaz.gang.invite", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Player target = null;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to invite a player ! &8(/gang create <GangName>)"));
            return;
        } if(!gangPlayer.havePermission(GangPermission.INVITE_PLAYER, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        } if(args == null || args[0] == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument !"));
            return;
        } if((target = Bukkit.getPlayer(args[0])) == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + args[0] + " &7is not online !"));
            return;
        } if(playerGang.getSendedPlayerInviteRequest().contains(target.getUniqueId())) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + target.getDisplayName() + " &7is already invited !"));
            return;
        }

        playerGang.getSendedPlayerInviteRequest().add(target.getUniqueId());
        MessageBuilder message = MessageBuilder.builder();
        message.addClickAndHoverEvent("&a[✓] &7You was invited in the gang '" + playerGang.getGangDisplayName() + "&7' (" + playerGang.getGangName() + ") !",
                ClickEvent.Action.RUN_COMMAND, "gang join " + playerGang.getGangName(),
                HoverEvent.Action.SHOW_TEXT, Collections.singletonList(new Text(ColorUtils.translate("Click here to join the gang " + playerGang.getGangName()))));
        player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully invited " + target.getDisplayName() + " &7!"));
    }
}
