package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

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
            event.getChannel().sendMessage("Heads").queue();
        } else if (n > 3001) {
            event.getChannel().sendMessage("Tails").queue();
        } else {
            event.getChannel().sendMessage("Side").queue();
        }
    }

    @Override
    public String help() {
        
        return "Flips a coin and prints the result. Use: '" + ReasonsMain.prefix + "flip'";
    }


}
