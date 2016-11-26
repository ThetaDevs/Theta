package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.Reference;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

/**
 * Created by dmanl on 9/13/2016.
 */
public class Command8Ball implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(Reference.Strings.EIGHT_BALL[new Random().nextInt(Reference.Strings.EIGHT_BALL.length)]);
    }

    @Override
    public String help() {
        return "Prints a random string from our magic 8Ball machine. Use: '" + ReasonsMain.prefix + "8ball'";
    }



}
