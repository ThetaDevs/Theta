package com.srgood.reasons.config;


import com.srgood.reasons.commands.Command;
import com.srgood.reasons.commands.CommandParser;
import com.srgood.reasons.commands.PermissionLevels;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

import java.io.InputStream;
import java.util.Set;

public class ConfigUtils {
    public static void initConfig() throws Exception {
        ConfigPersistenceUtils.initConfig();
    }

    public static void initFromStream(InputStream inputStream) throws Exception {
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

    public static void registerRoleConfig(Guild guild, Role role, PermissionLevels permLevel) {
        ConfigRoleUtils.registerRoleConfig(guild, role, permLevel);
    }

    public static PermissionLevels roleToPermission(Role role) {
        return ConfigRoleUtils.roleToPermission(role.getGuild(), role);
    }

    public static Set<Role> getGuildRolesFromPermission(Guild guild, PermissionLevels permLevel) {
        return ConfigRoleUtils.getGuildRolesFromPermissionLevel(guild, permLevel);
    }

    public static Set<String> getGuildRegisteredRoleIDs(Guild guild) {
        return ConfigRoleUtils.getGuildRegisteredRoleIDs(guild);
    }

    public static void deregisterRoleConfig(Guild guild, String roleID) {
        ConfigRoleUtils.deregisterRoleConfig(guild, roleID);
    }

    public static boolean guildHasRoleForPermission(Guild guild, PermissionLevels permLevel) {
        return ConfigRoleUtils.guildHasRoleForPermission(guild, permLevel);
    }

    public static void initCommandConfigIfNotExists(CommandParser.CommandContainer cmd) {
        ConfigCommandUtils.initCommandConfigIfNotExists(cmd);
    }

    public static void initCommandConfigIfNotExists(Guild guild, Command cmd) {
        ConfigCommandUtils.initCommandConfigIfNotExists(guild, cmd);
    }

    public static boolean isCommandEnabled(Guild guild, Command command) {
        return ConfigCommandUtils.isCommandEnabled(guild, command);
    }

    public static void setCommandEnabled(Guild guild, Command command, boolean enabled) {
        ConfigCommandUtils.setCommandIsEnabled(guild, command, enabled);
    }

    public static PermissionLevels getCommandPermission(Guild guild, Command command) {
        return ConfigCommandUtils.getCommandPermission(guild, command);
    }

    public static void setGuildPrefix(Guild guild, String prefix) {
        ConfigGuildUtils.setGuildPrefix(guild, prefix);
    }

    public static void setCommandPermission(Guild guild, Command command, PermissionLevels permLevel) {
        ConfigCommandUtils.setCommandPermission(guild, command, permLevel);
    }

    public static String getGuildProperty(Guild guild, String property) {
        return ConfigGuildUtils.getGuildSimpleProperty(guild, property);
    }

    public static void setGuildProperty(Guild guild, String property, String value) {
        ConfigGuildUtils.setGuildSimpleProperty(guild, property, value);
    }

    public static boolean verifyConfig() {
        return ConfigBasicUtils.verifyConfig();
    }
}
