package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.permissions.PermissionChecker;

import java.util.Optional;

public class CommandShutdownDescriptor extends BaseCommandDescriptor {
    public CommandShutdownDescriptor() {
        super(Executor::new, "Shuts down the bot. You SHOULD be an developer to use this", "<>", "shutdown", "die", "halt", "stop");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendOutput("Shutting down! %s", executionData.getSender().getAsMention());

            try {
                executionData.getBotManager().shutdown();
            } catch (Exception e) {
                e.printStackTrace();
                sendOutput("Error, shutdown failed with an exception. Force exiting!");
                executionData.getBotManager().shutdown(true);
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkBotAdmin(executionData.getSender());
        }
    }
}
