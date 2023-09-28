package fr.eris.lunaz.manager.consumable.commands;

import fr.eris.lunaz.manager.commands.SubCommandExecutor;

public class ConsumableExecutor extends SubCommandExecutor {
    public ConsumableExecutor() {
        super("consumable", null);
        registerSubcommands();
    }

    public void registerSubcommands() {
        this.setDefaultSubCommand(new ConsumableGiveSubCommand(), true);
    }
}
