package fr.eris.lunaz.manager.stats.data;

import lombok.Getter;

public class Stats {
    @Getter private double speed;
    @Getter private double health;
    @Getter private double damage;
    @Getter private double defence;

    public Stats(double speed, double health, double damage, double defence) {
        this.speed = speed;
        this.health = health;
        this.damage = damage;
        this.defence = defence;
    }

    public Stats addStats(Stats stats) {
        if(stats == null) return this;
        speed += stats.getSpeed();
        health += stats.getHealth();
        damage += stats.getDamage();
        defence += stats.getDefence();
        return this;
    }

    public static Stats emptyStats() {
        return new Stats(0, 0, 0, 0);
    }

    public static Stats toSpeedStats(double speed) {
        return new Stats(speed, 0, 0, 0);
    }

    public static Stats toHealthStats(double health) {
        return new Stats(0, health, 0, 0);
    }

    public static Stats toDamageStats(double damage) {
        return new Stats(0, 0, damage, 0);
    }

    public static Stats toDefenceStats(double defence) {
        return new Stats(0, 0, 0, defence);
    }
}
