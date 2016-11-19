package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class CommandDiceRoll implements Command {

    private static final String HELP = "Rolls a dice (or die) and prints the results. Use: '" + ReasonsMain.prefix + "roll [# dice MAX: 50; MIN: 0; DEFAULT: 1]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"diceroll"};
    }

}
