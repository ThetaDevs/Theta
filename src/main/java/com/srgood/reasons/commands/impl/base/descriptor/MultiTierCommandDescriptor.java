package com.srgood.reasons.commands.impl.base.descriptor;

import com.srgood.reasons.commands.CommandExecutor;
import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.executor.EmptyCommandExecutor;

import java.util.HashSet;
import java.util.Set;

import java.util.*;
import java.util.function.Function;

public abstract class MultiTierCommandDescriptor extends BaseCommandDescriptor {

    private final Set<CommandDescriptor> subCommands;

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, String help, String args, List<String> names) {
        this(subCommands, executionData -> EmptyCommandExecutor.INSTANCE, help, args, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, Function<CommandExecutionData, CommandExecutor> defaultExecutorFunction, String help, String args, List<String> names) {
        super(generateDataToExecutorFunction(subCommands, defaultExecutorFunction), help, args, names);
        this.subCommands = new HashSet<>(subCommands);
    }

    private static Function<CommandExecutionData, CommandExecutor> generateDataToExecutorFunction(Collection<CommandDescriptor> subCommandDescriptors,
                                                                                                  Function<CommandExecutionData, CommandExecutor> defaultExecutorFunction) {
        return executionData -> {
            if (executionData.getParsedArguments().isEmpty()) {
                return defaultExecutorFunction.apply(executionData);
            }

            String targetName = executionData.getParsedArguments().get(0);

            for (CommandDescriptor subDescriptor : subCommandDescriptors) {
                if (subDescriptor.getNames().stream().anyMatch(targetName::equals)) {
                    return subDescriptor.getExecutor(patchExecutionDataForSubCommand(executionData));
                }
            }

            return defaultExecutorFunction.apply(executionData);
        };
    }

    private static CommandExecutionData patchExecutionDataForSubCommand(CommandExecutionData data) {
        List<String> oldParsedArguments = data.getParsedArguments();
        List<String> newParsedArguments = oldParsedArguments.subList(1, oldParsedArguments.size());

        return new CommandExecutionData(data.getRawData(), data.getRawArguments(), newParsedArguments, data.getChannel(),
                data.getGuild(), data.getSender());
    }

    @Override
    public boolean hasSubCommands() {
        return true;
    }

    @Override
    public Set<CommandDescriptor> getSubCommands() {
        return Collections.unmodifiableSet(subCommands);
    }
}
