package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.config.ConfigPersistenceUtils;
import com.srgood.reasons.permissions.PermissionChecker;

public class CommandShutdownDescriptor extends BaseCommandDescriptor {
    public CommandShutdownDescriptor() {
        super(Executor::new, "Shuts down the bot. You SHOULD be an developer to use this", "<>","shutdown");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendOutput("Shutting down! %s", executionData.getSender().getAsMention());

            try {
                ConfigPersistenceUtils.writeXML();
                ReasonsMain.getJda().shutdown();
            } catch (Exception e) {
                e.printStackTrace();
                sendOutput("Error, shutdown failed with an exception. Force exiting!");
                System.exit(-1);
            }
        }

        @Override
        protected void checkCallerPermissions() {
            PermissionChecker.checkBotAdmin(executionData.getSender());
        }
    }
}
