package fr.eris.lunaz.manager.bullet.commands;

import fr.eris.lunaz.manager.commands.SubCommandExecutor;

public class BulletExecutor extends SubCommandExecutor {
    public BulletExecutor() {
        super("bullet", null);
        registerSubcommands();
    }

    public void registerSubcommands() {
        this.setDefaultSubCommand(new BulletGiveSubCommand(), true);
    }
}
