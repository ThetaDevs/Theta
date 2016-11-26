package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandGetPrefix implements Command {


    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Prefix: " + ConfigUtils.getGuildPrefix(event.getGuild())).queue();
    }

    @Override
    public String help() {
        return "Prints the current prefix for this server. Use: '" + ReasonsMain.prefix + "getprefix'";
    }


    @Override
    public String[] names() {
        return new String[] {"getprefix","prefix"};
    }

}
