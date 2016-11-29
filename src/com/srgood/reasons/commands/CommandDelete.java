package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import static com.srgood.reasons.ReasonsMain.jda;

public class CommandDelete implements Command {

    private int total;

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        total = event.getChannel().getHistory().getCachedHistory().size();
        if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MANAGE_PERMISSIONS)) {
            event.getChannel().sendMessage("Error, unable to delete messages! Please check permissions.").queue();
            return false;
        }
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
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
        return "Deletes messages. Use: '" + ReasonsMain.prefix + "delete [all|bot] [channel name]' Default is all in current channel";
    }

}
