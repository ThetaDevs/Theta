package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.BaseConstants;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

public class CommandShutdownDescriptor extends BaseCommandDescriptor {
    public CommandShutdownDescriptor() {
        super(Executor::new, "Shuts down the bot. You SHOULD be an developer to use this", null, false, "shutdown", "die", "halt", "stop");
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
        protected void checkCallerPermissions() {
            if (!BaseConstants.BOT_DEVELOPERS.contains(executionData.getSender().getUser().getId())) {
                 dontExecute("You must be a bot developer to use this.");
            }
        }
    }
}
