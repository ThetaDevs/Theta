package com.srgood.reasons.commands;


import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayDeque;
import java.util.Deque;

public class ChannelCommandThread extends Thread {

    private MessageChannel channel;

    private final Deque<Message> commandDeque = new ArrayDeque<>();

    private boolean commandWasAdded = false;

    public ChannelCommandThread(MessageChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            //if the commands get method returns true (see command.class)...
            for (int i = 0; i < commandDeque.size(); i++) {
                Message message = commandDeque.getFirst();
                try {
                    String calledCommad = CommandUtils.getCalledCommand(message);
                    CommandDescriptor descriptor = CommandManager.getCommandDescriptorByName(calledCommad);
                    CommandExecutionData executionData = new CommandExecutionData(message);
                    CommandExecutor executor = descriptor.getExecutor(executionData);
                    if (ConfigUtils.isCommandEnabled(message.getGuild(), descriptor)) {
                        if (executor.shouldExecute()) {
                            //then run the command and its post execution code (see command)
                            executor.execute();
                            executor.postExecution();
                        }
                        // Otherwise do nothing
                    } else {
                        message.getChannel().sendMessage("This command is not enabled.").queue();
                    }
                } catch (Exception e) {
                    message.getChannel().sendMessage("A ***FATAL*** exception occurred ( `" + e.getMessage()+ "` ) , please notify us. If possible, store the date and time.").queue();
                    e.printStackTrace();

                }
                commandDeque.removeFirst();
            }
            if (!commandWasAdded) {
                boolean threadLifeOver = attemptEndOfLife();
                if (threadLifeOver) {
                    return;
                }
            }
            commandWasAdded = false;
        }
    }

    private boolean attemptEndOfLife() {
        CommandManager.getChannelThreadMapLock().lock();
        boolean shouldEnd = commandDeque.size() == 0;
        if (shouldEnd) {
            CommandManager.deregisterThread(this);
        }
        CommandManager.getChannelThreadMapLock().unlock();
        return shouldEnd;
    }

    public void addCommand(Message commandMessage) {
        commandDeque.addLast(commandMessage);
        commandWasAdded = true;
    }


    public String getChannelID() {
        return channel.getId();
    }

}
