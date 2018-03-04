package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;

import java.util.Optional;

public class CommandShutdownDescriptor extends BaseCommandDescriptor {
    public CommandShutdownDescriptor() {
        super(Executor::new, "Shuts down the bot. You SHOULD be an developer to use this", "<>", false, "shutdown", "die", "halt", "stop");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendSuccess("Shutting down! %s", executionData.getSender().getAsMention());

            try {
                executionData.getBotManager().close();
            } catch (Exception e) {
                e.printStackTrace();
                sendError("Error, shutdown failed with an exception. The bot may be in an inconsistent state!");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkBotAdmin(executionData.getSender());
        }
    }
}
