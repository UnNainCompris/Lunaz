package fr.eris.lunaz.event.magazine;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.weapon.WeaponReloadBulletEvent;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ReloadMagazineEvent extends MagazineEvent implements Cancellable {

    @Getter @Setter private boolean cancelled;
    @Getter private final Tuple<Bullet, ItemStack> bullet;
    public ReloadMagazineEvent(Player player, Tuple<Magazine, ItemStack> magazine, Tuple<Bullet, ItemStack> bullet) {
        super(player, magazine);
        this.bullet = bullet;
    }

    public static ReloadMagazineEvent fromItem(Player player, ItemStack magazine, ItemStack bullet) {
        Magazine magazineType = null;
        NBTItem magazineNbt = new NBTItem(magazine);
        if(magazineNbt.hasTag("MagazineSystemName")) magazineType = LunaZ.getMagazineManager().getMagazine(magazineNbt.getString("MagazineSystemName"));

        Bullet bulletType = null;
        NBTItem bulletNbt = new NBTItem(bullet);
        if(bulletNbt.hasTag("BulletSystemName")) bulletType = LunaZ.getBulletManager().getBullet(bulletNbt.getString("BulletSystemName"));

        if(bulletType == null || magazineType == null) return null;
        return new ReloadMagazineEvent(player, new Tuple<>(magazineType, magazine), new Tuple<>(bulletType, bullet));
    }
}
