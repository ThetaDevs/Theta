package com.srgood.reasons.utils;

import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CensorUtils {

    public static List<String> getGuildCensorList(Guild guild) {
        return ConfigUtils.guildPropertyExists(guild, "moderation/censorlist") ? ConfigUtils.getGuildSerializedProperty(guild, "moderation/censorlist", List.class) : Collections.emptyList();
    }

    public static void setGuildCensorList(Guild guild, Collection<String> newCensorList) {
        List<String> patchedList = newCensorList.stream()
                                                .sorted()
                                                .distinct()
                                                .collect(Collectors.toList());
        ConfigUtils.setGuildSerializedProperty(guild, "moderation/censorlist", patchedList);
    }

    public static void checkCensor(Message message) {
        if (!PermissionUtil.checkPermission(
                ((Channel) message.getChannel()),
                message.getGuild().getSelfMember(),
                Permission.MESSAGE_MANAGE)) {
            return;
        }

        if (message.getContent().startsWith(ConfigUtils.getGuildPrefix(message.getGuild()) + "censor")) return;

        if (ConfigUtils.guildPropertyExists(message.getGuild(), "moderation/censorlist")) {
            Collection<String> censorList = CensorUtils.getGuildCensorList(message.getGuild());
            for (String aCensorList : censorList) {
                Pattern p = Pattern.compile("\\b" + aCensorList + "\\b");
                Matcher m = p.matcher(message.getContent().toLowerCase());
                if (m.find()) {
                    message.getAuthor()
                           .openPrivateChannel()
                           .queue(channel -> channel.sendMessage(String.format("The word `%s` is not allowed on this server.", aCensorList))
                                                    .queue());
                    // noinspection unchecked [For IntelliJ]
                    message.delete().queue(RestAction.DEFAULT_SUCCESS, RestAction.DEFAULT_SUCCESS);
                    break;
                }
            }
        }
    }
}
