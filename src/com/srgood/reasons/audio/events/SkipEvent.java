package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;
import com.srgood.reasons.audio.source.AudioSource;

public class SkipEvent extends PlayerEvent {
    private final AudioSource skippedSource;

    public SkipEvent(MusicPlayer player, AudioSource skippedSource) {
        super(player);
        this.skippedSource = skippedSource;
    }

    public AudioSource getSkippedSource() {
        return skippedSource;
    }
}
