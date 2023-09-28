package fr.eris.lunaz.manager.gang.data.permission;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.gang.enums.GangRank;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

public class GangPermissionManager {
    @Expose @Getter private UUID ownerID; // GangID

    public GangPermissionManager(UUID ownerID) {
        this.ownerID = ownerID;
        resetToDefaultPermission();
    }

    @Expose private final HashMap<GangRank, List<GangPermission>> rankPermissionMap = new HashMap<>();
    @Expose private final HashMap<UUID, List<GangPermission>> playerPermissionMap = new HashMap<>();

    public void resetToDefaultPermission() {
        rankPermissionMap.put(GangRank.LEADER, Arrays.asList(GangPermission.ALL));
        rankPermissionMap.put(GangRank.CO_LEADER, Collections.emptyList());
        rankPermissionMap.put(GangRank.MOD, Collections.emptyList());
        rankPermissionMap.put(GangRank.MEMBER, Collections.emptyList());
        rankPermissionMap.put(GangRank.RECRUIT, Collections.emptyList());
    }

    public boolean havePermission(GangRank gangRank, GangPermission permission) {
        return gangRank == GangRank.ADMIN_STATUS || (rankPermissionMap.containsKey(gangRank) && rankPermissionMap.get(gangRank).contains(permission)) ||
                havePermission(gangRank, GangPermission.ALL);
    }

    public boolean havePermission(UUID player, GangPermission permission, boolean withRank) {
        GangRank playerGangRank = LunaZ.getPlayerDataManager().getPlayerdata(player).getGangPlayer().getGangRank();
        return playerGangRank == GangRank.ADMIN_STATUS || (playerPermissionMap.containsKey(player) && playerPermissionMap.get(player).contains(permission)) ||
                (withRank && havePermission(playerGangRank, permission) ||
                playerPermissionMap.get(player).contains(GangPermission.ALL) || havePermission(playerGangRank, GangPermission.ALL));
    }

    public boolean havePermission(Player player, GangPermission permission, boolean withRank) {
        return havePermission(player.getUniqueId(), permission, withRank);
    }

    public boolean havePermission(GangPlayer gangPlayer, GangPermission permission, boolean withRank) {
        return havePermission(gangPlayer.getPlayerID(), permission, withRank);
    }

    public void addPermission(GangRank gangRank, GangPermission permission) {
        rankPermissionMap.putIfAbsent(gangRank, new ArrayList<>());
        rankPermissionMap.get(gangRank).add(permission);
    }

    public void addPermission(UUID player, GangPermission permission) {
        playerPermissionMap.putIfAbsent(player, new ArrayList<>());
        playerPermissionMap.get(player).add(permission);
    }

    public void addPermission(Player player, GangPermission permission) {
        addPermission(player.getUniqueId(), permission);
    }

    public void addPermission(GangPlayer gangPlayer, GangPermission permission) {
        addPermission(gangPlayer.getPlayerID(), permission);
    }

    public void removePermission(UUID target, GangPermission permission) {
        playerPermissionMap.putIfAbsent(target, new ArrayList<>());
        playerPermissionMap.get(target).remove(permission);
    }

    public void removePermission(Player player, GangPermission permission) {
        removePermission(player.getUniqueId(), permission);
    }

    public void removePermission(GangPlayer gangPlayer, GangPermission permission) {
        removePermission(gangPlayer.getPlayerID(), permission);
    }
}
