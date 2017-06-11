package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.config.GuildConfigManager;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.DMOutputCommandExecutor;
import com.srgood.reasons.impl.permissions.Permission;
import com.srgood.reasons.impl.permissions.PermissionChecker;
import com.srgood.reasons.impl.utils.GuildDataManager;

import java.util.*;

public class CommandCensorDescriptor extends MultiTierCommandDescriptor {
    public CommandCensorDescriptor() {
        super(new LinkedHashSet<>(
                Arrays.asList(
                        new ListDescriptor(),
                        new AddDescriptor(),
                        new RemoveDescriptor())),
                "Performs operations with the censorlist of the current Guild",
                "<list | add | remove> <...>",
                "censor");
    }

    private static abstract class BaseExecutor extends DMOutputCommandExecutor {
        private final boolean checkPermissions;

        public BaseExecutor(CommandExecutionData executionData, boolean checkPermissions) {
            super(executionData);
            this.checkPermissions = checkPermissions;
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            if (!checkPermissions) return Optional.empty();

            return PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), Permission.MANAGE_CENSOR);
        }
    }

    private static class ListDescriptor extends BaseCommandDescriptor {
        public ListDescriptor() {
            super(Executor::new, "Gets the current censorlist", "<>","list");
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData, false);
            }

            @Override
            public void execute() {
                List<String> censoredWords = GuildDataManager.getGuildCensorList(executionData.getBotManager().getConfigManager(), executionData.getGuild());

                StringBuilder outBuilder = new StringBuilder("__**Censored Words**__ ```\n");
                for (String censoredWord : censoredWords) {
                    outBuilder.append("- ");
                    outBuilder.append(censoredWord);
                    outBuilder.append("\n");
                }
                outBuilder.append("```");
                if (censoredWords.size() == 0) {
                    outBuilder = new StringBuilder("There are no censored words on this server.");
                }
                sendOutput(outBuilder.toString());
            }
        }
    }

    private static class AddDescriptor extends BaseCommandDescriptor {
        public AddDescriptor() {
            super(Executor::new, "Adds a word to the current censorlist", "<word>", "add");
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData, true);
            }

            @Override
            public void execute() {
                List<String> censoredWords = new ArrayList<>(GuildDataManager.getGuildCensorList(executionData.getBotManager().getConfigManager(), executionData.getGuild()));
                String wordToCensor = executionData.getParsedArguments().get(0).toLowerCase();
                censoredWords.add(wordToCensor);
                GuildDataManager.setGuildCensorList(executionData.getBotManager().getConfigManager(), executionData.getGuild(), censoredWords);

                sendOutput("The word `%s` has been added to the censorlist.", wordToCensor);
            }
        }
    }

    private static class RemoveDescriptor extends BaseCommandDescriptor  {
        public RemoveDescriptor() {
            super(Executor::new, "Removes a word from the current censorlist", "<word>", "remove");
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData,true);
            }

            @Override
            public void execute() {
                GuildConfigManager guildConfigManager = executionData.getBotManager()
                                                                     .getConfigManager()
                                                                     .getGuildConfigManager(executionData.getGuild());
                List<String> censoredWords = GuildDataManager.getGuildCensorList(executionData.getBotManager().getConfigManager(), executionData.getGuild());

                String wordToRemove = executionData.getParsedArguments().get(0).toLowerCase();
                boolean inList = censoredWords.contains(wordToRemove);

                if (inList) {
                    censoredWords.remove(wordToRemove);
                    GuildDataManager.setGuildCensorList(executionData.getBotManager().getConfigManager(), executionData.getGuild(), censoredWords);
                    sendOutput("The word `%s` has been removed from the censor list.", wordToRemove);
                } else {
                    sendOutput("The word `%s` was not found in the censor list.", wordToRemove);
                }
            }
        }
    }
}
