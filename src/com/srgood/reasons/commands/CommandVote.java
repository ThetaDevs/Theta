package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.Reference;
import com.srgood.reasons.utils.config.ConfigUtils;
import com.srgood.reasons.utils.ImageUtils;
import com.srgood.reasons.utils.Vote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dmanl on 9/26/2016.
 */
public class CommandVote implements Command{

    private final static String HELP = "Begins a vote with the specified options that ends after the specified amount of seconds. Use: '" + ReasonsMain.prefix + "vote <duration (seconds)> <option 1> <option 2> [option 3 [option 4 [option 5 [option 6 [...]]]]]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {

        if (args.length >= 3 && args.length <= Reference.Strings.COLORS.length + 2) {
            Map<String,Integer> voteMap = new LinkedHashMap<>();

            for (int i = 2; i < args.length; i++) {
                voteMap.put(args[i],0);
            }

            event.getChannel().sendMessage("A vote has started! use `vote <option>` to vote!").queue();
            new Vote(voteMap, Integer.parseInt(args[0]),event.getChannel(), new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();

                    for (String st : voteMap.keySet()) {
                        sb.append(st).append(": ").append(voteMap.get(st)).append("\n");
                    }
                    event.getChannel().sendMessage(sb.toString()).queue();
                    try {
                        event.getChannel().sendFile(ImageUtils.renderVote(
                                args[1],
                                voteMap.keySet().stream().toArray(String[]::new),
                                integerArrToIntArr(
                                        voteMap.values()
                                                .<Integer>toArray(
                                                        new Integer[] {}
                                                        )
                                )
                        ), null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                private int[] integerArrToIntArr(Integer[] integers) {
                    int[] ret = new int[integers.length];
                    for (int i = 0; i < integers.length; i++) {
                        ret[i] = integers[i];
                    }
                    return ret;
                }
            });
        } else {
            event.getChannel().sendMessage("Incorrect arguments, correct usage: " + ConfigUtils.getGuildPrefix(event.getGuild()) + "vote <duration (seconds)> <option 1> <option 2> ... [option 5 (up to " +  Reference.Strings.COLORS.length + " max)]").queue();
        }
    }

    @Override
    public String help() {
        return null;
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
        return PermissionLevels.ADMINISTRATOR;
    }
}
