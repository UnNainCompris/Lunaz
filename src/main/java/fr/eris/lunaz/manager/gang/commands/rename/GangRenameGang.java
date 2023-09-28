package fr.eris.lunaz.manager.gang.commands.rename;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GangRenameGang extends SubCommand {
    public GangRenameGang() {
        super("renamegang", "lunaz.gang.renamegang", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to rename your gang ! &8(/gang create <GangName>)"));
            return;
        } if(!gangPlayer.havePermission(GangPermission.RENAME_GANG, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        } if(args == null || args[0] == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument !"));
            return;
        } if(ColorUtils.translate(args[0]).length() < 5) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7The new name is too short !"));
            return;
        }
        playerGang.renameGang(args[0], false);
        player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully rename your gang into " + ColorUtils.translate(args[0]) + " &7!"));
    }
}
