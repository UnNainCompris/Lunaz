package fr.eris.lunaz.manager.armor.data;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Random;

public class ArmorStatsBonus {
    private static final Random random = new Random();
    @Expose @Getter private final double min;
    @Expose @Getter private final double max;

    public ArmorStatsBonus(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getRandomValue(Player requester) {
        PlayerData playerData = LunaZ.getPlayerDataManager().getPlayerdata(requester.getUniqueId());
        double luckValue = playerData.getLuck().getArmorLuck();
        return Math.max(min, Math.min(max, random.nextDouble(min, max) * luckValue));
    }
}
