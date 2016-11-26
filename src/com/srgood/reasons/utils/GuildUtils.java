package com.srgood.reasons.utils;


import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.Permissions.PermissionUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.SimpleLog;

public class GuildUtils {
    public static void initGuild(Guild guild) throws RateLimitedException {
        ConfigUtils.ensureGuildInitted(guild);

        try {
            for (PermissionLevels permission : PermissionLevels.values()) {
                PermissionUtils.createRole(permission, guild, true);
            }
        } catch (PermissionException e3) {
            SimpleLog.getLog("Reasons").warn("Could not create custom role! Possible permissions problem?");
        }

        ConfigUtils.ensureGuildInitted(guild);
    }

    public static void deleteGuild(Guild guild) {
        ConfigUtils.getGuildRegisteredRoleIDs(guild).forEach(roleID -> {
            ConfigUtils.deregisterRoleConfig(guild,roleID);
            Role role = guild.getRoleById(roleID);
            if (role != null) {
                role.delete().queue();
            }
        } );
        ConfigUtils.deleteGuild(guild);
    }

    public static void doPreMessageGuildCheck(Guild guild) throws RateLimitedException {
        ConfigUtils.ensureGuildInitted(guild);
        checkForRoles(guild);
    }
    private static void checkForRoles(Guild guild) throws RateLimitedException {
        deregisterPhantomRoles(guild);
        createMissingRoles(guild);
    }
    private static void deregisterPhantomRoles(Guild guild) {
        ConfigUtils.getGuildRegisteredRoleIDs(guild).stream().filter(s -> guild.getRoleById(s) == null).forEach(id -> ConfigUtils.deregisterRoleConfig(guild, id));
    }
    private static void createMissingRoles(Guild guild) throws RateLimitedException {
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