package com.srgood.reasons.commands;

import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommandManager {
    private static final Lock channelThreadMapLock = new ReentrantLock();
    private static final Map<String, CommandDescriptor> commands = new TreeMap<>();
    private static final Map<String, ChannelCommandThread> channelThreadMap = new java.util.HashMap<>();

    public static class CommandComparator implements Comparator<CommandDescriptor>, Serializable {

        @Override
        public int compare(CommandDescriptor command0, CommandDescriptor command1) {
            return command0.getPrimaryName().compareTo(command1.getPrimaryName());
        }
    }

        public static void handleCommand(Message cmd) {
            CommandExecutionData executionData = new CommandExecutionData(cmd);
            String calledCommand = CommandUtils.getCalledCommand(cmd);
            CommandDescriptor descriptor = getCommandDescriptorByName(calledCommand);
            // checks if the referenced command is in the command list
            if (descriptor != null) {
                CommandExecutor executor = descriptor.getExecutor(executionData);
                ConfigUtils.initCommandConfigIfNotExists(cmd.getGuild(), calledCommand);
            //if the command is enabled for the message's guild...
            //if the message author has the required permission level...
            if (ConfigUtils.isCommandEnabled(cmd.getGuild(), descriptor)) {
                getChannelThreadMapLock().lock();
                ChannelCommandThread channelCommandThread = channelThreadMap.computeIfAbsent(
                        cmd.getChannel().getId(), id -> new ChannelCommandThread(cmd.getChannel()));
                getChannelThreadMapLock().unlock();
                channelCommandThread.addCommand(cmd);
                if (channelCommandThread.getState() == Thread.State.NEW) {
                    channelCommandThread.start();
                }
            } else {
                cmd.getChannel().sendMessage("This command is disabled").queue();
            }
        } else {
            cmd.getChannel().sendMessage(String.format("Unknown command `%s`", calledCommand)).queue();
        }
    }

    public static CommandDescriptor getCommandDescriptorByName(String name) {
        return commands.get(name);
    }

    static Lock getChannelThreadMapLock() {
        return channelThreadMapLock;
    }

    static void deregisterThread(ChannelCommandThread thread) {
        getChannelThreadMapLock().lock();
        if (channelThreadMap.containsKey(thread.getChannelID())) {
            channelThreadMap.remove(thread.getChannelID());
        }
        getChannelThreadMapLock().unlock();
    }

    public static void registerCommandDescriptor(CommandDescriptor descriptor) {
        List<String> names = descriptor.getNames();
        for (String name : names) {
            commands.put(name, descriptor);
            SimpleLog.getLog("Theta").info("Registered command " + name);
        }
    }

    public static Set<CommandDescriptor> getRegisteredCommandDescriptors() {
        return new HashSet<>(commands.values());
    }

    public static void setCommandEnabled(Guild guild, CommandDescriptor cmd, boolean enabled) {
        if (!cmd.canSetEnabled()) {
            throw new IllegalArgumentException("Cannot toggle this command");
        }
        ConfigUtils.setCommandEnabled(guild, cmd, enabled);
    }
}
