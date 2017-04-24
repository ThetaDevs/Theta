package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.permissions.Permission;
import com.srgood.reasons.permissions.PermissionChecker;

import java.util.Optional;

public class CommandEnableDescriptor extends BaseCommandDescriptor {
    public CommandEnableDescriptor() {
        super(Executor::new, "Enables a command in the current Guild", "<command>","enable");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() > 0) {
                CommandDescriptor mCommand = CommandManager.getCommandDescriptorByName(executionData.getParsedArguments()
                                                                                                    .get(0));
                try {
                    CommandManager.setCommandEnabled(executionData.getGuild(), mCommand, true);
                    sendOutput("Command %s enabled.", mCommand.getPrimaryName());
                } catch (IllegalArgumentException e) {
                    sendOutput("Cannot enable command %s.", mCommand.getPrimaryName());
                }
            } else {
                sendOutput("Please specify a command to enable");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getSender(), Permission.SET_COMMAND_ENABLED);
        }
    }
}
