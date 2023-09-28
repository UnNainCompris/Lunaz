package fr.eris.lunaz.manager.tag.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.tag.data.Tag;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagGrant extends SubCommand {
    public TagGrant() {
        super("grant", "lunaz.tag.grant");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = null;

        if(args == null || args[0] == null || args[1] == null) {
            sender.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument ! &8[<target> <tag>]"));
            return;
        } else if(LunaZ.getTagManager().getTag(args[1]) == null) {
            sender.sendMessage(ColorUtils.translate("&c[✗] &7The tag doesn't exist !"));
        } else if((target = Bukkit.getPlayer(args[0])) == null) {
            sender.sendMessage(ColorUtils.translate("&c[✗] &7" + args[0] + " &7is not online ! &8(Use '/gang show' and click on the target name)"));
            return;
        }
        Tag tag = LunaZ.getTagManager().getTag(args[1]);

        target.sendMessage(ColorUtils.translate("&9[;(] &7You was just granted the tag '" + tag.getDisplayName() + "&7'!"));
        sender.sendMessage(ColorUtils.translate("&a[✓] &7You grant the tag '" + tag.getDisplayName() + "&7' " +
                "to " + target.getName() + "&7!"));
    }
}
