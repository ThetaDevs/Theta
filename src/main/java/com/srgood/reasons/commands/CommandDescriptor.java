package com.srgood.reasons.commands;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface CommandDescriptor {
    CommandExecutor getExecutor(CommandExecutionData executionData);

    HelpData help();

    List<String> getNames();

    default String getPrimaryName() {
        return getNames().get(0);
    }

    default boolean canSetEnabled() {
        return true;
    }

    default boolean hasSubCommands() {
        return !getSubCommands().isEmpty();
    }

    default Set<CommandDescriptor> getSubCommands() {
        return Collections.emptySet();
    }
}
