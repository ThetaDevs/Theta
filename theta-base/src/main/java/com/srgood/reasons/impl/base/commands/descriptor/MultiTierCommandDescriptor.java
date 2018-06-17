package com.srgood.reasons.impl.base.commands.descriptor;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandExecutor;
import com.srgood.reasons.impl.base.ArgsBuilder;
import com.srgood.reasons.commands.Argument;
import com.srgood.reasons.impl.base.commands.executor.EmptyCommandExecutor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class MultiTierCommandDescriptor extends BaseCommandDescriptor {

    private final Set<CommandDescriptor> subCommands;

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, String help, String primaryName, String... names) {
        this(subCommands, help, true, primaryName, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, String help, boolean visible, String primaryName, String... names) {
        this(subCommands, executionData -> EmptyCommandExecutor.INSTANCE, help, visible, primaryName, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, String help, boolean visible, Set<String> permissions, String primaryName, String... names) {
        this(subCommands, executionData -> EmptyCommandExecutor.INSTANCE, help, visible, permissions, primaryName, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, Function<CommandExecutionData, CommandExecutor> defaultExecutorFunction, String help, String primaryName, String... names) {
        this(subCommands, defaultExecutorFunction, help, true, primaryName, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, Function<CommandExecutionData, CommandExecutor> defaultExecutorFunction, String help, boolean visible, String primaryName, String... names) {
        this(subCommands, defaultExecutorFunction, help, true, allPermissions(subCommands), primaryName, names);
    }

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, Function<CommandExecutionData, CommandExecutor> defaultExecutorFunction, String help, boolean visible, Set<String> permissions, String primaryName, String... names) {
        super(generateDataToExecutorFunction(subCommands, defaultExecutorFunction), help, createArgs(subCommands), visible, permissions, primaryName, names);
        this.subCommands = new HashSet<>(subCommands);
    }

    private static Argument createArgs(Set<CommandDescriptor> subCommands) {
        ArgsBuilder builder = ArgsBuilder.create();
        for (CommandDescriptor descriptor : subCommands) {
            builder.addSubCommand(descriptor.getPrimaryName(), descriptor.help().args());
        }
        return builder.build();
    }

    private static Function<CommandExecutionData, CommandExecutor> generateDataToExecutorFunction(Collection<CommandDescriptor> subCommandDescriptors, Function<CommandExecutionData, CommandExecutor> defaultExecutorFunction) {
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

        return new com.srgood.reasons.impl.base.commands.CommandExecutionDataImpl(data.getMessage(), newParsedArguments, data.getBotManager());
    }

    private static Set<String> allPermissions(Set<CommandDescriptor> subCommands) {
        return subCommands.stream()
                          .map(CommandDescriptor::getDeclaredPermissions)
                          .flatMap(Collection::stream)
                          .collect(Collectors.toSet());
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
