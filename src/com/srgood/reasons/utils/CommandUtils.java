package com.srgood.reasons.utils;

import com.srgood.reasons.commands.ChannelCommandThread;
import com.srgood.reasons.commands.Command;
import com.srgood.reasons.commands.CommandParser;
import com.srgood.reasons.utils.config.ConfigUtils;
import com.srgood.reasons.utils.Permissions.PermissionUtils;
import net.dv8tion.jda.core.entities.Guild;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommandUtils {

    private static Map<String, Command> commands = new TreeMap<>();
    private static java.util.Map<String, ChannelCommandThread> channelThreadMap = new java.util.HashMap<>();
    private static final Lock channelThreadMapLock = new ReentrantLock();

    public static void handleCommand(CommandParser.CommandContainer cmd) {
        // checks if the referenced command is in the command list
        if (commands.containsKey(cmd.invoke)) {
            ConfigUtils.initCommandConfigIfNotExists(cmd);
            //if the command is enabled for the message's guild...
            //if the message author has the required permission level...
            if (ConfigUtils.isCommandEnabled(cmd.event.getGuild(), commands.get(cmd.invoke)))
                if (PermissionUtils.userPermissionLevel(cmd.event.getGuild(), cmd.event.getAuthor()).getLevel() >= commands.get(cmd.invoke).permissionLevel(cmd.event.getGuild()).getLevel()) {
                    boolean shouldExecute = commands.get(cmd.invoke).called(cmd.args, cmd.event);
                    getChannelThreadMapLock().lock();
                    ChannelCommandThread channelCommandThread =
                            channelThreadMap.containsKey(cmd.event.getChannel().getId())
                                    ? channelThreadMap.get(cmd.event.getChannel().getId())
                                    : new ChannelCommandThread(cmd.event.getChannel());
                    channelThreadMap.put(cmd.event.getChannel().getId(), channelCommandThread);
                    getChannelThreadMapLock().unlock();
                    channelCommandThread.addCommand(new ChannelCommandThread.CommandItem(cmd, shouldExecute));
                    if (channelCommandThread.getState() == Thread.State.NEW) {
                        channelCommandThread.start();
                    }
                } else {
                    cmd.event.getChannel().sendMessage("You lack the required permission to preform this action").queue();
                }
            else {
                cmd.event.getChannel().sendMessage("This command is disabled").queue();
            }
        } else {
            cmd.event.getChannel().sendMessage("Unknown command `" + cmd.invoke+"`").queue();
        }
    }

    public static void deregisterThread(ChannelCommandThread thread) {
        getChannelThreadMapLock().lock();
        if (channelThreadMap.containsKey(thread.getChannelID())) {
            channelThreadMap.remove(thread.getChannelID());
        }
        getChannelThreadMapLock().unlock();
    }

    public static String getNameFromCommand(Command cmd) {
        return cmd.names()[0];
    }

    public static String getPrimaryCommandAlias(String name) {
        return getNameFromCommand(getCommandByName(name));
    }

    public static Command getCommandByName(String name) {
        return commands.get(name);
    }

    public static Map<String, Command> getCommandsMap() {
        return commands;
    }

    public static Lock getChannelThreadMapLock() {
        return channelThreadMapLock;
    }

    public static void setCommandEnabled(Guild guild, Command cmd, boolean enabled) {
        if (!cmd.canSetEnabled()) {
            throw new IllegalArgumentException("Cannot toggle this command");
        }
        ConfigUtils.setCommandEnabled(guild, cmd, enabled);
    }

    public static class CommandComparator implements Comparator<Command> {

        @Override
        public int compare(Command command0, Command command1) {
            return getNameFromCommand(command0).compareTo(getNameFromCommand(command1));
        }
    }

}
