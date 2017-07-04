package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.commands.permissions.Permission;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

import java.util.Optional;

public class CommandDisableDescriptor extends BaseCommandDescriptor {
    public CommandDisableDescriptor() {
        super(Executor::new, "Disables a command in the current Guild", "<command>","disable");
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
                    executionData.getBotManager().getCommandManager().setCommandEnabled(executionData.getGuild(), mCommand, false);
                    sendOutput("Command disabled.", mCommand.getPrimaryName());
                } catch (IllegalArgumentException e) {
                    sendOutput("Cannot disable command %s.", mCommand.getPrimaryName());
                }
            } else {
                sendOutput("Please specify a command to disable.");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), Permission.SET_COMMAND_ENABLED);
        }
    }
}
