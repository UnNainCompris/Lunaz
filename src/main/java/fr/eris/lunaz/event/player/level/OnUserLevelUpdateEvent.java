package fr.eris.lunaz.event.player.level;

import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.event.player.PlayerDataEvent;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

public class OnUserLevelUpdateEvent extends PlayerDataEvent implements Cancellable {
    @Getter @Setter private boolean cancelled;

    @Getter @Setter private long newLevel;
    @Getter private final long oldLevel;
    public OnUserLevelUpdateEvent(PlayerData player, long oldLevel, long newLevel) {
        super(player);
        this.newLevel = newLevel;
        this.oldLevel = oldLevel;
    }
}
