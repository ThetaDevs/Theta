package com.srgood.reasons.commands.Audio;

import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.utils.audio.AudioUtils;
import com.srgood.reasons.utils.audio.GuildMusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by srgood on 1/1/2017.
 */
public class CommandAudioPause implements AudioCommand {
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {

        GuildMusicManager musicManager = AudioCommand.init(event).getMusicManager();

        if (!musicManager.player.isPaused()) {
            AudioUtils.pause(musicManager);
        } else {
            event.getChannel().sendMessage("Playback is already paused").queue();
        }


    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.MUSIC_DJ;
    }
}
