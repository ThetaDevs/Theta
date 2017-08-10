package com.srgood.reasons.impl.commands.impl.base.descriptor;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandExecutor;
import com.srgood.reasons.impl.commands.CommandExecutionDataImpl;
import com.srgood.reasons.impl.commands.impl.base.executor.EmptyCommandExecutor;

import java.util.*;
import java.util.function.Function;

public abstract class MultiTierCommandDescriptor extends BaseCommandDescriptor {

    private final Set<CommandDescriptor> subCommands;

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, String help, String args, String primaryName, String... names) {
        this(subCommands, help, args, true, primaryName, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, String help, String args, boolean visible, String primaryName, String... names) {
        this(subCommands, executionData -> EmptyCommandExecutor.INSTANCE, help, args, visible, primaryName, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, Function<CommandExecutionData, CommandExecutor> defaultExecutorFunction, String help, String args, String primaryName, String... names) {
        this(subCommands, defaultExecutorFunction, help, args, true, primaryName, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, Function<CommandExecutionData, CommandExecutor> defaultExecutorFunction, String help, String args, boolean visible, String primaryName, String... names) {
        super(generateDataToExecutorFunction(subCommands, defaultExecutorFunction), help, args, visible, primaryName, names);
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
                if (targetName.matches(subDescriptor.getNameRegex())) {
                    return subDescriptor.getExecutor(patchExecutionDataForSubCommand(executionData));
                }
            }

            return defaultExecutorFunction.apply(executionData);
        };
    }

    private static CommandExecutionData patchExecutionDataForSubCommand(CommandExecutionData data) {
        List<String> oldParsedArguments = data.getParsedArguments();
        List<String> newParsedArguments = oldParsedArguments.subList(1, oldParsedArguments.size());

        return new CommandExecutionDataImpl(data.getMessage(), data.getRawData(), data.getRawArguments(), newParsedArguments, data.getChannel(),
                data.getGuild(), data.getSender(), data.getBotManager());
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
