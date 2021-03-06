package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

public class CommandGetPrefixDescriptor extends BaseCommandDescriptor {
    public CommandGetPrefixDescriptor() {
        super(Executor::new, "Gets the prefix in the current Guild", null, "getprefix", "whatistheprefix");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendSuccess("**Prefix:** `%s`", executionData.getBotManager()
                                                        .getConfigManager()
                                                        .getGuildConfigManager(executionData.getGuild())
                                                        .getPrefix());
        }
    }
}
