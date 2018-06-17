package com.srgood.reasons.commands;

import java.util.Collections;
import java.util.Set;

public interface CommandDescriptor {
    CommandExecutor getExecutor(CommandExecutionData executionData);

    HelpData help();

    String getNameRegex();

    String getPrimaryName();

    default boolean canSetEnabled() {
        return true;
    }

    default boolean hasSubCommands() {
        return !getSubCommands().isEmpty();
    }

    default Set<CommandDescriptor> getSubCommands() {
        return Collections.emptySet();
    }

    default Set<String> getDeclaredPermissions() {
        return Collections.emptySet();
    }
}
