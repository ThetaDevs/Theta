package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.utils.GuildDataManager;
import com.srgood.reasons.impl.commands.utils.StringUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class CommandBlacklistDescriptor extends MultiTierCommandDescriptor {
    public CommandBlacklistDescriptor() {
        super(new LinkedHashSet<>(Arrays.asList(new ListDescriptor(), new AddDescriptor(), new RemoveDescriptor())), "Manages this Guild's blacklist.", "<list | add | remove> <...>", "blacklist");
    }

    private static class ListDescriptor extends BaseCommandDescriptor {
        public ListDescriptor() {
            super(Executor::new, "Lists the IDs on this Guild's blacklist", "<>", "list", "get");
        }

        private static class Executor extends ChannelOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> blacklistedIDs = GuildDataManager.getGuildBlacklist(executionData.getBotManager()
                                                                                              .getConfigManager(), executionData
                        .getGuild());
                if (blacklistedIDs.size() == 0) {
                    sendSuccess("There are no roles or users in this Guild's blacklist");
                    return;
                }
                List<String> outputs = new ArrayList<>();
                for (String id : blacklistedIDs) {
                    Member member = executionData.getGuild().getMemberById(id);
                    if (member != null) {
                        outputs.add(String.format("[Member %#s]", member.getUser()));
                        continue;
                    }
                    Role role = executionData.getGuild().getRoleById(id);
                    if (role != null) {
                        outputs.add(String.format("[Role @%s]", role.getName()));
                        continue;
                    }
                    outputs.add(String.format("[User/Role ID %s]", id));
                }
                StringUtils.groupMessagesToLength(outputs, 2000, "", "").forEach(this::sendSuccess);
            }
        }
    }

    private static class AddDescriptor extends BaseCommandDescriptor {
        public AddDescriptor() {
            super(Executor::new, "Adds a member/role ID to this Guild's blacklist", "<ID>", "add");
        }

        private static class Executor extends ChannelOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> blacklist = GuildDataManager.getGuildBlacklist(executionData.getBotManager()
                                                                                         .getConfigManager(), executionData
                        .getGuild());
                if (blacklist.contains(executionData.getParsedArguments().get(0))) {
                    sendError("```ID \"%s\" was already in the blacklist```", executionData.getParsedArguments()
                                                                                            .get(0));
                    return;
                }
                blacklist.add(executionData.getParsedArguments().get(0));
                GuildDataManager.setGuildBlacklist(executionData.getBotManager()
                                                                .getConfigManager(), executionData.getGuild(), blacklist);
                sendSuccess("```ID \"%s\" added to blacklist.```", executionData.getParsedArguments().get(0));
            }
        }
    }

    private static class RemoveDescriptor extends BaseCommandDescriptor {
        public RemoveDescriptor() {
            super(Executor::new, "Removes a member/role ID from this Guild's blacklist", "<ID>", "remove");
        }

        private static class Executor extends ChannelOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> blacklist = GuildDataManager.getGuildBlacklist(executionData.getBotManager()
                                                                                         .getConfigManager(), executionData
                        .getGuild());
                if (!blacklist.contains(executionData.getParsedArguments().get(0))) {
                    sendError("ID \"%s\" was not in the blacklist.", executionData.getParsedArguments().get(0));
                    return;
                }
                blacklist.remove(executionData.getParsedArguments().get(0));
                GuildDataManager.setGuildBlacklist(executionData.getBotManager()
                                                                .getConfigManager(), executionData.getGuild(), blacklist);
                sendSuccess("ID \"%s\" removed from blacklist.", executionData.getParsedArguments().get(0));
            }
        }
    }
}
