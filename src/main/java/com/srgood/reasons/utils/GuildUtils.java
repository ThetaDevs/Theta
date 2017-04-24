package com.srgood.reasons.utils;

import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class GuildUtils {
    public static void initGuild(Guild guild) {
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
    private static void checkForRoles(Guild guild) {
        deregisterPhantomRoles(guild);
    }
    private static void deregisterPhantomRoles(Guild guild) {
        ConfigUtils.getGuildRegisteredRoleIDs(guild).stream().filter(s -> guild.getRoleById(s) == null).forEach(id -> ConfigUtils.deregisterRoleConfig(guild, id));
    }
}
