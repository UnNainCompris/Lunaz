package fr.eris.lunaz.event.weapon;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponReloadBulletEvent extends WeaponEvent implements Cancellable {

    @Getter @Setter private boolean cancelled;
    @Getter private final Tuple<Bullet, ItemStack> bullet;

    public WeaponReloadBulletEvent(Player player, Tuple<Weapon, ItemStack> weapon, Tuple<Bullet, ItemStack> bullet) {
        super(player, weapon);
        this.bullet = bullet;
    }

    public static WeaponReloadBulletEvent fromItem(Player player, ItemStack weapon, ItemStack bullet) {
        Weapon weaponType = null;
        NBTItem weaponNbt = new NBTItem(weapon);
        if(weaponNbt.hasTag("WeaponSystemName")) weaponType = LunaZ.getWeaponManager().getWeapon(weaponNbt.getString("WeaponSystemName"));

        Bullet bulletType = null;
        NBTItem bulletNbt = new NBTItem(bullet);
        if(bulletNbt.hasTag("BulletSystemName")) bulletType = LunaZ.getBulletManager().getBullet(bulletNbt.getString("BulletSystemName"));

        if(weaponType == null || bulletType == null) return null;
        return new WeaponReloadBulletEvent(player, new Tuple<>(weaponType, weapon), new Tuple<>(bulletType, bullet));
    }
}
