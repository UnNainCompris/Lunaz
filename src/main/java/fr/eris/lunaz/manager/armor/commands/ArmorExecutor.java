package fr.eris.lunaz.manager.armor.commands;

import fr.eris.lunaz.manager.commands.SubCommandExecutor;

public class ArmorExecutor extends SubCommandExecutor {
    public ArmorExecutor() {
        super("armor", null);
        registerSubcommands();
    }

    public void registerSubcommands() {
        this.setDefaultSubCommand(new ArmorGiveSubCommand(), true);
    }
}
