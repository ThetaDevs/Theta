package com.srgood.reasons.commands;

import com.srgood.reasons.BotManager;

import java.util.Collections;
import java.util.Set;

public interface CommandDescriptor {
    CommandExecutor getExecutor(CommandExecutionData executionData);

    HelpData help();

    String getNameRegex();

    String getPrimaryName();

    default void init(BotManager botManager) {}

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
