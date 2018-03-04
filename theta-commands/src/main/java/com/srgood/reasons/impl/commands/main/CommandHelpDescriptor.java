package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.DMOutputCommandExecutor;
import com.srgood.reasons.impl.commands.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class CommandHelpDescriptor extends BaseCommandDescriptor {
    public CommandHelpDescriptor() {
        super(Executor::new, "Provides information about all commands if no arguments are given, otherwise only the commands given as arguments.", "{command} {...}", "help");
    }

    private static class Executor extends DMOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() < 1) {
                displayAllCommands();
            } else {
                displayCommands(executionData.getParsedArguments());
            }
        }

        private void displayAllCommands() {
            displayCommands(executionData.getBotManager()
                                         .getCommandManager()
                                         .getRegisteredCommands()
                                         .stream()
                                         .distinct()
                                         .filter(commandDescriptor -> commandDescriptor.help().visible())
                                         .map(CommandDescriptor::getPrimaryName)
                                         .sorted()
                                         .collect(Collectors.toList()));
        }


        private void displayCommands(List<String> commands) {
            List<String> helpLines = commands.stream()
                                             .map(this::getSingleCommandOutput)
                                             .flatMap(Collection::stream)
                                             .collect(Collectors.toList());
            helpLines.add(0, "__HELP__");
            List<String> messages = StringUtils.groupMessagesToLength(helpLines, 2000, "```Markdown\n", "```");
            messages.forEach(this::sendRaw);
        }

        private List<String> getSingleCommandOutput(String command) {
            CommandDescriptor commandDescriptor = executionData.getBotManager()
                                                               .getCommandManager()
                                                               .getCommandByName(command);
            if (commandDescriptor == null) {
                return Collections.singletonList(String.format("There was no command found by the name [%s]", command));
            }
            return getCommandHelpLines(commandDescriptor);
        }

        private List<String> getCommandHelpLines(CommandDescriptor command) {
            List<String> ret = new ArrayList<>();
            String primaryName = command.getPrimaryName();
            String regexLine = !Objects.equals(command.getNameRegex(), "(" + command.getPrimaryName() + ")") ? ". Matched on Regex: \"" + command
                    .getNameRegex() + "\"" : "";
            String format = String.format("[%s \"%s\"](%s%s)", primaryName, command.help().args(), command.help()
                                                                                                          .description(), regexLine);
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
