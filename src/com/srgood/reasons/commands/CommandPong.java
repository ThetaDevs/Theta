package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandPong implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Ping " + event.getAuthor().getAsMention());
    }

    @Override
    public String help() {
        return "Pong! Use: '" + ReasonsMain.prefix + "pong'";
    }

}
