package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class CommandListenDescriptor extends BaseCommandDescriptor {
    public CommandListenDescriptor() {
        super(Executor::new, "Enables or disables command listening in a channel", "<enable | disable> <channel>", "listen");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            List<TextChannel> channelList = executionData.getGuild().getTextChannelsByName(executionData.getParsedArguments().get(1), false);
            if (channelList.size() == 0) {
                sendOutput("No channel by the name `%s` was found.", executionData.getParsedArguments().get(1));
                return;
            }
            Channel channel = channelList.get(0);
            boolean enabledState;
            switch (executionData.getParsedArguments().get(0).toLowerCase()) {
                case "enable":
                    enabledState = true;
                    break;
                case "disable":
                    enabledState = false;
                    break;
                default:
                    sendOutput("Illegal enabled state. Use `enable` or `disable`");
                    return;
            }
            if (!executionData.getSender().hasPermission(channel, Permission.MANAGE_CHANNEL)) {
                sendOutput("You have insufficient permissions.");
                return;
            }
            executionData.getBotManager()
                         .getConfigManager()
                         .getGuildConfigManager(executionData.getGuild())
                         .getChannelConfigManager(channel)
                         .setProperty("listening", "" + enabledState);

            sendOutput("Listening %s for channel `%s`", enabledState == true ? "enabled" : "disabled", executionData.getParsedArguments().get(1));
        }
    }
}
