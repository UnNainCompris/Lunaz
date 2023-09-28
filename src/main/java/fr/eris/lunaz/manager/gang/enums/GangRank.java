package fr.eris.lunaz.manager.gang.enums;

import lombok.Getter;

public enum GangRank {
    ADMIN_STATUS(Integer.MAX_VALUE),
    LEADER(5), CO_LEADER(4), MOD(3), MEMBER(2), RECRUIT(1), NO_GANG(-1);

    @Getter private final int rankForce;

    GangRank(int rankForce) {
        this.rankForce = rankForce;
    }
}
