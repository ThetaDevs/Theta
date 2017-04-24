package com.srgood.reasons.commands.impl.base.executor;

import com.srgood.reasons.commands.CommandExecutionData;
import net.dv8tion.jda.core.entities.Message;

public abstract class DMOutputCommandExecutor extends BaseCommandExecutor {
    public DMOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    @Override
    protected void sendOutput(String format, Object... arguments) {
        String formatted = String.format(format, (Object[]) arguments); // Prevent confusing varargs call
        executionData.getSender().getUser().openPrivateChannel().queue(chan -> chan.sendMessage(formatted).queue());
    }

    @Override
    protected void sendOutput(Message message) {
        executionData.getSender().getUser().openPrivateChannel().queue(chan -> chan.sendMessage(message).queue());
    }
}
