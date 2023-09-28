package fr.eris.lunaz.manager.playerdata.data.currency;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.player.currency.OnUserCurrencyUpdateEvent;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;

import java.util.UUID;

public class Currency {

    @Expose @Getter private long value;
    @Expose @Getter private final UUID ownerID;

    public Currency(UUID ownerID) {
        this.ownerID = ownerID;
    }

    /**
     * @param newValue The value to add (remind to put - when need to remove)
     * @return the final value after the event and adding the value to add
     */
    public Tuple<Long, Boolean> postChangeValueEvent(long newValue) {
        OnUserCurrencyUpdateEvent event = new OnUserCurrencyUpdateEvent(LunaZ.getPlayerDataManager().getPlayerdata(ownerID), value, newValue);
        LunaZ.getEventManager().postEvent(event);
        return new Tuple<>(event.getNewValue(), event.isCancelled());
    }

    public void add(long amount) {
        if(amount <= 0) {
            LunaZ.log("Amount should be a positive integer [ADD] " + amount);
            return;
        }
        Tuple<Long, Boolean> result = postChangeValueEvent(value + amount);
        if(!result.getSecond()) value = result.getFirst();
    }

    public boolean has(long amount) {
        if(amount <= 0) {
            LunaZ.log("Amount should be a positive integer [HAS] " + amount);
            return false;
        }
        return value >= amount;
    }

    public void remove(long amount, boolean canBeNegative) {
        if(amount <= 0) {
            LunaZ.log("Amount should be a positive integer [REMOVE] " + amount);
            return;
        }
        Tuple<Long, Boolean> result;

        if(canBeNegative) result = postChangeValueEvent(value - amount);
        else result = postChangeValueEvent(Math.max(0, value - amount));

        if(!result.getSecond()) value = result.getFirst();
    }
}
