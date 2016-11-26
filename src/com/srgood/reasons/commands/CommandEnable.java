package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandEnable implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length > 0) {
            Command mCommand = CommandUtils.getCommandByName(args[0]);
            try {
                CommandUtils.setCommandEnabled(event.getGuild(), mCommand, true);
                event.getChannel().sendMessage(String.format("Command %s enabled.", CommandUtils.getNameFromCommand(mCommand)));
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(String.format("Cannot enable command %s.", CommandUtils.getNameFromCommand(mCommand)));
            }
        } else {
            event.getChannel().sendMessage("Please specify a command to enable.");
        }
    }

    @Override
    public String help() {
        return "Enables a command on this server. Use: '" + ReasonsMain.prefix + "enable <command>'";
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.ADMINISTRATOR;
    }

}
