package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

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

    public User getRandomUser(List<User> users, Boolean isOnline) {
        User user = null;

        if (isOnline) {

        }
        return null;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Random r = new Random();

        Role role = event.getGuild().getRoleById(args[0]);
        List<Member> users = event.getGuild().getMembersWithRoles(role);
        Member member = null;

        if (args.length > 0) {

            if (args.length > 1) {

            } else {

            }

        } else {

        }




        member.getUser().getPrivateChannel().sendMessage(event.getMember().getUser().getName() + " has requested your presence at " + event.getGuild().getName() + " in #" + event.getChannel().getName()).queue();
        event.getChannel().sendMessage(member.getEffectiveName() + " has been notified").queue();




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
