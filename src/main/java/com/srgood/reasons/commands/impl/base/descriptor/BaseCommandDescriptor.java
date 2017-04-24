package com.srgood.reasons.commands.impl.base.descriptor;

import com.srgood.reasons.commands.CommandExecutor;
import com.srgood.reasons.commands.HelpData;
import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BaseCommandDescriptor implements CommandDescriptor {
    private final List<String> names;
    private final HelpData help;
    private final Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction;

    protected BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction, String help, String args, String name) {
        this.names = Collections.singletonList(name);
        this.help = new HelpDataImpl(args, help);
        this.dataToExecutorFunction = dataToExecutorFunction;
    }

    protected BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction, String help, String args, List<String> names) {
        this.names = new ArrayList<>(names);
        this.help = new HelpDataImpl(args, help);
        this.dataToExecutorFunction = dataToExecutorFunction;
    }

    @Override
    public CommandExecutor getExecutor(CommandExecutionData executionData) {
        return dataToExecutorFunction.apply(executionData);
    }

    @Override
    public HelpData help() {
        return help;
    }

    @Override
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    private static class HelpDataImpl implements HelpData {
        private final String args;
        private final String description;

        public HelpDataImpl(String args, String description) {

            this.args = args;
            this.description = description;
        }

        @Override
        public String args() {
            return args;
        }

        @Override
        public String description() {
            return description;
        }
    }
}
