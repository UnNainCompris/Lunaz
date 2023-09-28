package fr.eris.lunaz.event.player.currency;

import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.event.player.PlayerDataEvent;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

public class OnUserCurrencyUpdateEvent extends PlayerDataEvent implements Cancellable {
    @Getter @Setter private boolean cancelled;

    @Getter @Setter private long newValue;
    @Getter private final long oldValue;
    public OnUserCurrencyUpdateEvent(PlayerData player, long oldValue, long newValue) {
        super(player);
        this.newValue = newValue;
        this.oldValue = oldValue;
    }
}