package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.DMOutputCommandExecutor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRolesDescriptor extends BaseCommandDescriptor {
    public CommandRolesDescriptor() {
        super(Executor::new, "Performs operations with the censorlist of the current Guild", "<list | add | remove> <...>", Collections
                .singletonList("roles"));
    }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> roleList = executionData.getGuild()
                                                     .getRoles()
                                                     .stream()
                                                     .filter(role -> !role.equals(executionData.getGuild()
                                                                                               .getPublicRole()))
                                                     .sorted(Comparator.reverseOrder())
                                                     .map(role -> String.format("[%s] %s", role.getName(), role.getId()))
                                                     .collect(Collectors.toList());
                sendOutput("**`Roles in %s`**", executionData.getGuild().getName());
                roleList.forEach(this::sendOutput);
                roleList.forEach(System.out::println);
            }
        }
}
