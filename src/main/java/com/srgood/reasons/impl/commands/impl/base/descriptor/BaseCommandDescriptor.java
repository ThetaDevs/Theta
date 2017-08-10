package com.srgood.reasons.impl.commands.impl.base.descriptor;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandExecutor;
import com.srgood.reasons.commands.HelpData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseCommandDescriptor implements CommandDescriptor {
    private final String nameRegex;
    private final String primaryName;
    private final HelpData help;
    private final Function<CommandExecutionData, CommandExecutor> executorFunction;

    protected BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> executorFunction, String help, String args, String primaryName, String... names) {
        this(executorFunction, help, args, true, primaryName, names);
    }

    protected BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> executorFunction, String help, String args, boolean visible, String primaryName, String... names) {
        List<String> tempNameList = new ArrayList<>(Arrays.asList(names));
        tempNameList.add(primaryName);
        this.nameRegex = tempNameList.stream().map(s -> "(" + s + ")").collect(Collectors.joining("|"));
        this.primaryName = primaryName;
        this.help = new HelpDataImpl(args, help, visible);
        this.executorFunction = executorFunction;
    }

    @Override
    public CommandExecutor getExecutor(CommandExecutionData executionData) {
        return executorFunction.apply(executionData);
    }

    @Override
    public HelpData help() {
        return help;
    }

    @Override
    public String getNameRegex() {
        return nameRegex;
    }

    @Override
    public String getPrimaryName() {
        return primaryName;
    }

    private static class HelpDataImpl implements HelpData {
        private final String args;
        private final String description;
        private final boolean visible;

        public HelpDataImpl(String args, String description, boolean visible) {
            this.args = args;
            this.description = description;
            this.visible = visible;
        }

        @Override
        public String args() {
            return args;
        }

        @Override
        public String description() {
            return description;
        }

        @Override
        public boolean visible() {
            return visible;
        }
    }
}
