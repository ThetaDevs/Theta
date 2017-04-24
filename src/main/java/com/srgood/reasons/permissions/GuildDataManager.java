package com.srgood.reasons.permissions;

import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class GuildDataManager {
    private static final Map<String, GuildPermissionSet> guildPermissionSetMap = new HashMap<>();

    public static GuildPermissionSet getGuildPermissionSet(Guild guild) {
        if (guildPermissionSetMap.containsKey(guild.getId())) {
            return guildPermissionSetMap.get(guild.getId());
        }

        // No guild data found, need to load from XML
        return loadNewGuild(guild);
    }

    private static GuildPermissionSet loadNewGuild(Guild guild) {
        ConfigUtils.ensureGuildInitted(guild);

        GuildPermissionSet ret;

        if (ConfigUtils.guildPropertyExists(guild, "roleData")) {
            ret =  ConfigUtils.getGuildSerializedProperty(guild, "roleData", GuildPermissionSet.class);
        } else {
            GuildPermissionSet newPermissionSet = new GuildPermissionSet(guild);
            ConfigUtils.setGuildSerializedProperty(guild, "roleData", newPermissionSet);
            ret = newPermissionSet;
        }

        ret.clean(guild.getJDA());

        guildPermissionSetMap.put(guild.getId(), ret);
        return ret;
    }

    public static void setGuildPermissionSet(Guild guild, GuildPermissionSet permissionSet) {
        guildPermissionSetMap.put(guild.getId(), permissionSet);
        ConfigUtils.setGuildSerializedProperty(guild, "roleData", permissionSet);
    }
}
