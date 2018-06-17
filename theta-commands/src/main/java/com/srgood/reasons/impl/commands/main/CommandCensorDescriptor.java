package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.Argument;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.config.GuildConfigManager;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.DMOutputCommandExecutor;
import com.srgood.reasons.impl.commands.utils.GuildDataManager;

import java.util.*;

public class CommandCensorDescriptor extends MultiTierCommandDescriptor {
    public CommandCensorDescriptor() {
        super(new LinkedHashSet<>(Arrays.asList(new ListDescriptor(), new AddDescriptor(), new RemoveDescriptor())), "Performs operations with the censorlist of the current Guild", "censor");
    }

    private static class ListDescriptor extends BaseCommandDescriptor {
        public ListDescriptor() {
            super(Executor::new, "Gets the current censorlist", null, "list");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> censoredWords = GuildDataManager.getGuildCensorList(executionData.getBotManager()
                                                                                              .getConfigManager(), executionData
                        .getGuild());

                StringBuilder outBuilder = new StringBuilder("__**Censored Words**__");
                for (String censoredWord : censoredWords) {
                    outBuilder.append(censoredWord);
                    outBuilder.append("\n");
                }
                if (censoredWords.size() == 0) {
                    outBuilder = new StringBuilder("There are no censored words on this server.");
                }
                sendSuccess(outBuilder.toString());
            }
        }
    }

    private static class AddDescriptor extends BaseCommandDescriptor {
        public AddDescriptor() {
            super(Executor::new, "Adds a word to the current censorlist", Argument.string("word"),new HashSet<String>(){{
                add("censor");
            }}, "add");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> censoredWords = new ArrayList<>(GuildDataManager.getGuildCensorList(executionData.getBotManager()
                                                                                                              .getConfigManager(), executionData
                        .getGuild()));
                String wordToCensor = executionData.getParsedArguments().get(0).toLowerCase();
                censoredWords.add(wordToCensor);
                GuildDataManager.setGuildCensorList(executionData.getBotManager()
                                                                 .getConfigManager(), executionData.getGuild(), censoredWords);

                sendSuccess("The word `%s` has been added to the censorlist.", wordToCensor);
            }

            @Override
            protected void checkCallerPermissions() {
                requirePermission("censor");
            }
        }
    }

    private static class RemoveDescriptor extends BaseCommandDescriptor {
        public RemoveDescriptor() {
            super(Executor::new, "Removes a word from the current censorlist", Argument.string("word"), new HashSet<String>(){{
                add("censor");
            }}, "remove");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                GuildConfigManager guildConfigManager = executionData.getBotManager()
                                                                     .getConfigManager()
                                                                     .getGuildConfigManager(executionData.getGuild());
                List<String> censoredWords = GuildDataManager.getGuildCensorList(executionData.getBotManager()
                                                                                              .getConfigManager(), executionData
                        .getGuild());

                String wordToRemove = executionData.getParsedArguments().get(0).toLowerCase();
                boolean inList = censoredWords.contains(wordToRemove);

                if (inList) {
                    censoredWords.remove(wordToRemove);
                    GuildDataManager.setGuildCensorList(executionData.getBotManager()
                                                                     .getConfigManager(), executionData.getGuild(), censoredWords);
                    sendSuccess("The word `%s` has been removed from the censor list.", wordToRemove);
                } else {
                    sendError("The word `%s` was not found in the censor list.", wordToRemove);
                }
            }

            @Override
            protected void checkCallerPermissions() {
                requirePermission("censor");
            }
        }
    }
}
