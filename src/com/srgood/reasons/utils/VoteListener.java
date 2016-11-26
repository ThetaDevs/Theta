package com.srgood.reasons.utils;


import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dmanl on 9/26/2016.
 */
public class VoteListener extends ListenerAdapter {
    private Channel voteChan;
    private Map<String,Integer> selections;
    private List<User> votedUsers = new ArrayList<>();

    public VoteListener(Channel channel, Map<String, Integer> choices) {
        voteChan = channel;
        selections = choices;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getChannel().equals(voteChan)) {
            String msg = event.getMessage().getContent().toLowerCase().trim();

            if (msg.startsWith("vote")) {
                msg = msg.replace("vote ","");
                for (String choice : selections.keySet()) {
                    if (choice.equals(msg)) {
                        if (votedUsers.contains(event.getAuthor())) {
                            event.getChannel().sendMessage("You have already voted " + event.getAuthor().getAsMention()).queue();
                        } else {
                            selections.replace(choice,selections.get(choice) + 1);
                            event.getChannel().sendMessage("Your vote has been counted " + event.getAuthor().getAsMention()).queue();
                            votedUsers.add(event.getAuthor());
                        }
                    }
                }
            }
        }
    }
}
