package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.GuildUtils;
import com.srgood.reasons.utils.ImageUtils;
import com.srgood.reasons.utils.Permissions.PermissionUtils;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class CommandDebug implements Command {
    private static final boolean ALLOW_DEBUG = true;

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return ALLOW_DEBUG;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reinitguild":
                case "flushandinitguild":
                    GuildUtils.deleteGuild(event.getGuild());
                    com.srgood.reasons.utils.GuildUtils.initGuild(event.getGuild());
                    event.getChannel().sendMessage("Done").queue();
                    break;
                case "flushguild":
                case "deleteguild":
                    GuildUtils.deleteGuild(event.getGuild());
                    break;
                case "verifyconfig":
                case "verifyxml":
                    event.getChannel().sendMessage("" + ConfigUtils.verifyConfig()).queue();
                    break;
                case "getuptime":
                    long seconds = Duration.between(ReasonsMain.startInstant, Instant.now()).getSeconds();
                    long absSeconds = Math.abs(seconds);
                    String positive = String.format("%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
                    String x = seconds < 0 ? "-" + positive : positive;
                    event.getChannel().sendMessage(x).queue();
                    break;
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
                case "roletest":
                    Role role = PermissionUtils.createRole(PermissionLevels.ADMINISTRATOR, event.getGuild(), false);
                    role.getManager().setMentionable(true);
                    event.getChannel().sendMessage(role.getAsMention()).queue();
                    break;
                default:
                    event.getChannel().sendMessage("Invalid argument").queue();
                    break;
            }
        } else {
            event.getChannel().sendMessage("Author Name: " + event.getMember().getEffectiveName() + "\n" + "Author Nick: " + event.getMember().getEffectiveName() + "\n" + "id: " + event.getAuthor().getId() + "\n" + event.getAuthor().getAsMention() + "\n" + "Picture url: " + event.getAuthor().getAvatarUrl() + "\n" + "ReasonsMain.jda.getSelfInfo().getAsMention().length()" + ReasonsMain.jda.getSelfUser().getAsMention().length() + "\n" + "ReasonsMain.jda.getSelfInfo().getAsMention()" + ReasonsMain.jda.getSelfUser().getAsMention() + "\n" + "ConfigUtils.verifyConfig() = " + ConfigUtils.verifyConfig()).queue();
        }

    }

    @Override
    public String help() {
        
        return "Used internally for debugging. Use: '" + ReasonsMain.prefix + "debug [debug arg]'";
    }


}