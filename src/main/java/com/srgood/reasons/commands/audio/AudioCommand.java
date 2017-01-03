package com.srgood.reasons.commands.audio;

import com.srgood.reasons.commands.Command;
import com.srgood.reasons.utils.audio.AudioUtils;
import com.srgood.reasons.utils.audio.GuildMusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 * Created by srgood on 12/26/2016.
 */
public interface AudioCommand extends Command {

    static AudioContainer init(GuildMessageReceivedEvent event) {

        return new AudioContainer(event.getGuild());
    }

    class AudioContainer {
        private AudioManager audioManager;
        private GuildMusicManager musicManager;


        AudioContainer(Guild guild) {
            this.audioManager = guild.getAudioManager();
            this.musicManager = AudioUtils.getGuildAudioPlayer(guild);
        }

        public AudioManager getAudioManager() {
            return audioManager;
        }

        public GuildMusicManager getMusicManager() {
            return musicManager;
        }
    }

    /*
        private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }
     */
}
