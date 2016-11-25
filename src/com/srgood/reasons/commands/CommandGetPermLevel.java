package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandGetPermLevel implements Command {

    private static final String HELP = "Prints the required permission level for a command in this Guild. Use: '" + ReasonsMain.prefix + "getpermlevel <command>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length < 1) {
            event.getChannel().sendMessage("getpermlevel requires 1 argument.");
            return;
        }
        String primaryAlias = CommandUtils.getPrimaryCommandAlias(args[0]);
        Command command = CommandUtils.getCommandByName(primaryAlias);
        event.getChannel().sendMessage(String.format("Permission level for command `%s`: **%s**", primaryAlias, ConfigUtils.getCommandPermission(event.getGuild(), command).getReadableName()));
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
        return new String[] {"getpermlevel"};
    }
}
