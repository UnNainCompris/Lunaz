package fr.eris.lunaz;

import fr.eris.event.manager.EventManager;
import fr.eris.lunaz.manager.actionbar.ActionbarManager;
import fr.eris.lunaz.manager.armor.ArmorManager;
import fr.eris.lunaz.manager.bullet.BulletManager;
import fr.eris.lunaz.manager.chat.ChatManager;
import fr.eris.lunaz.manager.commands.CommandManager;
import fr.eris.lunaz.manager.consumable.ConsumableManager;
import fr.eris.lunaz.manager.damage.DamageManager;
import fr.eris.lunaz.manager.district.DisctrictManager;
import fr.eris.lunaz.manager.explosive.ExplosiveManager;
import fr.eris.lunaz.manager.gang.GangManager;
import fr.eris.lunaz.manager.magazine.MagazineManager;
import fr.eris.lunaz.manager.melee.MeleeManager;
import fr.eris.lunaz.manager.playerdata.PlayerDataManager;
import fr.eris.lunaz.manager.scoreboard.ScoreboardManager;
import fr.eris.lunaz.manager.stats.StatsManager;
import fr.eris.lunaz.manager.tag.TagManager;
import fr.eris.lunaz.manager.weapon.WeaponManager;
import fr.eris.lunaz.manager.shooter.ShooterManager;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.manager.ManagerEnabler;
import fr.eris.manager.ManagerPriority;
import fr.eris.manager.Priority;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@ErisConfiguration(version = "Beta - 0.1", permissionPrefix = "lunaz", name = "LunaZ")
public final class LunaZ extends JavaPlugin {

    @Getter private static LunaZ instance;
    @Getter private static ErisConfiguration configuration;
    @Getter private static boolean running;

    @Getter @ManagerPriority(initPriority = Priority.HIGHEST) private static CommandManager commandManager;
    @Getter @ManagerPriority(initPriority = Priority.HIGHEST) private static EventManager eventManager;
    @Getter @ManagerPriority(initPriority = Priority.HIGH) private static DamageManager damageManager;
    @Getter @ManagerPriority(initPriority = Priority.HIGH) private static PlayerDataManager playerDataManager;
    @Getter @ManagerPriority(initPriority = Priority.HIGH) private static DisctrictManager disctrictManager;
    @Getter @ManagerPriority(initPriority = Priority.HIGH) private static StatsManager statsManager;
    @Getter @ManagerPriority(initPriority = Priority.HIGH) private static GangManager gangManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static ConsumableManager consumableManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static MagazineManager magazineManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static BulletManager bulletManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static WeaponManager weaponManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static ExplosiveManager explosiveManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static MeleeManager meleeManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static ArmorManager armorManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static ShooterManager shooterManager;
    @Getter @ManagerPriority(initPriority = Priority.NORMAL) private static TagManager tagManager;
    @Getter @ManagerPriority(initPriority = Priority.LOWEST) private static ChatManager chatManager;
    @Getter @ManagerPriority(initPriority = Priority.LOWEST) private static ScoreboardManager scoreboardManager;
    @Getter @ManagerPriority(initPriority = Priority.LOWEST) private static ActionbarManager actionbarManager;

    public static void log(String message) {
        LunaZ.getInstance().getLogger().info(message);
    }

    @Override
    public void onEnable() {
        running = true;
        if(!vitalSetup()) return;
        ManagerEnabler.init(this);
    }

    private boolean vitalSetup() {
        instance = this;

        ErisConfiguration configurationAnno = this.getClass().getAnnotation(ErisConfiguration.class);
        if(configurationAnno != null) {
            configuration = configurationAnno;
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        FileUtils.ROOT_FOLDER = this.getDataFolder();
        return true;
    }

    public void onDisable() {
        running = false;
        ManagerEnabler.stop(this);
    }
}
