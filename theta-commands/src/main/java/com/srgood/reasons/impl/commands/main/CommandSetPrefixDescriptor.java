package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.Argument;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

import java.util.HashSet;

public class CommandSetPrefixDescriptor extends BaseCommandDescriptor {
    public CommandSetPrefixDescriptor() {
        super(Executor::new, "Gets the prefix in the current Guild", Argument.string("prefix"), new HashSet<String>() {{
            add("setprefix");
        }}, "setprefix");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() > 0) {
                executionData.getBotManager()
                             .getConfigManager()
                             .getGuildConfigManager(executionData.getGuild())
                             .setPrefix(executionData.getParsedArguments().get(0));
                sendSuccess("The prefix has been set.");
            } else {
                sendError("Please specify a prefix.");
            }
        }

        @Override
        protected void checkCallerPermissions() {
            requirePermission("setprefix");
        }
    }
}
