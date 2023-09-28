package fr.eris.lunaz.manager.magazine.commands;

import fr.eris.lunaz.manager.commands.SubCommandExecutor;

public class MagazineExecutor extends SubCommandExecutor {
    public MagazineExecutor() {
        super("magazine", null);
        registerSubcommands();
    }

    public void registerSubcommands() {
        this.setDefaultSubCommand(new MagazineGiveSubCommand(), true);
    }
}
