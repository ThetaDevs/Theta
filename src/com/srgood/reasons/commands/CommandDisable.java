package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.config.ConfigUtils;
import com.srgood.reasons.utils.CommandUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandDisable implements Command {
    private static final String HELP = "Disables a command on this server. Use: '" + ReasonsMain.prefix + "disable <command>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length > 0) {
            Command mCommand = CommandUtils.getCommandByName(args[0]);
            try {
                CommandUtils.setCommandEnabled(event.getGuild(), mCommand, false);
                event.getChannel().sendMessage(String.format("Command %s disabled.", CommandUtils.getNameFromCommand(mCommand))).queue();
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(String.format("Cannot disable command %s.", CommandUtils.getNameFromCommand(mCommand))).queue();
            }
        } else {
            event.getChannel().sendMessage("Please specify a command to toggle").queue();
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
        return PermissionLevels.ADMINISTRATOR;
    }

    @Override
    public String[] names() {
        return new String[] { "disable" };
    }

}
