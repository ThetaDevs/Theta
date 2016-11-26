package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by srgood on 11/26/2016.
 */
public class CommandAlias implements Command {
    Command command;

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        if(args.length < 1) {
            event.getChannel().sendMessage("Invalid arguments").queue();
            return false;
        }

        for (Command command0: CommandUtils.getCommandsMap().values()) {
            for (String name: command0.names()) {
                if (name.equals(args[0].trim().toLowerCase())) {
                    command = command0;
                    return true;
                }
            }


        }

        if (command == null) {
            event.getChannel().sendMessage(args[0].trim().toLowerCase() + " is not a valid command").queue();
            return false;
        }

        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        StringBuilder sb = new StringBuilder();
        sb.append("Aliases: ");
        for(String s: command.names()) {
            sb.append(s).append(", ");
        }
        sb.deleteCharAt(sb.length()- 1).deleteCharAt(sb.length() - 1);

        event.getChannel().sendMessage(sb.toString()).queue();
    }

    @Override
    public String help() {
        return "Prints all the aliases for a command use '" + ReasonsMain.prefix + "alias [command]";
    }

    @Override
    public String[] names() {
        return new String[] {"alias","names","aliases"};
    }
}
