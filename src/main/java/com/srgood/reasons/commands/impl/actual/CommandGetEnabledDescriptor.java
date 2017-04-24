package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.config.ConfigUtils;

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
                CommandDescriptor command = CommandManager.getCommandDescriptorByName(executionData.getParsedArguments()
                                                                                                   .get(0));
                sendOutput("Command %s is %s.", command.getPrimaryName(), ConfigUtils.isCommandEnabled(executionData.getGuild(), command) ? "enabled" : "disabled");
            } else {
                sendOutput("Please specify a command for which to get the enabled status.");
            }
        }
    }
}
