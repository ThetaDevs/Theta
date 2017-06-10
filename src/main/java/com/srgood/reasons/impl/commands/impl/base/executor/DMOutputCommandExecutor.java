package com.srgood.reasons.impl.commands.impl.base.executor;

import com.srgood.reasons.commands.CommandExecutionData;
import net.dv8tion.jda.core.entities.Message;

public abstract class DMOutputCommandExecutor extends BaseCommandExecutor {
    public DMOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    @Override
    protected void sendOutput(String format, Object... arguments) {
        executionData.getSender()
                     .getUser()
                     .openPrivateChannel()
                     .queue(chan -> chan.sendMessageFormat(format, arguments).queue());
    }

    @Override
    protected void sendOutput(Message message) {
        executionData.getSender().getUser().openPrivateChannel().queue(chan -> chan.sendMessage(message).queue());
    }
}
