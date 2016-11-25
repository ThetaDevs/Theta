package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.Reference;
import com.srgood.reasons.utils.config.ConfigPersistenceUtils;
import com.srgood.reasons.utils.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.xml.transform.TransformerException;

public class CommandShutdown implements Command {
    private static final String HELP = "Used to shutdown Reasons. Use: '" + ReasonsMain.prefix + "shutdown' -OR- '" + ReasonsMain.prefix + "shutdown override <override key>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        long uid = Long.parseLong(event.getAuthor().getId());


        try {
            if (Reference.Other.BOT_DEVELOPERS.contains(String.valueOf(uid))) {
                event.getChannel().sendMessage("Shutting down! " + event.getAuthor().getAsMention()).queue();
                doShutdown();
            } else {
                if (args[0].toLowerCase().equals("override")) {
                    if (ReasonsMain.overrideKey.equals(args[1])) {
                        event.getChannel().sendMessage("Valid key. Shutting down! " + event.getAuthor().getAsMention()).queue();
                        doShutdown();
                    } else {
                        event.getChannel().sendMessage("Bad key " + event.getAuthor().getAsMention()).queue();
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                if (args[0].toLowerCase().equals("override")) {
                    event.getChannel().sendMessage("Invalid Arguments, you should quit the debate team " + event.getAuthor().getAsMention()).queue();
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                event.getChannel().sendMessage("You aren't me. you cant do that " + event.getAuthor().getAsMention()).queue();
            }

        }

    }

    private void doShutdown() {
        try {
            ConfigPersistenceUtils.writeXML();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ReasonsMain.jda.shutdown();
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
    public PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return PermissionLevels.ADMINISTRATOR;
    }

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public String[] names() {
        return new String[] {"shutdown", "halt", "die", "kill"};
    }

}
