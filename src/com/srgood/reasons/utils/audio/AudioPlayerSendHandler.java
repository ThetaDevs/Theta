package com.srgood.reasons.utils.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;


/**
 * Created by srgood on 12/26/2016.
 */
public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    /**
     * @param audioPlayer Audio AudioPlayerSendHandler to wrap.
     */
    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean canProvide() {
        if (lastFrame == null) {
            lastFrame = audioPlayer.provide();
        }

        return lastFrame != null;
    }

    @Override
    public byte[] provide20MsAudio() {
        if (lastFrame == null) {
            lastFrame = audioPlayer.provide();
        }

        byte[] data = lastFrame != null ? lastFrame.data : null;
        lastFrame = null;

        return data;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
