package fr.eris.lunaz.event.weapon;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.magazine.ReloadMagazineEvent;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponReloadMagazineEvent extends WeaponEvent implements Cancellable {

    @Getter @Setter private boolean cancelled;
    @Getter private final Tuple<Magazine, ItemStack> magazine;

    public WeaponReloadMagazineEvent(Player player, Tuple<Weapon, ItemStack> weapon, Tuple<Magazine, ItemStack> magazine) {
        super(player, weapon);
        this.magazine = magazine;
    }

    public static WeaponReloadMagazineEvent fromItem(Player player, ItemStack weapon, ItemStack magazine) {
        Weapon weaponType = null;
        NBTItem weaponNbt = new NBTItem(weapon);
        if(weaponNbt.hasTag("WeaponSystemName")) weaponType = LunaZ.getWeaponManager().getWeapon(weaponNbt.getString("WeaponSystemName"));

        Magazine magazineType = null;
        NBTItem magazineNbt = new NBTItem(magazine);
        if(magazineNbt.hasTag("MagazineSystemName")) magazineType = LunaZ.getMagazineManager().getMagazine(magazineNbt.getString("MagazineSystemName"));

        if(weaponType == null || magazineType == null) return null;
        return new WeaponReloadMagazineEvent(player, new Tuple<>(weaponType, weapon), new Tuple<>(magazineType, magazine));
    }
}
