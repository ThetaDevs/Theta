package com.srgood.reasons.commands.Audio;

import com.srgood.reasons.commands.Command;
import com.srgood.reasons.utils.audio.player;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 * Created by srgood on 12/26/2016.
 */
interface AudioCommand extends Command {

    final float DEFAULT_VOLUME = 0.35f;

    static AudioContainer init(GuildMessageReceivedEvent event) {

        return new AudioContainer(event.getGuild().getAudioManager());
    }

    class AudioContainer {
        AudioManager manager;
        player player;

        public AudioContainer(AudioManager manager) {
            this.manager = manager;
            player player;

            if(manager.getSendingHandler() == null){
                player = new player();
                player.volume = DEFAULT_VOLUME;
                manager.setSendingHandler(player);
            } else player = (player) manager.getSendingHandler();

            this.player = player;
        }
    }
}
