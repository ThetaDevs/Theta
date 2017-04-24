package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.permissions.Permission;
import com.srgood.reasons.permissions.PermissionChecker;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static com.srgood.reasons.Reference.EIGHT_BALL;

public class Command8BallDescriptor extends BaseCommandDescriptor {
    public Command8BallDescriptor() {
        super(Executor::new, "Tells your fortune", "<>", Arrays.asList("8ball", "fortune", "magic", "idiothelper"));
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        private Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            Random random = new Random();
            int numChoices = EIGHT_BALL.length;
            int randIndex = random.nextInt(numChoices);
            sendOutput(EIGHT_BALL[randIndex]);
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getSender(), Permission.DO_CHANCE_GAME);
        }
    }
}
