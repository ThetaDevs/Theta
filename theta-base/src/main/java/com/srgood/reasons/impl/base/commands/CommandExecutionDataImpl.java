package com.srgood.reasons.impl.base.commands;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandExecutionData;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

import java.util.Collections;
import java.util.List;

import static com.srgood.reasons.impl.base.commands.CommandUtils.generatePossiblePrefixesForGuild;

public class CommandExecutionDataImpl implements CommandExecutionData {
    private final Message message;
    private final String rawArgs;
    private final List<String> parsedArguments;
    private final BotManager botManager;

    public CommandExecutionDataImpl(Message message, BotManager botManager) {
        this(
                message,
                CommandUtils.parseCommandMessageArguments(
                        message.getContentRaw(),
                        generatePossiblePrefixesForGuild(
                                botManager
                                        .getConfigManager()
                                        .getGuildConfigManager(
                                                message.getGuild()),
                                message.getGuild())),
                botManager
        );
    }

    // Needed for subcommand parsing
    public CommandExecutionDataImpl(Message message, List<String> forcedArguments, BotManager botManager) {
        this(
                message,
                CommandUtils.getCommandMessageArgsSection(
                        message.getContentRaw(),
                        generatePossiblePrefixesForGuild(
                                botManager
                                        .getConfigManager()
                                        .getGuildConfigManager(
                                                message.getGuild()),
                                message.getGuild())),
                forcedArguments,
                botManager
        );
    }

    public CommandExecutionDataImpl(Message message, String rawArgs, List<String> parsedArguments, BotManager botManager) {
        this.message = message;
        this.rawArgs = rawArgs;
        this.parsedArguments = parsedArguments;
        this.botManager = botManager;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String getRawData() {
        return message.getContentRaw();
    }

    @Override
    public String getRawArguments() {
        return rawArgs;
    }

    @Override
    public List<String> getParsedArguments() {
        return Collections.unmodifiableList(parsedArguments);
    }

    @Override
    public MessageChannel getChannel() {
        return message.getChannel();
    }

    @Override
    public Guild getGuild() {
        return message.getGuild();
    }

    @Override
    public Member getSender() {
        return message.getMember();
    }

    @Override
    public BotManager getBotManager() {
        return botManager;
    }

    @Override
    public JDA getJDA() {
        return message.getJDA();
    }
}
