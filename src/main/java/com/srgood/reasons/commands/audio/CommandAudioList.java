package com.srgood.reasons.commands.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.srgood.reasons.utils.MessageUtils;
import com.srgood.reasons.utils.audio.AudioUtils;
import com.srgood.reasons.utils.audio.GuildMusicManager;
import com.srgood.reasons.utils.audio.TrackScheduler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.collections4.list.UnmodifiableList;

/**
 * Created by srgood on 1/15/2017.
 */
public class CommandAudioList implements AudioCommand{
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        AudioContainer container = AudioCommand.init(event);
        AudioTrackInfo playingTrackInfo = container.getMusicManager().player.getPlayingTrack().getInfo();
        UnmodifiableList<AudioTrack> queue = container.getMusicManager().scheduler.getQueue();
        long totalLen = playingTrackInfo.length;


        StringBuilder sb = new StringBuilder("1. **")
                .append(playingTrackInfo.title)
                .append("** by: *")
                .append(playingTrackInfo.author)
                .append("* (")
                .append(AudioUtils.formatTimeMilis(playingTrackInfo.length))
                .append(")\n");


        for (int i = 0; i < queue.size(); i++) {
            AudioTrack t = queue.get(i);
            sb
                    .append(i + 2)
                    .append(". ")
                    .append(formatInfo(t.getInfo()))
                    .append("*\n");

            totalLen += t.getInfo().length;
        }

        sb.append("Total Time " + AudioUtils.formatTimeMilis(totalLen));

        MessageUtils.sendMessageSafeSplitOnChar(event.getChannel(),sb.toString(),'\n');
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String[] names() {return new String[] {"list","audiolist","queue","al"};}

    private String formatInfo(AudioTrackInfo ti) {
        return "**" +
                ti.title +
                "** by: *" +
                ti.author +
                " (" +
                AudioUtils.formatTimeMilis(ti.length) +
                ")";
    }
}

