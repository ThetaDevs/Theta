package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.Reference;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandInfo implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        //see http://stackoverflow.com/questions/396429/how-do-you-know-what-version-number-to-use
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("notes")) {
                //TODO add an XML field for past and current Release notes. I.E. <notes ver= 0.1.2>Added Version command</Notes>
            }
        } else {
            event.getChannel().sendMessage(String.format("The current version is: %s%n%s", Reference.Strings.VERSION, Reference.Strings.CREDITS)).queue();
        }
    }

    @Override
    public String help() {
        return "Prints the version of Reasons. Use: '" + ReasonsMain.prefix + "version'";
    }

    @Override
    public String[] names() {
        return new String[] { "info", "version","libraries"};
    }

}
