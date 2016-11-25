package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class CommandCoinFlip implements Command {

    private static final String HELP = "Flips a coin and prints the result. Use: '" + ReasonsMain.prefix + "flip'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        
        Random r = new Random();
        int n = r.nextInt(6002);
        if (n < 3000) {
            event.getChannel().sendMessage("Heads");
        } else if (n > 3001) {
            event.getChannel().sendMessage("Tails");
        } else {
            event.getChannel().sendMessage("Side");
        }
    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        

    }

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        
        return PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"coinflip"};
    }

}
