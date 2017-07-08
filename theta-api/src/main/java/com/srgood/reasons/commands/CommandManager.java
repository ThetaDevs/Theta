package com.srgood.reasons.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.util.Set;

public interface CommandManager extends AutoCloseable {
    void registerCommand(CommandDescriptor command);
    Set<CommandDescriptor> getRegisteredCommands();

    void handleCommandMessage(Message message);

    void setCommandEnabled(Guild guild, CommandDescriptor command, boolean enabled);

    CommandDescriptor getCommandByName(String name);
}
