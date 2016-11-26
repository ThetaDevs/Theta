package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by dmanl on 9/12/2016.
 */
public class CommandPoing implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Plong " + event.getAuthor().getAsMention());
    }

    @Override
    public String help() {
        return "Poing! Use: '" + ReasonsMain.prefix + "poing'";
    }

}
