package fr.eris.lunaz.manager.gang.commands.kick;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GangKick extends SubCommand {
    public GangKick() {
        super("kick", "lunaz.gang.kick", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Player target = null;
        GangPlayer gangTarget = null;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        boolean silent = false;
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to kick a player ! &8(/gang create <GangName>)"));
            return;
        } if(!gangPlayer.havePermission(GangPermission.KICK, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        } if(args == null || args[0] == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument !"));
            return;
        } if(args[1] != null) {
            if(args[1].equalsIgnoreCase("-s")) {
                if(gangPlayer.havePermission(GangPermission.KICK_SILENT, true)) {
                    silent = true;
                } else {
                    player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to kick silently !"));
                    return;
                }
            }
        } if((target = Bukkit.getPlayer(args[0])) == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + args[0] + " &7is not online ! &8(Use '/gang show' and click on the target name)"));
            return;
        } if(!playerGang.getAllMemberAsUuid().contains(target.getUniqueId())) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + target.getDisplayName() + " &7is not in the gang !"));
            return;
        }
        gangTarget = LunaZ.getPlayerDataManager().getPlayerdata(target.getUniqueId()).getGangPlayer();
        if(gangTarget.getGangRank().getRankForce() > gangPlayer.getGangRank().getRankForce()) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        }
        playerGang.kickPlayer(target);
        if(silent) target.sendMessage(ColorUtils.translate("&9[;(] &7You was kicked from your gang."));
        else target.sendMessage(ColorUtils.translate("&9[;(] &7You was kicked from your gang by " + target.getDisplayName() + "&7."));
        player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully kicked " + target.getDisplayName() + " &7!"));
    }
}
