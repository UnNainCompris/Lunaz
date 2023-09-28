package fr.eris.lunaz.manager.playerdata.data;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public class PlayerStatistics {
    @Expose @Getter @Setter private long playerKills = 0;
    @Expose @Getter @Setter private long playerDeath = 0;
    @Expose @Getter @Setter private long zombieKills = 0;
    @Expose @Getter @Setter private long messageSent = 0;
    @Expose @Getter @Setter private long commandExecuted = 0;
    @Expose @Getter @Setter private long blockTravel = 0;
}
