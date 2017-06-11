package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.permissions.Permission;
import com.srgood.reasons.impl.permissions.PermissionChecker;

import java.util.Optional;

import static com.srgood.reasons.impl.Reference.EIGHT_BALL;
import static com.srgood.reasons.impl.Reference.GLOBAL_RANDOM;

public class Command8BallDescriptor extends BaseCommandDescriptor {
    public Command8BallDescriptor() {
        super(Executor::new, "Tells your fortune", "<>", "8ball", "fortune", "magic", "idiothelper");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        private Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            int numChoices = EIGHT_BALL.length;
            int randIndex = GLOBAL_RANDOM.nextInt(numChoices);
            sendOutput(EIGHT_BALL[randIndex]);
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), Permission.DO_CHANCE_GAME);
        }
    }
}
