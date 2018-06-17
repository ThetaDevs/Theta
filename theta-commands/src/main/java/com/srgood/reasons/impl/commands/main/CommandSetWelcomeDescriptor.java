package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.Argument;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.config.GuildConfigManager;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.utils.GreetingUtils;

import java.util.HashSet;

public class CommandSetWelcomeDescriptor extends BaseCommandDescriptor {
    public CommandSetWelcomeDescriptor() {
        super(Executor::new, "Sets the welcome message for the Guild, which will be sent in the current Channel. Set it to OFF to disable. Use @USER to mention the leaving user.", Argument
                .string("message"),
                new HashSet<String>(){{
                    add("managejoinleave");
                }}, "setwelcome");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            String message = executionData.getParsedArguments().get(0);

            GuildConfigManager guildConfigManager = executionData.getBotManager()
                                                                 .getConfigManager()
                                                                 .getGuildConfigManager(executionData.getGuild());
            guildConfigManager.setProperty(GreetingUtils.formatPropertyName("welcome"), message);
            guildConfigManager.setProperty(GreetingUtils.formatPropertyName("welcomechannel"), executionData.getChannel()
                                                                                                            .getId());

            if (message.trim().equalsIgnoreCase("OFF")) {
                sendSuccess("Welcome message turned off");
            } else {
                sendSuccess("Welcome message set. Messages will be sent in this channel.", message);
            }
        }

        @Override
        protected void checkCallerPermissions() {
            requirePermission("managejoinleave");
        }
    }
}
