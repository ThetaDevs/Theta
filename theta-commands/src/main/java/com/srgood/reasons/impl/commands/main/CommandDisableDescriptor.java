package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.Argument;
import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

import java.util.HashSet;

public class CommandDisableDescriptor extends BaseCommandDescriptor {
    public CommandDisableDescriptor() {
        super(Executor::new, "Disables a command in the current Guild", Argument.string("command"),
                new HashSet<String>(){{
                    add("enable");
                }}, "disable");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() > 0) {
                CommandDescriptor mCommand = executionData.getBotManager()
                                                          .getCommandManager()
                                                          .getCommandByName(executionData.getParsedArguments().get(0));
                try {
                    executionData.getBotManager()
                                 .getCommandManager()
                                 .setCommandEnabled(executionData.getGuild(), mCommand, false);
                    sendSuccess("Command disabled.", mCommand.getPrimaryName());
                } catch (IllegalArgumentException e) {
                    sendError("Cannot disable command %s.", mCommand.getPrimaryName());
                }
            } else {
                sendError("Please specify a command to disable.");
            }
        }

        @Override
        protected void checkCallerPermissions() {
            requirePermission("enable");
        }
    }
}
