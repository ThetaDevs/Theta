package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by dmanl on 9/24/2016.
 */
public class CommandNotifyRand implements Command {

    private static final String HELP = "Notifies a random member of a Role. Use: '" + ReasonsMain.prefix + "notifyrand <role ID>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    public User getRandomUser(List<User> users,Boolean isOnline) {
        User user = null;

        if (isOnline) {

        }
        return null;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Random r = new Random();

        Role role = event.getGuild().getRoleById(args[0]);
        List<User> users = event.getGuild().getUsersWithRole(role);
        User user = null;

        if (args.length > 0) {

            if (args.length > 1) {

            } else {

            }

        } else {

        }




        user.getPrivateChannel().sendMessage(event.getAuthor().getUsername() + " has requested your presence at " + event.getGuild().getName() + " in #" + event.getChannel().getName());
        event.getChannel().sendMessage(user.getUsername() + " has been notified");




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
}
