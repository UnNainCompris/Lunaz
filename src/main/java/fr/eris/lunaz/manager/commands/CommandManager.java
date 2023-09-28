package fr.eris.lunaz.manager.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.commands.DiscordCommands;
import fr.eris.lunaz.utils.nms.NmsUtils;
import fr.eris.manager.Manager;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

public class CommandManager extends Manager {
    private final CommandMap commandMap;

    public CommandManager() {
        commandMap = NmsUtils.getCommandMap();
        registersCommands();
    }

    private void registersCommands() {
        registerCommand(new DiscordCommands());
    }

    public void registerCommand(BukkitCommand command) {
        commandMap.register(LunaZ.getConfiguration().name().toLowerCase(), command);
    }
}
