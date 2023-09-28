package fr.eris.lunaz.manager.melee.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.melee.data.Melee;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.utils.inventory.ExtendInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MeleeGiveSubCommand extends SubCommand {
    public MeleeGiveSubCommand() {
        super("give", "lunaz.melee.admin.give", true);
        extendInventory = new ExtendInventory();
        extendInventory.setInventoryName("&Melee [%current_pages%/%max_page%]");
        extendInventory.setInventorySize(27);
    }

    private final ExtendInventory extendInventory;

    @Override
    public void execute(CommandSender sender, String[] args) {
        extendInventory.removeAll();
        loadExtendInventory();
        extendInventory.update((Player)sender);
    }

    public void loadExtendInventory() {
        List<Melee> meleeList = LunaZ.getMeleeManager().getAllMelee(false);
        for(Melee melee : meleeList) {
            extendInventory.addItem(new ClickableItem(melee::getAsDisplayItem,
            (event) -> {
                Melee meleeToGive = LunaZ.getMeleeManager().getMelee(event.getCurrentItem());
                if(!(event.getWhoClicked() instanceof Player) || meleeToGive == null) return;
                Player player = (Player) event.getWhoClicked();
                player.getInventory().addItem(melee.getAsItem(player));
            }));
        }
    }
}
