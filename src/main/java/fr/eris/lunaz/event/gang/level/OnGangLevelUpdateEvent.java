package fr.eris.lunaz.event.gang.level;

import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.event.gang.GangEvent;
import fr.eris.lunaz.event.player.PlayerDataEvent;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

public class OnGangLevelUpdateEvent extends GangEvent implements Cancellable {
    @Getter @Setter private boolean cancelled;

    @Getter @Setter private long newLevel;
    @Getter private final long oldLevel;
    public OnGangLevelUpdateEvent(Gang gang, long oldLevel, long newLevel) {
        super(gang);
        this.newLevel = newLevel;
        this.oldLevel = oldLevel;
    }
}
