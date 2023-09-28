package fr.eris.lunaz.manager.commands.commands;

import fr.eris.lunaz.manager.commands.BaseCommand;
import fr.eris.lunaz.utils.ColorUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DiscordCommands extends BaseCommand {
    public DiscordCommands() {
        super("discord", "erisdupe.discord");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(ColorUtils.translate("&bhttps://discord.gg/CWKQ54jv7")); // TODO: 21/08/2023 Redo this shit 
        return true;
    }
}
