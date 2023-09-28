package fr.eris.lunaz.manager.gang.data.upgrade;

import fr.eris.lunaz.manager.playerdata.data.currency.CurrencyType;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class MaxMemberUpgrade {
    @Getter private int currentLevel = 0;
    private final List<Integer> maxMember = Arrays.asList(3, 5, 7, 10, 12, 15, 17, 20, 23, 25);
    private final List<UpgradePrice> upgradePrices;
    public MaxMemberUpgrade() {
        upgradePrices = Arrays.asList(
                new UpgradePrice(CurrencyType.MONEY, 10000), new UpgradePrice(CurrencyType.MONEY, 20000),
                new UpgradePrice(CurrencyType.MONEY, 30000), new UpgradePrice(CurrencyType.MONEY, 40000),
                new UpgradePrice(CurrencyType.MONEY, 50000), new UpgradePrice(CurrencyType.MONEY, 60000),
                new UpgradePrice(CurrencyType.MONEY, 70000), new UpgradePrice(CurrencyType.MONEY, 80000),
                new UpgradePrice(CurrencyType.LUNA, 100), new UpgradePrice(CurrencyType.LUNA, 1000));
    }

    public double getMaxMember() {
        return maxMember.get(currentLevel);
    }
}
