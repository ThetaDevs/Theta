package com.srgood.reasons.commands.Audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by srgood on 1/2/2017.
 */
public class CommandAudioPlaying implements AudioCommand{
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        AudioTrackInfo ai = AudioCommand.init(event).getMusicManager().player.getPlayingTrack().getInfo();
        event.getChannel().sendMessage("Playing " + ai.title + " by: " + ai.author ).queue();
    }

    @Override
    public String help() {
        return null;
    }
}
