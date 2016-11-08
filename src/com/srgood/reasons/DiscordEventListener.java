package com.srgood.reasons;

import com.srgood.reasons.utils.GuildUtils;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.events.voice.VoiceLeaveEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.utils.SimpleLog;

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

        GuildUtils.doPreMessageGuildCheck(event.getGuild());

        String localPrefix = ConfigUtils.getGuildPrefix(event.getGuild());

        if (event.getMessage().getContent().equals(Constants.Strings.TABLE_FLIP)) {
            event.getChannel().sendMessage(Constants.Strings.TABLE_UNFLIP_JOKE);
        }

        if (event.getMessage().getContent().startsWith(localPrefix) && !java.util.Objects.equals(event.getMessage().getAuthor().getId(), event.getJDA().getSelfInfo().getId())) {
            com.srgood.reasons.utils.CommandUtils.handleCommand(ReasonsMain.parser.parse(event.getMessage().getContent().toLowerCase(), event, localPrefix));
            SimpleLog.getLog("Reasons").info("Got prefixed input: " + event.getMessage().getContent());
        } else {
            try {

                if (event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {

                    SimpleLog.getLog("Reasons").info("Got prefixed input (mention): " + event.getMessage().getContent());
                    com.srgood.reasons.utils.CommandUtils.handleCommand(ReasonsMain.parser.parse(event.getMessage().getContent().toLowerCase(), event, localPrefix));
                }
            } catch (Exception ignored) {

            }
        }

    }


    @Override
    public void onGuildJoin(GuildJoinEvent event) {
//          initGuild(event.getGuild());
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        GuildMessageReceivedEvent e = new GuildMessageReceivedEvent(event.getJDA(),event.getResponseNumber(),event.getMessage(),event.getChannel());
        this.onGuildMessageReceived(e);
    }

    @Override
    public void onVoiceLeave(VoiceLeaveEvent event) {
        if (event.getOldChannel().getUsers().size() == 1) {
            AudioManager manager = event.getGuild().getAudioManager();

            manager.closeAudioConnection();
        }
    }

}