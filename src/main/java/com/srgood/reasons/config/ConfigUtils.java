package com.srgood.reasons.config;


import com.srgood.reasons.commands.CommandDescriptor;
import net.dv8tion.jda.core.entities.Guild;

import java.io.InputStream;
import java.util.Set;

public class ConfigUtils {
    public static void initConfig() throws Exception {
        ConfigPersistenceUtils.initConfig();
    }

    public static void initFromStream(InputStream inputStream) {
        ConfigPersistenceUtils.initConfigFromStream(inputStream);
    }

    public static void deleteGuild(Guild guild) {
        ConfigGuildUtils.deleteGuildConfig(guild);
    }

    public static void ensureGuildInitted(Guild guild) {
        ConfigGuildUtils.ensureGuildInitted(guild);
    }
    
    public static String getGuildPrefix(Guild guild) {
        return ConfigGuildUtils.getGuildPrefix(guild);
    }

    public static Set<String> getGuildRegisteredRoleIDs(Guild guild) {
        return ConfigRoleUtils.getGuildRegisteredRoleIDs(guild);
    }

    public static void deregisterRoleConfig(Guild guild, String roleID) {
        ConfigRoleUtils.deregisterRoleConfig(guild, roleID);
    }

    public static void initCommandConfigIfNotExists(Guild guild, String cmd) {
        ConfigCommandUtils.initCommandConfigIfNotExists(guild, cmd);
    }

    public static boolean isCommandEnabled(Guild guild, CommandDescriptor command) {
        return ConfigCommandUtils.isCommandEnabled(guild, command);
    }

    public static void setCommandEnabled(Guild guild, CommandDescriptor command, boolean enabled) {
        ConfigCommandUtils.setCommandIsEnabled(guild, command, enabled);
    }

    public static void setGuildPrefix(Guild guild, String prefix) {
        ConfigGuildUtils.setGuildPrefix(guild, prefix);
    }

    public static boolean guildPropertyExists(Guild guild, String property) {
        return ConfigGuildUtils.guildPropertyExists(guild, property);
    }

    public static String getGuildProperty(Guild guild, String property) {
        return ConfigGuildUtils.getGuildSimpleProperty(guild, property);
    }

    public static <T> T getGuildSerializedProperty(Guild guild, String property, Class<T> propertyClass) {
        return ConfigGuildUtils.getGuildSerializedProperty(guild, property, propertyClass);
    }

    public static void setGuildProperty(Guild guild, String property, String value) {
        ConfigGuildUtils.setGuildSimpleProperty(guild, property, value);
    }
    public static void setGuildSerializedProperty(Guild guild, String property, Object value) {
        ConfigGuildUtils.setGuildSerializedProperty(guild, property, value);
    }

}
