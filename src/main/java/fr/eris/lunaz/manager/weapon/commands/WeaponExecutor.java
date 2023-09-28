package fr.eris.lunaz.manager.weapon.commands;

import fr.eris.lunaz.manager.commands.SubCommandExecutor;

import java.util.List;

public class WeaponExecutor extends SubCommandExecutor {
    public WeaponExecutor() {
        super("weapon", null);
        registerSubcommands();
    }

    public void registerSubcommands() {
        this.setDefaultSubCommand(new WeaponGiveSubCommand(), true);
    }
}
