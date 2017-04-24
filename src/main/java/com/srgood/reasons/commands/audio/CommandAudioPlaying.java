package com.srgood.reasons.commands.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.srgood.reasons.commands.PermissionLevels;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import static com.srgood.reasons.utils.audio.AudioUtils.formatTimeMilis;


/**
 * Created by srgood on 1/2/2017.
 */
public class CommandAudioPlaying implements AudioCommand {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        AudioContainer container = AudioCommand.init(event);
        AudioTrackInfo ai = container.getMusicManager().player.getPlayingTrack().getInfo();
        long position = container.getMusicManager().player.getPlayingTrack().getPosition();

        long len = ai.length;

        event.getChannel().sendMessage("Playing " + ai.title + " by: " + ai.author + " (" + formatTimeMilis(position) + "/" + formatTimeMilis(len) + ")").queue();
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.STANDARD;
    }
}
