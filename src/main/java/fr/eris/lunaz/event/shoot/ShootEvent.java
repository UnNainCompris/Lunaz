package fr.eris.lunaz.event.shoot;

import fr.eris.event.events.Event;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShootEvent implements Event {
    @Getter private final Player shooter;
    @Getter private final Tuple<Weapon, ItemStack> weapon;
    public ShootEvent(Player shooter, Tuple<Weapon, ItemStack> weapon) {
        this.shooter = shooter;
        this.weapon = weapon;
    }
}
