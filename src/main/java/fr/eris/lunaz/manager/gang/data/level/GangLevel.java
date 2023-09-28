package fr.eris.lunaz.manager.gang.data.level;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.gang.level.OnGangExperienceUpdateEvent;
import fr.eris.lunaz.event.gang.level.OnGangLevelUpdateEvent;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;

import java.util.UUID;

public class GangLevel {
    @Expose @Getter private long level;
    @Expose @Getter private long experience;
    @Expose @Getter private UUID ownerID; // GangID

    public GangLevel(UUID ownerID) {
        this.ownerID = ownerID;
    }

    /**
     * @param newLevel The value to add (remind to put - when need to remove)
     * @return the final value after the event and adding the value to add
     */
    public Tuple<Long, Boolean> postChangeLevelEvent(long newLevel) {
        OnGangLevelUpdateEvent event = new OnGangLevelUpdateEvent(LunaZ.getGangManager().getGang(ownerID), level, newLevel);
        LunaZ.getEventManager().postEvent(event);
        return new Tuple<>(event.getNewLevel(), event.isCancelled());
    }

    public void addLevel(long amount) {
        if(amount <= 0) {
            LunaZ.log("Amount should be a positive integer [ADD] " + amount);
            return;
        }
        Tuple<Long, Boolean> result = postChangeLevelEvent(level + amount);
        if(!result.getSecond()) level = Math.max(1, result.getFirst());
    }

    public void removeLevel(long amount) {
        if(amount <= 0) {
            LunaZ.log("Amount should be a positive integer [REMOVE] " + amount);
            return;
        }
        Tuple<Long, Boolean> result = postChangeLevelEvent(Math.max(1, level - amount));
        if(!result.getSecond()) level = Math.max(1, result.getFirst());
    }

    /**
     * @param newExperience The value to add (remind to put - when need to remove)
     * @return the final value after the event and adding the value to add
     */
    public Tuple<Long, Boolean> postChangeExperienceEvent(long newExperience) {
        OnGangExperienceUpdateEvent event = new OnGangExperienceUpdateEvent(LunaZ.getGangManager().getGang(ownerID), experience, newExperience);
        LunaZ.getEventManager().postEvent(event);
        return new Tuple<>(event.getNewExperience(), event.isCancelled());
    }

    public void addExperience(long amount, boolean allowUpdate) {
        if(amount <= 0) {
            LunaZ.log("Amount should be a positive integer [ADD] " + amount);
            return;
        }
        Tuple<Long, Boolean> result = postChangeExperienceEvent(experience + amount);
        if(!result.getSecond()) experience = result.getFirst();
        if(allowUpdate) updateExperience();
    }

    public void removeExperience(long amount, boolean allowUpdate) {
        if(amount <= 0) {
            LunaZ.log("Amount should be a positive integer [REMOVE] " + amount);
            return;
        }
        Tuple<Long, Boolean> result = postChangeExperienceEvent(experience - amount);
        if(!result.getSecond()) experience = result.getFirst();
        if(allowUpdate) updateExperience();
    }

    public void updateExperience() {
        if(experience >= getRequiredExperience()) {
            removeExperience(getRequiredExperience(), false);
            addLevel(1);
            updateExperience();
        } else if(experience < 0) {
            if(level == 1) {
                experience = 0;
                return;
            }
            removeLevel(1);
            addExperience(getRequiredExperience(level), false);
            updateExperience();
        }
    }

    public long getRequiredExperience() {
        return (long) (((level * Math.sqrt(level / 1000d)) * 1000d) + 1000d);
    }

    public long getRequiredExperience(long level) {
        return (long) (((level * Math.sqrt(level / 1000d)) * 1000d) + 1000d);
    }
}
