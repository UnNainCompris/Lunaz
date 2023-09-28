package fr.eris.lunaz.manager.consumable.data;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.manager.stats.task.TemporaryStatsUpdater;
import lombok.Getter;

public class ConsumableStatsBonus {
    @Expose @Getter private final double value;
    @Expose @Getter private final long tickTime;
    @Expose @Getter private final TemporaryStatsUpdater.Action todoIfAlreadyHaveBoost;
    @Expose @Getter private String customReasonName = "none";


    public ConsumableStatsBonus(double value, long tickTime, TemporaryStatsUpdater.Action todoIfAlreadyHaveBoost) {
        this.value = value;
        this.tickTime = tickTime;
        this.todoIfAlreadyHaveBoost = todoIfAlreadyHaveBoost;
    }

    @Override
    public String toString() {
        String operator = "";
        if(value > 0) operator = "+";
        else if (value < 0) operator = "-";
        return operator + value + " (" + tickTime / 20 + "s)";
    }
}
