package fr.eris.lunaz.manager.gang.commands.admin;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.gang.enums.GangRank;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GangForceAdmin extends SubCommand {
    public GangForceAdmin() {
        super("forceadmin", "lunaz.gang.forceadmin", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to be in a gang to use this command !"));
            return;
        }

        gangPlayer.setGangRank(GangRank.ADMIN_STATUS);
        player.sendMessage(ColorUtils.translate("&a[✓] &7You have now full access of this gang until " +
                "you disconnect or leave the gang &7!"));
    }
}
