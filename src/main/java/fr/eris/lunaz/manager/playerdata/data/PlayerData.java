package fr.eris.lunaz.manager.playerdata.data;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.playerdata.data.control.PlayerControl;
import fr.eris.lunaz.manager.playerdata.data.currency.Currency;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import lombok.Getter;

import java.util.UUID;

public class PlayerData {

    @Expose @Getter private final UUID uuid;
    @Expose @Getter private final PlayerStats stats;
    @Expose @Getter private final PlayerLuck luck;
    @Expose @Getter private final PlayerStatistics statistics;
    @Expose @Getter private final PlayerStatus status;
    @Expose @Getter private final PlayerControl control;
    @Expose @Getter private final PlayerSettings settings;
    @Expose @Getter private final PlayerStaff staff;
    @Expose @Getter private final PlayerTag tag;
    @Expose @Getter private final Currency money;
    @Expose @Getter private final Currency luna;
    @Expose @Getter private final GangPlayer gangPlayer;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.statistics = new PlayerStatistics();
        this.luck = new PlayerLuck();
        this.stats = new PlayerStats();
        this.status = new PlayerStatus();
        this.control = new PlayerControl();
        this.settings = new PlayerSettings();
        this.tag = new PlayerTag();
        this.staff = new PlayerStaff();
        this.money = new Currency(uuid);
        this.luna = new Currency(uuid);
        this.gangPlayer = new GangPlayer(uuid);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return LunaZ.getPlayerDataManager().getPlayerdata(uuid);
    }
}
