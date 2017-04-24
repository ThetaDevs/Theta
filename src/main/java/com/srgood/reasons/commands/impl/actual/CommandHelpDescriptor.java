package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.DMOutputCommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandHelpDescriptor extends BaseCommandDescriptor {
    public CommandHelpDescriptor() {
        super(Executor::new, "Provides information about all commands", "<>", "help");
    }

    private static class Executor extends DMOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            List<CommandDescriptor> registeredCommands = CommandManager.getRegisteredCommandDescriptors()
                                                                       .stream()
                                                                       .distinct()
                                                                       .sorted(new CommandManager.CommandComparator())
                                                                       .collect(Collectors.toList());
            StringBuilder stringBuilder = new StringBuilder();
            String newLine = "\n";
            stringBuilder.append("```Markdown");
            stringBuilder.append(newLine);
            stringBuilder.append("__HELP__");
            stringBuilder.append(newLine);
            List<String> messages = new ArrayList<>();
            for (CommandDescriptor command : registeredCommands) {
                List<String> helpLines = getCommandHelpLines(command);
                for (String line : helpLines) {
                    if (stringBuilder.length() + line.length() + "```".length() + newLine.length() >= 2000) { // 2000 is max message length
                        stringBuilder.append("```");
                        messages.add(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("```Markdown");
                        stringBuilder.append(newLine);
                    }
                    stringBuilder.append(line);
                }
            }
            stringBuilder.append("```");
            messages.add(stringBuilder.toString());
            messages.forEach(this::sendOutput);
        }

        private List<String> getCommandHelpLines(CommandDescriptor command) {
            List<String> ret = new ArrayList<>();
            String primaryName = command.getPrimaryName();
            String aliases = "";
            if (command.getNames().size() > 1) {
                aliases = ". Aliases: " + command.getNames()
                                               .stream()
                                               .filter(name -> !Objects.equals(name, primaryName))
                                               .sorted()
                                               .collect(Collectors.joining(", "));
            }
            String format = String.format("[%s \"%s\"](%s%s)%n", primaryName, command.help().args(),
                    command.help().description(), aliases);
            ret.add(format);
            if (command.hasSubCommands()) {
                for (CommandDescriptor subCommand : command.getSubCommands()) {
                    List<String> subLines = getCommandHelpLines(subCommand);
                    List<String> subLinesWithPrefix = subLines.stream().map(x -> "-" + x).collect(Collectors.toList());
                    ret.addAll(subLinesWithPrefix);
                }
            }
            return ret;
        }
    }
}
