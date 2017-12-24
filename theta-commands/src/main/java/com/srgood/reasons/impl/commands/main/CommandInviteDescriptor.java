package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import net.dv8tion.jda.core.Permission;


public class CommandInviteDescriptor extends BaseCommandDescriptor {
    public CommandInviteDescriptor() {
        super(Executor::new, "Prints the authlink for the bot", "<>", "invite", "link", "authlink");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendOutput("Theta authorization link: %s", executionData.getJDA()
                                                                    .asBot()
                                                                    .getInviteUrl(Permission.ADMINISTRATOR));
        }
    }
}
