package com.srgood.reasons.commands.audio;

import com.srgood.reasons.utils.audio.AudioUtils;
import com.srgood.reasons.utils.audio.GuildMusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by srgood on 1/1/2017.
 */
public class CommandAudioSkip implements AudioCommand {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        GuildMusicManager musicManager = AudioCommand.init(event).getMusicManager();

        AudioUtils.skipTrack(event.getChannel());

    }

    @Override
    public String help() {
        return null;
    }

}
