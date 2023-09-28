package fr.eris.lunaz.manager.gang.commands;

import fr.eris.lunaz.manager.commands.SubCommandExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GangExecutor extends SubCommandExecutor {
    public GangExecutor() {
        super("gang", Collections.singletonList("g"), null);
        registerCommands();
    }

    public void registerCommands() {

    }
}
