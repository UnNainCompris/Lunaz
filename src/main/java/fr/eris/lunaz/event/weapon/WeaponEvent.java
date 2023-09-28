package fr.eris.lunaz.event.weapon;

import fr.eris.lunaz.event.player.PlayerEvent;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponEvent extends PlayerEvent {
    @Getter private final Tuple<Weapon, ItemStack> weapon;
    public WeaponEvent(Player player, Tuple<Weapon, ItemStack> weapon) {
        super(player);
        this.weapon = weapon;
    }
}
