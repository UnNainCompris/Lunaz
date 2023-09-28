package fr.eris.lunaz.event.magazine;

import fr.eris.event.events.Event;
import fr.eris.lunaz.event.player.PlayerDataEvent;
import fr.eris.lunaz.event.player.PlayerEvent;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MagazineEvent extends PlayerEvent {
    @Getter private final Tuple<Magazine, ItemStack> magazine;
    public MagazineEvent(Player player, Tuple<Magazine, ItemStack> magazine) {
        super(player);
        this.magazine = magazine;
    }
}
