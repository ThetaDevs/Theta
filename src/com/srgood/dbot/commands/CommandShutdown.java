package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.Reference;
import com.srgood.dbot.utils.config.ConfigUtils;
import com.srgood.dbot.utils.config.ConfigPersistenceUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import javax.xml.transform.TransformerException;

public class CommandShutdown implements Command {
    private static final String HELP = "Used to shutdown Reasons. Use: '" + BotMain.prefix + "shutdown' -OR- '" + BotMain.prefix + "shutdown override <override key>'";

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
                event.getChannel().sendMessage("Shutting down! " + event.getAuthor().getAsMention());
                doShutdown();
            } else {
                if (args[0].toLowerCase().equals("override")) {
                    if (BotMain.overrideKey.equals(args[1])) {
                        event.getChannel().sendMessage("Valid key. Shutting down! " + event.getAuthor().getAsMention());
                        doShutdown();
                    } else {
                        event.getChannel().sendMessage("Bad key " + event.getAuthor().getAsMention());
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                if (args[0].toLowerCase().equals("override")) {
                    event.getChannel().sendMessage("Invalid Arguments, you should quit the debate team " + event.getAuthor().getAsMention());
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                event.getChannel().sendMessage("You aren't me. you cant do that " + event.getAuthor().getAsMention());
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
        BotMain.jda.shutdown();
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
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.ADMINISTRATOR;
    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public String[] names() {
        return new String[] {"shutdown", "halt", "die", "kill"};
    }

}
