package fr.eris.lunaz.manager.melee.commands;

import fr.eris.lunaz.manager.commands.SubCommandExecutor;

public class MeleeExecutor extends SubCommandExecutor {
    public MeleeExecutor() {
        super("melee", null);
        registerSubcommands();
    }

    public void registerSubcommands() {
        this.setDefaultSubCommand(new MeleeGiveSubCommand(), true);
    }
}
