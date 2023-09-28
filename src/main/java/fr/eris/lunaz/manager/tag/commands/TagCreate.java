package fr.eris.lunaz.manager.tag.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.tag.data.Tag;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCreate extends SubCommand {
    public TagCreate() {
        super("create", "lunaz.tag.create");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args == null || args[0] == null || args[1] == null) {
            sender.sendMessage(ColorUtils.translate("&c[✗] &7Missing argument ! &8[<Tag> <TagDisplayName>]"));
            return;
        } else if(LunaZ.getTagManager().getTag(args[1]) != null) {
            sender.sendMessage(ColorUtils.translate("&c[✗] &7This tag already exist !"));
        }
        Tag newTag = new Tag(args[0], args[1]);
        sender.sendMessage(ColorUtils.translate("&a[✓] &7You create the tag '" + newTag.getSystemName() + "&7' " +
                "(" + newTag.getDisplayName() + "&7) !"));
        LunaZ.getTagManager().saveTag(newTag);
    }
}
