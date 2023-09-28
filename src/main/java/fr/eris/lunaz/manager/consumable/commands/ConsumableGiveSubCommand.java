package fr.eris.lunaz.manager.consumable.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.consumable.data.Consumable;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.utils.inventory.ExtendInventory;
import fr.eris.lunaz.utils.item.ClickableItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ConsumableGiveSubCommand extends SubCommand {
    public ConsumableGiveSubCommand() {
        super("give", "lunaz.consumable.admin.give", true);
        extendInventory = new ExtendInventory();
        extendInventory.setInventoryName("&cConsumable [%current_pages%/%max_page%]");
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
        List<Consumable> consumableList = LunaZ.getConsumableManager().getAllConsumable(false);
        for(Consumable consumable : consumableList) {
            extendInventory.addItem(new ClickableItem(consumable::getAsItem,
            (event) -> {
                Consumable consumableToGive = LunaZ.getConsumableManager().getConsumable(event.getCurrentItem());
                if(!(event.getWhoClicked() instanceof Player) || consumableToGive == null) return;
                Player player = (Player) event.getWhoClicked();
                player.getInventory().addItem(consumable.getAsItem());
            }));
        }
    }
}
