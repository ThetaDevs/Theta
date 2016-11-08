package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.GuildUtils;
import com.srgood.reasons.utils.ImageUtils;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class CommandDebug implements Command {
    private static final String HELP = "Used internally for debugging. Use: '" + ReasonsMain.prefix + "debug [debug arg]'";
    private static final boolean ALLOW_DEBUG = true;

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return ALLOW_DEBUG;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reinitguild":
                case "flushandinitguild":
                    GuildUtils.deleteGuild(event.getGuild());
                    com.srgood.reasons.utils.GuildUtils.initGuild(event.getGuild());
                    event.getChannel().sendMessage("Done");
                    break;
                case "flushguild":
                case "deleteguild":
                    GuildUtils.deleteGuild(event.getGuild());
                    break;
                case "verifyconfig":
                case "verifyxml":
                    event.getChannel().sendMessage("" + ConfigUtils.verifyConfig());
                    break;
                case "getuptime":
                    long seconds = Duration.between(ReasonsMain.startInstant, Instant.now()).getSeconds();
                    long absSeconds = Math.abs(seconds);
                    String positive = String.format("%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
                    String x = seconds < 0 ? "-" + positive : positive;
                    event.getChannel().sendMessage(x);
                    break;
                case "except":
                case "throw":
                    new Exception("Test Exception").printStackTrace();
                    break;
                case "audiotest":
                    //whatever url you want to pley...
                    String url = "https://www.youtube.com/watch?v=IxijStMHzyA";
                    break;
                case "rendertest":
                    int[] ops = {1,2,3,2,4};
                    try {
                        event.getChannel().sendFile(ImageUtils.renderVote("Test", new String[] {"A","B","C","D","E"},ops),null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    event.getChannel().sendMessage("Invalid argument");
                    break;
            }
        } else {
            event.getChannel().sendMessage("Author Name: " + event.getAuthor().getUsername() + "\n" + "Author Nick: " + event.getAuthorNick() + "\n" + "id: " + event.getAuthor().getId() + "\n" + event.getAuthor().getAsMention() + "\n" + "Picture url: " + event.getAuthor().getAvatarUrl() + "\n" + "ReasonsMain.jda.getSelfInfo().getAsMention().length()" + ReasonsMain.jda.getSelfInfo().getAsMention().length() + "\n" + "ReasonsMain.jda.getSelfInfo().getAsMention()" + ReasonsMain.jda.getSelfInfo().getAsMention() + "\n" + "ConfigUtils.verifyConfig() = " + ConfigUtils.verifyConfig());
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
        return new String[] {"debug"};
    }

}