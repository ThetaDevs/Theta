package com.srgood.reasons.impl.base.commands.executor;

import com.srgood.reasons.commands.CommandExecutionData;
import net.dv8tion.jda.core.entities.Message;

public abstract class ChannelOutputCommandExecutor extends BaseCommandExecutor {
    public ChannelOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    @Override
    protected void sendOutput(String format, Object... arguments) {
        executionData.getChannel().sendMessageFormat(format, arguments).queue();
    }

    @Override
    protected void sendOutput(Message message) {
        executionData.getChannel().sendMessage(message).queue();
    }
}
