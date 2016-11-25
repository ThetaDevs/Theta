package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.config.ConfigUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandDelete implements Command {

    private static final String HELP = "Deletes messages. Use: '" + ReasonsMain.prefix + "delete [all|bot] [channel name]' Default is all in current channel";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {

        if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MANAGE_PERMISSIONS)) {
            event.getChannel().sendMessage("Error, unable to delete messages! Please check permissions.").queue();
            return;
        }

        String channel, delType;
        List<Message> messages = event.getChannel().getHistory().getCachedHistory();
        List<Message> buffer = new ArrayList<>();
        boolean needsRecursion = false;

        if (messages.size() > 100) {
            for (; ;) {
                messages.remove(messages.size()-1);
                if (messages.size() == 100) break;
            }
            needsRecursion = true;
        }
/*
        if (delType.equals("bot")) {
            for (Message message : messages) {
                if (!message.getContent().startsWith(ConfigUtils.getGuildPrefix(event.getGuild())) & !event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention()) & !event.getAuthor().getId().equals(event.getJDA().getSelfInfo().getId())) {
                    messages.remove(message);
                }
            }
        }
*/

        event.getChannel().deleteMessages(messages).queue();

        if (needsRecursion) {
            this.action(args,event);
        } else {
            event.getChannel().sendMessage("Successfully Deleted **" + messages.size() + "** messages").queue();
        }


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
    public PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"delete"};
    }

}
