package fr.eris.lunaz.manager.tag.commands;

import fr.eris.lunaz.manager.commands.SubCommandExecutor;

import java.util.List;

public class TagExecutor extends SubCommandExecutor {
    public TagExecutor() {
        super("tag", null);
        registerCommands();
    }

    public void registerCommands() {
        setDefaultSubCommand(new TagPlayerList(), true);
        addSubCommand(new TagRevoke());
        addSubCommand(new TagGrant());
        addSubCommand(new TagCreate());
        addSubCommand(new TagDelete());
    }
}
