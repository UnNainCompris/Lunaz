package fr.eris.lunaz.event.gang.currency;

import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.event.gang.GangEvent;
import fr.eris.lunaz.event.player.PlayerDataEvent;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

public class OnGangBankUpdateEvent extends GangEvent implements Cancellable {
    @Getter @Setter private boolean cancelled;

    @Getter @Setter private long newValue;
    @Getter private final long oldValue;
    public OnGangBankUpdateEvent(Gang gang, long oldValue, long newValue) {
        super(gang);
        this.newValue = newValue;
        this.oldValue = oldValue;
    }
}