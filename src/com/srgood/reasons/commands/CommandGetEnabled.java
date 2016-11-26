package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandGetEnabled implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Command command = CommandUtils.getCommandByName(args[0]);
        ConfigUtils.initCommandConfigIfNotExists(event.getGuild(), command);
        event.getChannel().sendMessage(String.format("Command %s is %s.", CommandUtils.getNameFromCommand(command), ConfigUtils.isCommandEnabled(event.getGuild(), command) ? "enabled" : "disabled"));
    }

    @Override
    public String help() {
        return "Prints whether a command is enabled on this server. Use: '" + ReasonsMain.prefix + "getenabled <command>'";
    }

    @Override
    public String[] names() {
        return new String[] {"getenabled", "isenabled", "commandisenbaled"};
    }
}
