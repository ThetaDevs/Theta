package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.config.ConfigUtils;

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
            sendOutput("**Prefix:** `%s`", ConfigUtils.getGuildPrefix(executionData.getGuild()));
        }
    }
}
