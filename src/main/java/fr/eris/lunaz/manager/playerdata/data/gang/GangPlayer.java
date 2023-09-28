package fr.eris.lunaz.manager.playerdata.data.gang;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.gang.enums.GangChatType;
import fr.eris.lunaz.manager.gang.enums.GangRank;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GangPlayer {

    @Expose @Getter private GangRank gangRank = GangRank.NO_GANG;
    @Expose @Setter @Getter private UUID gangID;
    @Expose @Getter private final UUID playerID;
    @Expose @Setter @Getter private String gangPlayerDisplayName = "";
    @Expose @Setter @Getter private GangChatType currentChat = GangChatType.GLOBAL;

    public GangPlayer(UUID playerID) {
        this.playerID = playerID;
    }

    public boolean havePermission(GangPermission permission, boolean withRank) {
        return LunaZ.getGangManager().getGang(gangID).getPermissionManager().havePermission(this, permission, withRank);
    }

    public void setGangRank(GangRank rank) {
        if(rank != GangRank.NO_GANG) {
            Gang playerGang = LunaZ.getGangManager().getGang(gangID);
            playerGang.removePlayerFromList(playerID, this.gangRank);
            playerGang.addPlayerToList(playerID, rank);
        }
        this.gangRank = rank;
    }

    public void setNextChatType() {
        List<GangChatType> chatTypeList = Arrays.asList(GangChatType.values());
        if(chatTypeList.indexOf(currentChat) == chatTypeList.size() - 1) currentChat = chatTypeList.get(0);
        else currentChat = chatTypeList.get(chatTypeList.indexOf(currentChat) + 1);
    }
}
