package com.srgood.reasons.commands.upcoming;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.Collections;
import java.util.List;

public class CommandExecutionData {
    private final String rawData;
    private final String rawArgs;
    private final List<String> parsedArguments;
    private final MessageChannel channel;
    private final Guild guild;
    private final User sender;

    public CommandExecutionData(Message message) {
        this(   message.getRawContent(),
                CommandUtils.getCommandMessageArgsSection(message),
                CommandUtils.parseCommandMessageArguments(message),
                message.getChannel(),
                message.getGuild(),
                message.getAuthor());
    }

    public CommandExecutionData(String rawData, String rawArgs, List<String> parsedArguments, MessageChannel channel, Guild guild, User sender) {
        this.rawData = rawData;
        this.rawArgs = rawArgs;
        this.parsedArguments = parsedArguments;
        this.channel = channel;
        this.guild = guild;
        this.sender = sender;
    }


    public String getRawData() {
        return rawData;
    }

    public String getRawArguments() {
        return rawArgs;
    }

    public List<String> getParsedArguments() {
        return Collections.unmodifiableList(parsedArguments);
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }

    public User getSender() {
        return sender;
    }
}
