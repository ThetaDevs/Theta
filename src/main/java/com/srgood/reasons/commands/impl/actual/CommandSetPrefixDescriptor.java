package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.permissions.Permission;
import com.srgood.reasons.permissions.PermissionChecker;

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
                ConfigUtils.setGuildPrefix(executionData.getGuild(), executionData.getParsedArguments().get(0));
                sendOutput("The prefix has been set.");
            } else {
                sendOutput("Please specify a prefix.");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getSender(), Permission.SET_PREFIX);
        }
    }
}
