package fr.eris.lunaz.manager.gang.data.upgrade;

import fr.eris.lunaz.manager.playerdata.data.currency.CurrencyType;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class LuckUpgrade {
    @Getter private int currentLevel = 0;
    private final List<Double> luckBoost = Arrays.asList(1.0d, 1.2d, 1.4d, 1.6d, 1.8d, 2.0d);
    private final List<UpgradePrice> upgradePrices;
    public LuckUpgrade() {
        upgradePrices = Arrays.asList(
                new UpgradePrice(CurrencyType.MONEY, 10000), new UpgradePrice(CurrencyType.MONEY, 20000),
                new UpgradePrice(CurrencyType.MONEY, 30000), new UpgradePrice(CurrencyType.MONEY, 40000),
                new UpgradePrice(CurrencyType.LUNA, 100));
    }

    public double getLuckBoost() {
        return luckBoost.get(currentLevel);
    }
}