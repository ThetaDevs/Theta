package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.permissions.Permission;
import com.srgood.reasons.impl.permissions.PermissionChecker;

import java.util.Optional;
import java.util.Random;

public class CommandCoinFlipDescriptor extends BaseCommandDescriptor {
    public CommandCoinFlipDescriptor() {
        super(Executor::new, "Flips a coin", "<>", "coinflip", "flip", "flipcoin", "flipacoin");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            Random r = new Random();
            int n = r.nextInt(6002);
            if (n < 3000) {
                sendOutput("Heads");
            } else if (n > 3001) {
                sendOutput("Tails");
            } else {
                sendOutput("Side");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), Permission.DO_CHANCE_GAME);
        }
    }
}
