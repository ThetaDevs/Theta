package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.config.GuildConfigManager;
import com.srgood.reasons.impl.commands.permissions.Permission;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;
import com.srgood.reasons.impl.commands.utils.GreetingUtils;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

import java.util.Optional;

public class CommandSetGoodbyeDescriptor extends BaseCommandDescriptor {
    public CommandSetGoodbyeDescriptor() {
        super(Executor::new, "Sets the goodbye message for the Guild, which will be sent in the current Channel. Set it to OFF to disable. Use @USER to mention the leaving user", "<message>", "setgoodbye");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            String message = executionData.getParsedArguments().get(0);

            GuildConfigManager guildConfigManager = executionData.getBotManager().getConfigManager()
                                                                                       .getGuildConfigManager(executionData.getGuild());

            guildConfigManager.setProperty(GreetingUtils.formatPropertyName("goodbye"), message);
            guildConfigManager.setProperty(GreetingUtils.formatPropertyName("goodbyechannel"),
                    executionData.getChannel().getId());

            if (message.trim().equalsIgnoreCase("OFF")) {
                sendOutput("Goodbye message turned off");
            } else {
                sendOutput("Goodbye message set. Messages will be sent in this channel.", message);
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), Permission.MANAGE_JOIN_LEAVE_MESSAGES);
        }
    }
}
