package fr.eris.lunaz.event.shoot;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.weapon.WeaponReloadBulletEvent;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShootBlockEvent extends ShootEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled;
    @Getter private final Block target;

    public ShootBlockEvent(Player shooter, Tuple<Weapon, ItemStack> weapon, Block target) {
        super(shooter, weapon);
        this.target = target;
    }

    public static ShootBlockEvent fromItem(Player player, ItemStack weapon, Block target) {
        Weapon weaponType = null;
        NBTItem weaponNbt = new NBTItem(weapon);
        if(weaponNbt.hasTag("WeaponSystemName")) weaponType = LunaZ.getWeaponManager().getWeapon(weaponNbt.getString("WeaponSystemName"));

        if(weaponType == null) return null;
        return new ShootBlockEvent(player, new Tuple<>(weaponType, weapon), target);
    }
}
