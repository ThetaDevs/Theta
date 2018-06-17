package com.srgood.reasons.impl.runner;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.impl.commands.utils.BlacklistUtils;
import net.dv8tion.jda.core.entities.Message;

import java.util.Collections;
import java.util.Objects;
import java.util.function.BiPredicate;

public class MessageReceivedPredicates {
    public static final BiPredicate<Message, BotManager> NOT_BOT_SENDER = (message, unused) -> !message.getAuthor()
                                                                                                       .isBot();
    public static final BiPredicate<Message, BotManager> NOT_BLACKLISTED = (message, botManager) -> !BlacklistUtils.isBlacklisted(botManager, message.getMember(), botManager.getConfigManager()
                                                                .getGuildConfigManager(message.getGuild())
                                                                .getSerializedProperty("blacklist", Collections.emptyList()));
    public static final BiPredicate<Message, BotManager> LISTENING_IN_CHANNEL = (message, botManager) -> Objects.equals(botManager
            .getConfigManager()
            .getGuildConfigManager(message.getGuild())
            .getChannelConfigManager(message.getTextChannel())
            .getProperty("listening", "true"), "true");
}
