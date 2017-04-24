package com.srgood.reasons.utils;

import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Arrays;

public class GreetingUtils {
    private static final String PROP_PREFIX = "moderation";

    public static void tryGreeting(Member member) {
        tryBasic(member, "welcome");
    }

    public static void tryGoodbye(Member member) {
        tryBasic(member, "goodbye");
    }

    private static void tryBasic(Member member, String type) {
        String message = getPropertyContent(member.getGuild(), type);

        if (!Arrays.asList(null, "", "OFF").contains(message)) { // Evil wizard hax
            String channelID = getPropertyContent(member.getGuild(), type + "channel");

            if (channelID != null) {
                TextChannel channel = member.getGuild().getTextChannelById(channelID);
                if (channel != null) {
                    channel.sendMessage(formatMessage(message, member)).queue();
                }
            }
        }
    }

    private static String getPropertyContent(Guild guild, String basicName) {
        return ConfigUtils.getGuildProperty(guild, formatPropertyName(basicName));
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
