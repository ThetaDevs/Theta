package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.DMOutputCommandExecutor;
import com.srgood.reasons.impl.commands.utils.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRolesDescriptor extends BaseCommandDescriptor {
    public CommandRolesDescriptor() {
        super(Executor::new, "Lists the roles and their IDs in the current Guild", "<>", "roles");
    }

    private static class Executor extends DMOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            List<String> roleOutputs = executionData.getGuild()
                                                    .getRoles()
                                                    .stream()
                                                    .sorted(Comparator.reverseOrder())
                                                    .map(role -> String.format("[%s] %s", role.getName(), role.getId()))
                                                    .collect(Collectors.toList());
            roleOutputs.add(0, String.format("**Roles in %s**", executionData.getGuild().getName()));
            StringUtils.groupMessagesToLength(roleOutputs, 2000, "", "").forEach(this::sendSuccess);
        }
    }
}
