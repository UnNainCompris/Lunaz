package fr.eris.lunaz.manager.weapon.data;

import com.google.gson.annotations.Expose;
import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.actionbar.Message;
import fr.eris.lunaz.manager.shooter.task.ShootTask;
import fr.eris.lunaz.manager.weapon.task.ReloadTask;
import fr.eris.lunaz.utils.ItemUtils;
import fr.eris.lunaz.utils.NBTUtils;
import fr.eris.lunaz.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Weapon {
    @Expose @Getter private final double weaponDamage;
    @Expose @Getter private final double bulletDispersion;
    @Expose @Getter private final long weaponShootingSpeed; // tick between shoot
    @Expose @Getter private final double bulletSpeed; // in block/s

    @Expose @Getter private final String magazineSystemName; // The bullet type required to reload weapon
    @Expose @Getter private final int reloadTime; // time per magazine in tick

    @Expose @Getter private final boolean canReloadWithBullet;
    @Expose @Getter private final int bulletReloadTime; // time per bullet in tick
    @Expose @Getter private final String bulletSystemName; // The bullet type required to reload weapon

    @Expose @Getter private final String displayName;
    @Expose @Getter private final String systemName;
    @Expose @Getter private final Material material;

    public Weapon(double weaponDamage, double bulletDispersion, long weaponShootingSpeed, int reloadTime, double bulletSpeed, String magazineSystemName, boolean canReloadWithBullet,
                  int bulletReloadTime, String bulletSystemName, String displayName, String systemName, Material material) {
        this.weaponDamage = weaponDamage;
        this.bulletDispersion = bulletDispersion;
        this.weaponShootingSpeed = weaponShootingSpeed;
        this.reloadTime = reloadTime;
        this.bulletSpeed = bulletSpeed;
        this.magazineSystemName = magazineSystemName;
        this.canReloadWithBullet = canReloadWithBullet;
        this.bulletReloadTime = bulletReloadTime;
        this.bulletSystemName = bulletSystemName;
        this.displayName = displayName;
        this.systemName = systemName;
        this.material = material;
    }

    public static Weapon defaultWeapon() {
        return new Weapon(10, 10, 5, 20, 20, "defaultMagazine", true,
                3, "defaultBullet", "&6DefaultWeapon", "defaultWeapon", Material.STICK);
    }

    public void shoot(Player shooter, ItemStack item) {
        if(!ShootTask.canShoot(shooter)) return;
        shooter.getInventory().setItemInMainHand(
                new NBTUtils(item).set("WeaponReamingBullets", new NBTItem(item).getInteger("WeaponReamingBullets") - 1).build());
        LunaZ.getShooterManager().shot(shooter, this, item);
    }

    public void tryReload(Player player, ItemStack weapon) {
        if(weapon.getAmount() > 1) {
            player.sendMessage("&c[LunaZ] &8 You can't reload stacked weapon !");
            return;
        } if(LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getStatus().isReloadingWeapon()) {
            return;
        }
        if(haveMagazine(player)) {
            new ReloadTask(player, weapon, this, ReloadTask.ReloadType.MAGAZINE);
        } else if(canReloadWithBullet && haveBullet(player)) {
            new ReloadTask(player, weapon, this, ReloadTask.ReloadType.BULLET);
        }
    }

    public boolean haveBullet(Player player) { // Check if the player have bullet with the bullet type in there inventory
        return LunaZ.getBulletManager().getBullet(bulletSystemName).haveBullet(player) != null;
    }

    public boolean haveMagazine(Player player) { // Check if the player have magazine with the magazine bullet type in there inventory
        return LunaZ.getMagazineManager().getMagazine(magazineSystemName).haveMagazine(player) != null;
    }

    public ItemStack getAsItem() {
        return new NBTUtils(ItemUtils.createItem(material, displayName, 1,
                Arrays.asList("&7Damage: &c" + weaponDamage,
                        "&7Shooting Speed: &7" + weaponShootingSpeed,
                        "&7Bullet dispersion: &7" + bulletDispersion,
                        "&7Reload time: &7" + reloadTime,
                        (canReloadWithBullet ? "&7Reload time: &7" + bulletReloadTime + "/bullet" : null)),
                null))
                .set("WeaponSystemName", this.systemName).set("WeaponReamingBullets", 0).build();
    }

    public static Weapon getItemAsWeapon(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if (!nbtItem.hasNBTData()) return null;
        if (!nbtItem.hasTag("WeaponSystemName")) return null;
        return LunaZ.getWeaponManager().getWeapon(nbtItem.getString("WeaponSystemName"));
    }

    public void damage(LivingEntity entity, Player shooter) {

    }

    public void displayBullet(Player player, ItemStack item, boolean keepMessageIfStillHoldWeapon) {
        Message.MessageUpdater message = () -> {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if(!LunaZ.getWeaponManager().isAnWeapon(weapon)) {
                LunaZ.getActionbarManager().getByPlayer(player).removeByReason("DisplayBullets");
                return "";
            }

            if(keepMessageIfStillHoldWeapon) {
                if(!player.getInventory().getItemInMainHand().equals(weapon)) {
                    LunaZ.getActionbarManager().getByPlayer(player).removeByReason("DisplayBullets");
                } else {
                    LunaZ.getActionbarManager().getByPlayer(player).getByReason("DisplayBullets").showCounter = 0;
                }
            }
            if(weapon.getType() == Material.AIR || weapon.getAmount() == 0) return "";
            return StringUtils.fastReplace("&7Ammo: [{current}]", "{current}->" + NBTUtils.toNBT(weapon).getInteger("WeaponReamingBullets"));
        };
        LunaZ.getActionbarManager().add(player, new Message(40, "DisplayBullets", message));
    }
}
