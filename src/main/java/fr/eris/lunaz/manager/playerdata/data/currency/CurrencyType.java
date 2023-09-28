package fr.eris.lunaz.manager.playerdata.data.currency;

import lombok.Getter;

public enum CurrencyType {
    LUNA("&dLuna&r"), MONEY("&eMoney&r");

    @Getter private final String displayName;

    CurrencyType(String displayName) {
        this.displayName = displayName;
    }
}
