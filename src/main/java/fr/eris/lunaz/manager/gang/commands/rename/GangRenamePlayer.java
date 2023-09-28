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

public class GangRenamePlayer extends SubCommand {
    public GangRenamePlayer() {
        super("renameplayer", "lunaz.gang.renameplayer", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Player target = null;
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        Gang playerGang = LunaZ.getGangManager().getPlayerGang(player);
        boolean self = false;
        if(playerGang == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You need to &chave &7a gang to rename a player ! &8(/gang create <GangName>)"));
            return;
        } if(args == null || args[0] == null) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument ! &8[<NewName> (target)]"));
            return;
        }

        if(args[1] == null) {
            if(!gangPlayer.havePermission(GangPermission.RENAME_SELF, true)) {
                player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
                return;
            }
            self = true;
        }

        if(!self && !gangPlayer.havePermission(GangPermission.RENAME_PLAYER, true)) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7You are not allowed to do this !"));
            return;
        }

        if(!self) {
            target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                player.sendMessage(ColorUtils.translate("&c[✗] &7The target should be online to be renamed !"));
                return;
            }
        } else target = player;

        if(ColorUtils.translate(args[0]).length() < 5) {
            player.sendMessage(ColorUtils.translate("&c[✗] &7The new name is too short !"));
            return;
        }

        playerGang.renameGangPlayer(target, args[0], false);
        player.sendMessage(ColorUtils.translate("&a[✓] &7You successfully rename your gang mate into " + ColorUtils.translate(args[0]) + " &7!"));
    }
}
