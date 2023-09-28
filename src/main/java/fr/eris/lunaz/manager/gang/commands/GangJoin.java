package fr.eris.lunaz.manager.gang.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GangJoin extends SubCommand {
    public GangJoin() {
        super("join", "lunaz.gang.join", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Gang requestedGang = null;
        if(args == null || args[0] == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument ! &8<GangName>"));
            return;
        } if((requestedGang = LunaZ.getGangManager().getGangByName(args[0])) == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7The gang " + args[0] + " &7doesn't exist!"));
            return;
        }
        if(requestedGang.getSendedPlayerInviteRequest().contains(player.getUniqueId())) {
            requestedGang.makeJoin(player, false);
            Bukkit.dispatchCommand(player, "gang show");
            player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully join the gang " + requestedGang.getGangDisplayName() + " &7!"));
        } else {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not invited in " + requestedGang.getGangDisplayName() + " &7!"));
        }
    }
}
