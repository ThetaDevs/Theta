package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.utils.MessageUtils;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandHelp implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        PrivateChannel privateChannel = event.getAuthor().getPrivateChannel();

        StringBuilder message = new StringBuilder();
        message.append("All commands: ").append("\n\n");

        CommandUtils.getCommandsMap().values().stream().sorted(new CommandUtils.CommandComparator())
                .distinct()
                .map(command ->
                        "**" + CommandUtils.getNameFromCommand(command) + ":** " + " `" + command.help() + "`\n\n")
                .forEach(message::append);

        MessageUtils.sendMessageSafeSplitOnChar(privateChannel, message.toString(), '\n');
        event.getChannel().sendMessage("Commands were set to you in a private message");
    }

    @Override
    public String help() {
        return "Lists all commands (only primary aliases). Use: '" + ReasonsMain.prefix + "HELP'";
    }

    @Override
    public String[] names() {
        return new String[] { "help","commands"};
    }

}
