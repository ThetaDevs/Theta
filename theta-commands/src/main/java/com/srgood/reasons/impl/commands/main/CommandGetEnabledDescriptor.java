package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

public class CommandGetEnabledDescriptor extends BaseCommandDescriptor {
    public CommandGetEnabledDescriptor() {
        super(Executor::new, "Gets whether the command is enabled or not in the current Guild", "<command>", "getenabled");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() > 0) {
                CommandDescriptor command = executionData.getBotManager().getCommandManager().getCommandByName(executionData.getParsedArguments()
                                                                                                                                  .get(0));
                sendOutput("Command %s is %s.", command.getPrimaryName(), executionData.getBotManager().getConfigManager()
                                                                                                             .getGuildConfigManager(executionData.getGuild())
                                                                                                             .getCommandConfigManager(command)
                                                                                                             .isEnabled() ? "enabled" : "disabled");
            } else {
                sendOutput("Please specify a command for which to get the enabled status.");
            }
        }
    }
}
