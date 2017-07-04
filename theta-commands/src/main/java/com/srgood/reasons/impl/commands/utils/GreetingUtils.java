package com.srgood.reasons.impl.commands.utils;

import com.srgood.reasons.config.GuildConfigManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Arrays;

public class GreetingUtils {
    private static final String PROP_PREFIX = "moderation";

    public static void tryGreeting(Member member, GuildConfigManager guildConfigManager) {
        tryBasic(member, guildConfigManager, "welcome");
    }

    public static void tryGoodbye(Member member, GuildConfigManager guildConfigManager) {
        tryBasic(member, guildConfigManager, "goodbye");
    }

    private static void tryBasic(Member member, GuildConfigManager guildConfigManager, String type) {
        String message = getPropertyContent(guildConfigManager, type);

        if (!Arrays.asList(null, "", "OFF").contains(message)) { // Evil wizard hax
            String channelID = getPropertyContent(guildConfigManager, type + "channel");

            if (channelID != null) {
                TextChannel channel = member.getGuild().getTextChannelById(channelID);
                if (channel != null) {
                    channel.sendMessage(formatMessage(message, member)).queue();
                }
            }
        }
    }

    private static String getPropertyContent(GuildConfigManager guildConfigManager, String basicName) {
        return guildConfigManager.getProperty(formatPropertyName(basicName));
    }

    private static String formatMessage(String message, Member member) {
        return message.replace("@USER", member.getAsMention())
                      .replace("@SERVER", member.getGuild().getName())
                      .replace("@GUILD", member.getGuild().getName());
    }

    public static String formatPropertyName(String basicName) {
        return String.format("%s/%s", PROP_PREFIX, basicName);
    }
}
