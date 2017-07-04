package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.commands.permissions.Permission;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

import java.util.Optional;

public class CommandSetPrefixDescriptor extends BaseCommandDescriptor {
    public CommandSetPrefixDescriptor() {
        super(Executor::new, "Gets the prefix in the current Guild", "<prefix>", "setprefix");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() > 0) {
                executionData.getBotManager().getConfigManager().getGuildConfigManager(executionData.getGuild()).setPrefix(executionData.getParsedArguments().get(0));
                sendOutput("The prefix has been set.");
            } else {
                sendOutput("Please specify a prefix.");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), Permission.SET_PREFIX);
        }
    }
}
