package com.srgood.reasons.commands.Audio;

import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.audio.Playlist;
import com.srgood.reasons.utils.audio.source.AudioSource;
import com.srgood.reasons.utils.audio.source.RemoteSource;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by srgood on 12/26/2016.
 */
public class CommandAudioPlay implements AudioCommand {

    private UrlValidator validator = new UrlValidator();

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return args.length >= 1 && validator.isValid(args[0]);
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        AudioContainer container = AudioCommand.init(event);

        Playlist playlist = Playlist.getPlaylist(args[0],event.getGuild().getId());
        List<AudioSource> sources = new LinkedList<>(playlist.getSources());
        container.player.audioQueue.add(sources.get(0));
        container.player.play();
    }

    @Override
    public String help() {
        return null;
    }
}
