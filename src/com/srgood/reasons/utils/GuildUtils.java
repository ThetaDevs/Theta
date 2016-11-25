package com.srgood.reasons.utils;

import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;

public class GuildUtils {
    public static void initGuild(net.dv8tion.jda.entities.Guild guild) {
        ConfigUtils.ensureGuildInitted(guild);

        try {
            for (PermissionLevels permission : PermissionLevels.values()) {
                com.srgood.reasons.utils.PermissionUtils.createRole(permission, guild, true);
            }
        } catch (net.dv8tion.jda.exceptions.PermissionException e3) {
            net.dv8tion.jda.utils.SimpleLog.getLog("Reasons").warn("Could not create custom role! Possible permissions problem?");
        }

        ConfigUtils.ensureGuildInitted(guild);
    }

    public static void deleteGuild(Guild guild) {
        ConfigUtils.getGuildRegisteredRoleIDs(guild).forEach(roleID -> {
            ConfigUtils.deregisterRoleConfig(guild,roleID);
            Role role = guild.getRoleById(roleID);
            if (role != null) {
                role.getManager().delete();
            }
        } );
        ConfigUtils.deleteGuild(guild);
    }

    public static void doPreMessageGuildCheck(Guild guild) {
        ConfigUtils.ensureGuildInitted(guild);
        checkForRoles(guild);
    }
    private static void checkForRoles(Guild guild) {
        deregisterPhantomRoles(guild);
        createMissingRoles(guild);
    }
    private static void deregisterPhantomRoles(Guild guild) {
        ConfigUtils.getGuildRegisteredRoleIDs(guild).stream().filter(s -> guild.getRoleById(s) == null).forEach(id -> ConfigUtils.deregisterRoleConfig(guild, id));
    }
    private static void createMissingRoles(Guild guild) {
        for (PermissionLevels permLevel : PermissionLevels.values()) {
            if (!permLevel.isVisible()) {
                continue;
            }
            if (!ConfigUtils.guildHasRoleForPermission(guild, permLevel)) {
                PermissionUtils.createRole(permLevel, guild, true);
            }
        }
    }
}
