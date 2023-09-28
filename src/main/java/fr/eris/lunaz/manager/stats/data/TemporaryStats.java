package fr.eris.lunaz.manager.stats.data;

import fr.eris.lunaz.utils.GetValue;
import lombok.Getter;
import lombok.Setter;

public class TemporaryStats {
    @Getter private final Stats stats;
    @Setter @Getter private long maxTickTime;
    @Getter private final GetValue<Boolean> condition;
    @Setter @Getter private long currentTickTime;

    @Getter private final String reason;

    public TemporaryStats(Stats stats, long maxTickTime, String reason) {
        this.stats = stats;
        this.maxTickTime = maxTickTime;
        this.reason = reason;
        this.condition = null;
    }

    public TemporaryStats(Stats stats, GetValue<Boolean> condition, String reason) {
        this.stats = stats;
        this.reason = reason;
        this.maxTickTime = -1;
        this.condition = condition;
    }

    public boolean update() {
        currentTickTime++;
        if(condition == null) return maxTickTime <= currentTickTime;
        return condition.getValue();
    }

    public void setTimer(long maxTickTime, long currentTickTime) {
        this.maxTickTime = maxTickTime;
        this.currentTickTime = currentTickTime;
    }
}
