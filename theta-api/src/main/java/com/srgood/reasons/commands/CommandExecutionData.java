package com.srgood.reasons.commands;

import com.srgood.reasons.BotManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.List;

public interface CommandExecutionData {
    Message getMessage();

    String getRawData();

    String getRawArguments();

    List<String> getParsedArguments();

    MessageChannel getChannel();

    Guild getGuild();

    Member getSender();

    BotManager getBotManager();
}
