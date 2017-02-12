package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.MessageUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CommandRoles implements Command {

    private static final String HELP = "Lists all roles and their ID's in the guild. Use: " + ReasonsMain.prefix + "roles";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        List<Role> roles = guild.getRoles();
        List<String> roleStrings = roles.stream().filter(role -> role.compareTo(event.getGuild().getPublicRole()) != 0).map(role -> role.getName() + ": " + role.getId() + "\n").collect(Collectors.toList());

        StringBuilder stringBuilder = new StringBuilder();
        roleStrings.forEach(stringBuilder::append);
        MessageUtils.sendMessageSafeSplitOnChar(event.getChannel(), stringBuilder.toString(), '\n');
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] {"roles", "listroles", "list-roles", "getroles", "get-roles"};
    }
}
