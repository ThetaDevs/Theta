package com.srgood.reasons.impl.commands.utils;

import com.srgood.reasons.config.BotConfigManager;
import com.srgood.reasons.impl.commands.permissions.GuildPermissionSet;
import net.dv8tion.jda.core.entities.Guild;

import java.util.*;
import java.util.stream.Collectors;

public class GuildDataManager {
    private static final Map<String, GuildPermissionSet> guildPermissionSetMap = new HashMap<>();
    private static final Map<String, List<String>> guildCensorListMap = new HashMap<>();
    private static final Map<String, List<String>> guildBlacklistMap = new HashMap<>();

    public static GuildPermissionSet getGuildPermissionSet(BotConfigManager botConfigManager, Guild guild) {
        if (!guildPermissionSetMap.containsKey(guild.getId())) {
            // No guild data found, need to load from XML
            loadNewGuild(botConfigManager, guild);
        }

        return guildPermissionSetMap.get(guild.getId());
    }

    public static void setGuildPermissionSet(BotConfigManager botConfigManager, Guild guild, GuildPermissionSet permissionSet) {
        guildPermissionSetMap.put(guild.getId(), permissionSet);
        botConfigManager.getGuildConfigManager(guild).setSerializedProperty("roleData", permissionSet);
    }

    public static List<String> getGuildCensorList(BotConfigManager botConfigManager, Guild guild) {
        if (!guildCensorListMap.containsKey(guild.getId())) {
            // No guild data found, need to load from XML
            loadNewGuild(botConfigManager, guild);
        }

        return guildCensorListMap.get(guild.getId());
    }

    public static void setGuildCensorList(BotConfigManager botConfigManager, Guild guild, List<String> censorList) {
        List<String> patchedCensorList = censorList.stream()
                                                   .distinct()
                                                   .sorted()
                                                   .collect(Collectors.toCollection(ArrayList::new));
        guildCensorListMap.put(guild.getId(), patchedCensorList);
        botConfigManager.getGuildConfigManager(guild).setSerializedProperty("moderation/censorList", patchedCensorList);
    }

    public static List<String> getGuildBlacklist(BotConfigManager botConfigManager, Guild guild) {
        if (!guildBlacklistMap.containsKey(guild.getId())) {
            // No guild data found, need to load from XML
            loadNewGuild(botConfigManager, guild);
        }

        return guildBlacklistMap.get(guild.getId());
    }

    public static void setGuildBlacklist(BotConfigManager botConfigManager, Guild guild, List<String> blacklist) {
        guildBlacklistMap.put(guild.getId(), blacklist);
        botConfigManager.getGuildConfigManager(guild).setSerializedProperty("moderation/blacklist", blacklist);
    }

    private static void loadNewGuild(BotConfigManager botConfigManager, Guild guild) {
        GuildPermissionSet permissionSet = botConfigManager.getGuildConfigManager(guild)
                                                           .getSerializedProperty("roleData", new GuildPermissionSet(guild), Collections
                                                         .singletonMap("com.srgood.reasons.permissions", "com.srgood.reasons.impl.permissions"));
        permissionSet.clean(guild.getJDA());
        guildPermissionSetMap.put(guild.getId(), permissionSet);

        List<String> censorList = botConfigManager.getGuildConfigManager(guild)
                                                  .getSerializedProperty("moderation/censorList", new ArrayList<>());
        guildCensorListMap.put(guild.getId(), censorList);

        List<String> blacklist = botConfigManager.getGuildConfigManager(guild)
                                                 .getSerializedProperty("moderation/blacklist", new ArrayList<>());
        guildBlacklistMap.put(guild.getId(), blacklist);
    }
}
