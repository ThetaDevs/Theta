package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

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
                event.getChannel().sendMessage(String.format("Command %s disabled.", CommandUtils.getNameFromCommand(mCommand)));
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(String.format("Cannot disable command %s.", CommandUtils.getNameFromCommand(mCommand)));
            }
        } else {
            event.getChannel().sendMessage("Please specify a command to toggle");
        }
    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        
        return PermissionLevels.ADMINISTRATOR;
    }

    @Override
    public String[] names() {
        return new String[] { "disable" };
    }

}
