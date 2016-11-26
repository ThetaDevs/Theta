package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class CommandCoinFlip implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        
        Random r = new Random();
        int n = r.nextInt(6000) + 1;
        if (n < 3000) {
            event.getChannel().sendMessage("Heads");
        } else if (n > 3000) {
            event.getChannel().sendMessage("Tails");
        } else {
            event.getChannel().sendMessage("Side");
        }
    }

    @Override
    public String help() {
        return "Flips a coin and prints the result. Use: '" + ReasonsMain.prefix + "flip'";
    }


}
