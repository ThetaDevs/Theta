package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.Reference;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandInvite implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(String.format("Reasons authorization link: %s", Reference.Strings.INVITE_LINK));
    }

    @Override
    public String help() {
        
        return "Prints the link to add Reasons to another server. Use: '" + ReasonsMain.prefix + "invite'";
    }

}
