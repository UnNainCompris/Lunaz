package fr.eris.lunaz.manager.tag.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.tag.data.Tag;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagDelete extends SubCommand {
    public TagDelete() {
        super("delete", "lunaz.tag.delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args == null || args[0] == null || args[1] == null) {
            sender.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument ! &8[<tag>]"));
            return;
        } else if(LunaZ.getTagManager().getTag(args[1]) == null) {
            sender.sendMessage(ColorUtils.translate("&c[✗] &7The tag doesn't exist !"));
        }
        Tag tag = LunaZ.getTagManager().getTag(args[1]);
        sender.sendMessage(ColorUtils.translate("&a[✓] &7You successfully delete the tag '" + tag.getSystemName() + "&7' " +
                "(" + tag.getDisplayName() + "&7) !"));
        LunaZ.getTagManager().deleteTag(tag.getSystemName());
    }
}
