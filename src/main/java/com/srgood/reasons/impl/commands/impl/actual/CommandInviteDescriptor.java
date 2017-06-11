package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.Reference;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.ChannelOutputCommandExecutor;

public class CommandInviteDescriptor extends BaseCommandDescriptor {
    public CommandInviteDescriptor() {
        super(Executor::new, "Prints the authlink for the bot","<>", "invite", "link", "authlink");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendOutput("Theta authorization link: %s", Reference.INVITE_LINK);
        }
    }
}
