package fr.eris.lunaz.manager.district.data;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class District {
    @Expose @Getter @Setter private List<Integer> firstCorner;
    @Expose @Getter @Setter private List<Integer> secondCorner;

    @Expose @Getter @Setter private List<String> accessPermission;

    @Expose @Getter @Setter private String districtName;
    @Expose @Getter @Setter private String scoreboardDisplayName;
    @Expose @Getter @Setter private boolean displayPlayerInDistrict;

    @Expose @Getter @Setter private long minimumPlayerOnline;
    @Expose @Getter @Setter private List<String> bypassPermission;

    @Expose @Getter @Setter private boolean radioactive;
    @Expose @Getter @Setter private int radioactiveForce;

    @Expose @Getter @Setter private String districtInfo;
    @Expose @Getter @Setter private String districtDifficulty; // S, A, B, C, D, E (+ intermediate rank like [E+, E, E-])

    public boolean isIn(Location location) {
        if(firstCorner == null || secondCorner == null) return false;
        double maxX, minX, maxY, minY, maxZ, minZ;
        maxX = Math.max(firstCorner.get(0), secondCorner.get(0));
        minX = Math.min(firstCorner.get(0), secondCorner.get(0));
        maxY = Math.max(firstCorner.get(1), secondCorner.get(1));
        minY = Math.min(firstCorner.get(1), secondCorner.get(1));
        maxZ = Math.max(firstCorner.get(2), secondCorner.get(2));
        minZ = Math.min(firstCorner.get(2), secondCorner.get(2));
        return  maxX >= location.getX() && location.getX() >= minX &&
                maxY >= location.getY() && location.getY() >= minY &&
                maxZ >= location.getZ() && location.getZ() >= minZ;
    }

    public static District defaultDistrict() {
        District defaultDistrict = new District();
        defaultDistrict.firstCorner = Arrays.asList(0, 0, 0);
        defaultDistrict.secondCorner = Arrays.asList(0, 0, 0);

        defaultDistrict.accessPermission = Arrays.asList("none");

        defaultDistrict.districtName = "DefaultDistrict";
        defaultDistrict.scoreboardDisplayName = "&6Default";

        defaultDistrict.minimumPlayerOnline = 0;
        defaultDistrict.bypassPermission = Arrays.asList("lunaz.district.bypass.vip");

        defaultDistrict.radioactive = false;
        defaultDistrict.radioactiveForce = -1;

        defaultDistrict.districtInfo = "This is a default placeholder district";
        defaultDistrict.districtDifficulty = "S++";
        return defaultDistrict;
    }

    public List<Player> getPlayerInDistrict() {
        List<Player> playersInDistrict = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(isIn(player.getLocation()))
                playersInDistrict.add(player);
        }
        return playersInDistrict;
    }

    public String getPlayerInDistrictDisplay() {
        if(!displayPlayerInDistrict) return "&k%%%%";
        else return String.valueOf(getPlayerInDistrict().size());
    }
}
