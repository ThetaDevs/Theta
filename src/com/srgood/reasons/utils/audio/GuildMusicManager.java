package com.srgood.reasons.utils.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 * Created by srgood on 1/1/2017.
 */
public class GuildMusicManager {
    /**
     * Audio AudioPlayerSendHandler for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the AudioPlayerSendHandler.
     */
    public final TrackScheduler scheduler;

    /**
     * Creates a AudioPlayerSendHandler and a track scheduler.
     * @param manager Audio AudioPlayerSendHandler manager to use for creating the AudioPlayerSendHandler.
     */
    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}