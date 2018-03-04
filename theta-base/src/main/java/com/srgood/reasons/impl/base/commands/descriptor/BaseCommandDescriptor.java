package com.srgood.reasons.impl.base.commands.descriptor;

import com.srgood.reasons.commands.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;

import java.awt.*;
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

    protected BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> executorFunction, String help, Argument args, String primaryName, String... names) {
        this(executorFunction, help, args, true, primaryName, names);
    }

    protected BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> executorFunction, String help, Argument args, boolean visible, String primaryName, String... names) {
        List<String> tempNameList = new ArrayList<>(Arrays.asList(names));
        tempNameList.add(primaryName);
        this.nameRegex = tempNameList.stream().map(s -> "(" + s + ")").collect(Collectors.joining("|"));
        this.primaryName = primaryName;
        this.help = new HelpDataImpl(args, help, visible);
        this.executorFunction = executorFunction;
    }

    @Override
    public CommandExecutor getExecutor(CommandExecutionData executionData) {
        if (!checkArgs(executionData)) {
            return invalidSyntaxExecutor(executionData);
        }

        return executorFunction.apply(executionData);
    }

    private CommandExecutor invalidSyntaxExecutor(CommandExecutionData executionData) {
        return () -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.appendDescription("Invalid usage!");
            embedBuilder.addField("Correct Usage", primaryName + " " + help().args().format(), false);
            embedBuilder.setColor(Color.RED);
            executionData.getChannel().sendMessage(new MessageBuilder(embedBuilder).build()).queue();
        };
    }

    private boolean checkArgs(CommandExecutionData executionData) {
        Argument expectedArgument = help.args();
        boolean reachedRepeated = false;
        for (String commandArgument : executionData.getParsedArguments()) {
            if (expectedArgument == null) return true;

            if (expectedArgument.isLegal(commandArgument)) {
                expectedArgument = expectedArgument.next(commandArgument);
            } else {
                return false;
            }

            if (expectedArgument != null && expectedArgument.isRepeated()) reachedRepeated = true;
        }
        if (expectedArgument == null) return true; // End of arguments, acceptable

        if (expectedArgument.isOptional()) return true; // Into optional arguments, acceptable

        if (!expectedArgument.isRepeated()) return false; // Not optional, not in a repeat, we missed some

        return reachedRepeated; // Repeated, not optional, but we reached it, acceptable
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
        private final Argument args;
        private final String description;
        private final boolean visible;

        public HelpDataImpl(Argument args, String description, boolean visible) {
            this.args = args;
            this.description = description;
            this.visible = visible;
        }

        @Override
        public Argument args() {
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
