package fr.eris.lunaz.manager.gang.commands.invite;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GangUnInvite extends SubCommand {
    public GangUnInvite() {
        super("uninvite", "lunaz.gang.uninvite", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Player target = null;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to uninvite a player ! &8(/gang create <GangName>)"));
            return;
        } if(!gangPlayer.havePermission(GangPermission.UN_INVITE_PLAYER, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        } if(args == null || args[0] == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument !"));
            return;
        } if(args[0].equalsIgnoreCase("all")) {
            playerGang.getSendedPlayerInviteRequest().clear();
            player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully delete all current pending invitation !"));
            return;
        } if((target = Bukkit.getPlayer(args[0])) == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + args[0] + " &7is not online ! &8(Try '/uninvite all')"));
            return;
        } if(!playerGang.getSendedPlayerInviteRequest().contains(target.getUniqueId())) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + target.getDisplayName() + " &7is already not invited !"));
            return;
        }

        playerGang.getSendedPlayerInviteRequest().remove(target.getUniqueId());
        player.sendMessage(ColorUtils.translate("&[✓] &7You successfully uninvited " + target.getDisplayName() + " &7!"));
    }
}
