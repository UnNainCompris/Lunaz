package fr.eris.lunaz.manager.gang.commands.kick;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GangUUIDKick extends SubCommand {
    public GangUUIDKick() {
        super("uuidkick", "lunaz.gang.uuidkick", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        OfflinePlayer target = null;
        GangPlayer gangTarget = null;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to kick a player ! &8(/gang create <GangName>)"));
            return;
        } if(!gangPlayer.havePermission(GangPermission.FRIENDLY_FIRE, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        } if(args == null || args[0] == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument !"));
            return;
        }
        target = Bukkit.getOfflinePlayer(UUID.fromString(args[0]));
        if(!playerGang.getAllMemberAsUuid().contains(target.getUniqueId())) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7" + target.getName() + " &7is not in the gang !"));
            return;
        }

        gangTarget = LunaZ.getPlayerDataManager().getPlayerdata(target.getUniqueId()).getGangPlayer();
        if(gangTarget.getGangRank().getRankForce() > gangPlayer.getGangRank().getRankForce()) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        }

        playerGang.kickPlayer(target.getUniqueId());
        player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully kicked " + target.getName() + " &7!"));
    }
}
