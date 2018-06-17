package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

import java.util.HashSet;

import static com.srgood.reasons.impl.base.BaseConstants.GLOBAL_RANDOM;
import static com.srgood.reasons.impl.commands.CommandConstants.EIGHT_BALL;

public class Command8BallDescriptor extends BaseCommandDescriptor {
    public Command8BallDescriptor() {
        super(Executor::new, "Tells your fortune", null, new HashSet<String>(){{
            add("chance");
        }}, "8ball", "fortune", "magic", "idiothelper");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        private Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            int numChoices = EIGHT_BALL.length;
            int randIndex = GLOBAL_RANDOM.nextInt(numChoices);
            sendSuccess(EIGHT_BALL[randIndex]);
        }

        @Override
        protected void checkCallerPermissions() {
            requirePermission("chance");
        }
    }
}
