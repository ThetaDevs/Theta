package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

import java.util.HashSet;

import static com.srgood.reasons.impl.base.BaseConstants.GLOBAL_RANDOM;

public class CommandCoinFlipDescriptor extends BaseCommandDescriptor {
    public CommandCoinFlipDescriptor() {
        super(Executor::new, "Flips a coin", null, new HashSet<String>(){{
            add("chance");
        }}, "coinflip", "flip", "flipcoin", "flipacoin");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            int n = GLOBAL_RANDOM.nextInt(6002);
            if (n < 3000) {
                sendSuccess("Heads");
            } else if (n > 3001) {
                sendSuccess("Tails");
            } else {
                sendSuccess("Side");
            }
        }

        @Override
        protected void checkCallerPermissions() {
            requirePermission("chance");
        }
    }
}
