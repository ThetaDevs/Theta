package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class CommandDiceRoll implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        
        int numRolls;
        StringBuilder stringBuilder = new StringBuilder();
        Random r = new Random();

        if (args.length > 0) {
            if (Integer.parseInt(args[0]) > 50) {
                event.getChannel().sendMessage("Whoa there, Im not going to roll " + args[0] + " dice, how about 50 instead?").queue();
                numRolls = 50;
            } else numRolls = Integer.parseInt(args[0]);

            for (int roll = 0; roll < numRolls; roll++) {
                int randNum = r.nextInt(6) + 1;
                if (roll < numRolls - 1) {
                    stringBuilder.append(randNum).append(", ");
                } else {
                    stringBuilder.append(randNum).append(".");
                }

            }

            event.getChannel().sendMessage(stringBuilder.toString()).queue();
        } else {
            event.getChannel().sendMessage(r.nextInt(6) + 1 + ".").queue();
        }
    }

    @Override
    public String help() {
        
        return "Rolls a dice (or die) and prints the results. Use: '" + ReasonsMain.prefix + "roll [# dice MAX: 50; MIN: 0; DEFAULT: 1]'";
    }

}
