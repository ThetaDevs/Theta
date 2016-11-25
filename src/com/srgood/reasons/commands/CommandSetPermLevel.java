package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import static com.srgood.reasons.utils.PermissionUtils.stringToRole;
import static com.srgood.reasons.config.ConfigUtils.setCommandPermission;

/**
 * Created by dmanl on 9/11/2016.
 */
public class CommandSetPermLevel implements Command {
    private static final String HELP = "Sets the permission level required for a command on this server. Use: '" + ReasonsMain.prefix + "setpermlevel <command> <role ID>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Command mCommand = CommandUtils.getCommandByName(args[0]);
        PermissionLevels perm;
        perm = args[1].equalsIgnoreCase("DEFAULT") ? mCommand.defaultPermissionLevel() : stringToRole(args[1]);

        setCommandPermission(event.getGuild(),mCommand,perm);

        event.getChannel().sendMessage("Permission for " + CommandUtils.getNameFromCommand(mCommand) + " set to " + perm.getReadableName() + ".");
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
        return new String[] {"setpermlevel"};
    }
}
