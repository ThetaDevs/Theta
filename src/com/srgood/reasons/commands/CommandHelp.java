package com.srgood.reasons.commands;

import com.srgood.reasons.BotMain;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.utils.MessageUtils;
import com.srgood.reasons.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandHelp implements Command {
    private static final String HELP = "Lists all commands (only primary aliases). Use: '" + BotMain.prefix + "HELP'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        PrivateChannel privateChannel = event.getAuthor().getPrivateChannel();

        StringBuilder message = new StringBuilder();
        message.append("All commands: ").append("\n\n");

        CommandUtils.getCommandsMap().values().stream()
                .sorted(new CommandUtils.CommandComparator())
                .distinct()
                .map(command ->
                        "**" + CommandUtils.getNameFromCommand(command) + ":** " + " `" + command.help() + "`\n\n")
                .forEach(message::append);

        MessageUtils.sendMessageSafeSplitOnChar(privateChannel, message.toString(), '\n');
        event.getChannel().sendMessage("Commands were set to you in a private message");

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
    public com.srgood.reasons.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.reasons.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.reasons.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] { "help" };
    }

}
