package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.ChannelOutputCommandExecutor;

import java.util.Arrays;

public class CommandGetPrefixDescriptor extends BaseCommandDescriptor {
    public CommandGetPrefixDescriptor() {
        super(Executor::new, "Gets the prefix in the current Guild","<>", Arrays.asList("getprefix", "whatistheprefix"));
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendOutput("**Prefix:** `%s`", executionData.getBotManager().getConfigManager().getGuildConfigManager(executionData.getGuild()).getPrefix());
        }
    }
}
