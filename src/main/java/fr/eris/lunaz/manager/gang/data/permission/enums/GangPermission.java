package fr.eris.lunaz.manager.gang.data.permission.enums;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum GangPermission {
    INVITE_PLAYER("&aInvite", Material.OAK_DOOR, "&7Allow the target to invite other player"),
    UN_INVITE_PLAYER("&cUn Invite", Material.OAK_DOOR, "&7Allow the target to un invite other player"),

    KICK("&cKick", Material.SPRUCE_DOOR, "&7Allow the target to kick gang player"),
    KICK_SILENT("&cKick Silent", Material.SPRUCE_DOOR, "&7Allow the target to kick silently a gang player &8(Need Kick permission)",
            "&8The target will kicked the player out of the gang without any message"),

    FRIENDLY_FIRE("&cFriendly Fire", Material.WOODEN_SWORD, "&7Allow the target to activate or disable friendly fire"),

    RENAME_GANG("&6Rename Gang", Material.WRITABLE_BOOK, "&7Allow the target to rename the gang"),
    RENAME_PLAYER("&6Rename Player", Material.WRITABLE_BOOK, "&7Allow the target to rename any member of the gang"),
    RENAME_SELF("&6Rename Self", Material.WRITABLE_BOOK, "&7Allow the target to rename him self"),
    DISBAND("&4&lDisband", Material.BARRIER, "&7Allow the target to disband the gang"),

    MANAGE_PLAYER("&6Manage Player", Material.BOOK, "&7Allow the target to access to the manage player inventory"),
    MANAGE_PLAYER_RANK("&6Manage Player Rank", Material.PAPER, "&7Allow the target to change rank of a member"),
    MANAGE_PLAYER_PERMISSION("&6Manage Player Permission", Material.PAPER, "&7Allow the target to change permission of a member"),

    EDIT_BANK_ITEM("&6Take item in the bank", Material.ENDER_CHEST, "&7Allow the target to take a put item in the bank"),
    VIEW_ITEM_BANK("&6View item bank", Material.ENDER_EYE, "&7Allow the target to see the item bank"),
    DEPOSIT_BANK_MONEY("&6Deposit Bank money", Material.GOLD_INGOT, "&7Allow the target to deposit money in the gang bank"),
    WITHDRAW_BANK_MONEY("&6Withdraw Bank money", Material.GOLD_BLOCK, "&7Allow the target to withdraw money from the gang bank"),
    DEPOSIT_BANK_LUNA("&6Deposit Bank luna", Material.DIAMOND, "&7Allow the target to deposit luna in the gang bank"),
    WITHDRAW_BANK_LUNA("&6Withdraw Bank luna", Material.DIAMOND_BLOCK, "&7Allow the target to withdraw luna from the gang bank"),
    BANK_ACCESS("&6Withdraw Bank money", Material.CHEST, "&7Allow the target to access the gang bank","&8(Use the /gang bank)"),

    UPGRADE_MENU_ACCESS("&6Access upgrade menu", Material.ENCHANTED_BOOK, "&7Allow the target to access the gang upgrade menu","&8(Use the /gang upgrade)"),
    UPGRADE_BUYING("&6Buy upgrade", Material.EMERALD, "&7Allow the target to buy gang upgrade"),
    ALL("&4&lALL PERMISSION", Material.BARRIER, "&4This permission give all other permission to the target !");

    @Getter private final List<String> description;
    @Getter private final String itemDisplayName;
    @Getter private final Material itemDisplayMaterial;

    GangPermission(String itemDisplayName, Material itemDisplayMaterial, String... description) {
        if(description == null) this.description = Collections.emptyList();
        else this.description = Arrays.asList(description);

        this.itemDisplayName = itemDisplayName;
        this.itemDisplayMaterial = itemDisplayMaterial;
    }
}
