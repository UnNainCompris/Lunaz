package fr.eris.lunaz.manager.gang.data.upgrade;

import fr.eris.lunaz.manager.playerdata.data.currency.CurrencyType;
import lombok.Getter;

public class UpgradePrice {
    @Getter private final CurrencyType currency;
    @Getter private final long value;

    public UpgradePrice(CurrencyType currency, long value) {
        this.currency = currency;
        this.value = value;
    }
}
