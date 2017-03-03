package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.utils.MessageUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandHelp implements Command {
    private static final String HELP = "Lists all commands (only primary aliases). Use: '" + ReasonsMain.prefix + "HELP'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            StringBuilder message = new StringBuilder();
            message.append("All commands: ").append("\n\n");

            CommandUtils.getCommandsMap().values().stream()
                        .sorted(new CommandUtils.CommandComparator())
                        .distinct()
                        .map(command ->
                                "**" + CommandUtils.getNameFromCommand(command) + ":** " + " `" + command.help() + "`\n\n")
                        .forEach(message::append);

            MessageUtils.sendMessageSafeSplitOnChar(privateChannel, message.toString(), '\n');
            event.getChannel().sendMessage("Commands were set to you in a private message").queue();
        });

    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] { "help" };
    }

}