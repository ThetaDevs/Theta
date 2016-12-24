package com.srgood.reasons;

import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.GuildUtils;
import com.srgood.reasons.utils.Censor;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.SimpleLog;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>DiscordEventListener</h1>
 * <p>
 * DiscordEventListener deals with MessageReceived events, excluding its own.
 *
 * @author srgood
 * @version 0.8
 * @since 7/12/16
 */
public class DiscordEventListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        try {
            GuildUtils.doPreMessageGuildCheck(event.getGuild());
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }

        String localPrefix = ConfigUtils.getGuildPrefix(event.getGuild());

        if (event.getMessage().getContent().equals(com.srgood.reasons.Reference.Strings.TABLE_FLIP)) {
            event.getChannel().sendMessage(com.srgood.reasons.Reference.Strings.TABLE_UNFLIP_JOKE);
        }

        if (event.getMessage().getContent().startsWith(localPrefix) && !java.util.Objects.equals(event.getMessage().getAuthor().getId(), event.getJDA().getSelfUser().getId())) {
            com.srgood.reasons.utils.CommandUtils.handleCommand(ReasonsMain.COMMAND_PARSER.parse(event.getMessage().getContent().toLowerCase(), event, localPrefix));
            SimpleLog.getLog("Reasons").info("Got prefixed input: " + event.getMessage().getContent());
        } else {
            try {

                if (event.getJDA().getSelfUser().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {

                    SimpleLog.getLog("Reasons").info("Got prefixed input (mention): " + event.getMessage().getContent());
                    com.srgood.reasons.utils.CommandUtils.handleCommand(ReasonsMain.COMMAND_PARSER.parse(event.getMessage().getContent().toLowerCase(), event, localPrefix));
                }
            } catch (Exception ignored) {

            }
        }

        if(Censor.list.containsKey(event.getMessage().getGuild())) {
            for(int i = 0; i < Censor.list.get(event.getMessage().getGuild()).size(); i++) {
                Pattern p = Pattern.compile("\\b" + Censor.list.get(event.getMessage().getGuild()).get(i) + "\\b");
                Matcher m = p.matcher(event.getMessage().getContent().toLowerCase());
                if(m.find()) {
                    event.getAuthor().getPrivateChannel().sendMessage(Censor.list.get(event.getMessage().getGuild()).get(i) + " is not allowed.").queue();
                    event.getMessage().deleteMessage().queue();
                }
            }
        }
    }


    @Override
    public void onGuildJoin(GuildJoinEvent event) {
//          initGuild(event.getGuild());
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        GuildMessageReceivedEvent e = new GuildMessageReceivedEvent(event.getJDA(),event.getResponseNumber(),event.getMessage());
        this.onGuildMessageReceived(e);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (event.getChannelLeft().getMembers().size() == 1) {
            // TODO FIX AUDIO
//            AudioManager manager = event.getGuild();
//
//            manager.closeAudioConnection();
        }
    }

}