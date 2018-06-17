package com.srgood.reasons.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface CommandManager extends AutoCloseable {
    void registerCommand(CommandDescriptor command);

    Set<CommandDescriptor> getRegisteredCommands();

    void handleCommandMessage(Message message);

    void setCommandEnabled(Guild guild, CommandDescriptor command, boolean enabled);

    CommandDescriptor getCommandByName(String name);

    default Set<String> getDeclaredPermissions() {
        return getRegisteredCommands().stream()
                                      .map(CommandDescriptor::getDeclaredPermissions) // Now a Stream of Set of String
                                      .flatMap(Collection::stream) // Take the elements of every Set and put them into a new Stream
                                      .collect(Collectors.toSet()); // Take the strings and put them into a set
    }
}
