package com.srgood.reasons.audio;

import com.srgood.reasons.audio.hooks.PlayerEventListener;
import com.srgood.reasons.audio.hooks.PlayerEventManager;
import com.srgood.reasons.audio.source.AudioSource;
import com.srgood.reasons.audio.source.AudioStream;
import com.srgood.reasons.audio.source.AudioTimestamp;
import net.dv8tion.jda.audio.AudioConnection;
import net.dv8tion.jda.audio.AudioSendHandler;
import net.dv8tion.jda.utils.SimpleLog;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class MusicPlayer implements AudioSendHandler {
    private static final int PCM_FRAME_SIZE = 4;
    private final PlayerEventManager eventManager = new PlayerEventManager();
    private final LinkedList<AudioSource> audioQueue = new LinkedList<>();
    private AudioSource previousAudioSource = null;
    private AudioSource currentAudioSource = null;
    private AudioStream currentAudioStream = null;
    private State state = State.STOPPED;
    private boolean shuffle = false;
    private boolean repeat = false;
    private float volume = 1.0F;

    private final byte[] buffer = new byte[AudioConnection.OPUS_FRAME_SIZE * PCM_FRAME_SIZE];

    protected enum State {
        PLAYING, PAUSED, STOPPED
    }

    public void addEventListener(PlayerEventListener listener) {
        eventManager.register(listener);
    }

    public void removeEventListener(PlayerEventListener listener) {
        eventManager.unregister(listener);
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void reload(boolean autoPlay) {
        reload0(autoPlay, true);
    }

    public void skipToNext() {
        AudioSource skipped = currentAudioSource;
        playNext(false);

        eventManager.handle(new com.srgood.reasons.audio.events.SkipEvent(this, skipped));
        if (state == State.STOPPED) eventManager.handle(new com.srgood.reasons.audio.events.FinishEvent(this));
    }

    public LinkedList<AudioSource> getAudioQueue() {
        return audioQueue;
    }

    public AudioSource getCurrentAudioSource() {
        return currentAudioSource;
    }

    public AudioSource getPreviousAudioSource() {
        return previousAudioSource;
    }

    public AudioTimestamp getCurrentTimestamp() {
        if (currentAudioStream != null) return currentAudioStream.getCurrentTimestamp();
        else return null;
    }

    public void play() {
        play0(true);
    }

    public void pause() {
        if (state == State.PAUSED) return;

        if (state == State.STOPPED) throw new IllegalStateException("Cannot pause a stopped player!");

        state = State.PAUSED;
        eventManager.handle(new com.srgood.reasons.audio.events.PauseEvent(this));
    }

    public void stop() {
        stop0(true);
    }

    public boolean isPlaying() {
        return state == State.PLAYING;
    }

    public boolean isPaused() {
        return state == State.PAUSED;
    }

    public boolean isStopped() {
        return state == State.STOPPED;
    }

    // ============ JDA AudioSendHandler overrides =============

    @Override
    public boolean canProvide() {
        return state.equals(State.PLAYING);
    }

    @Override
    public byte[] provide20MsAudio() {
//        if (currentAudioStream == null || audioFormat == null)
//            throw new IllegalStateException("The Audio source was never set for this player!\n" +
//                    "Please provide an AudioInputStream using setAudioSource.");
        try {
            int amountRead = currentAudioStream.read(buffer, 0, buffer.length);
            if (amountRead > -1) {
                if (amountRead < buffer.length) {
                    Arrays.fill(buffer, amountRead, buffer.length - 1, (byte) 0);
                }
                if (volume != 1) {
                    short sample;
                    for (int i = 0; i < buffer.length; i += 2) {
                        sample = (short) ((buffer[i + 1] & 0xff) | (buffer[i] << 8));
                        sample = (short) (sample * volume);
                        buffer[i + 1] = (byte) (sample & 0xff);
                        buffer[i] = (byte) ((sample >> 8) & 0xff);
                    }
                }
                return buffer;
            } else {
                sourceFinished();
                return null;
            }
        } catch (IOException e) {
            SimpleLog.getLog("JDA-Player").warn("A source closed unexpectedly? Oh well I guess...");
            sourceFinished();
        }
        return null;
    }

    // ========= Internal Functions ==========

    private void play0(boolean fireEvent) {
        if (state == State.PLAYING) return;

        if (currentAudioSource != null) {
            state = State.PLAYING;
            return;
        }

        if (audioQueue.isEmpty())
            throw new IllegalStateException("MusicPlayer: The audio queue is empty! Cannot start playing.");

        loadFromSource(audioQueue.removeFirst());
        state = State.PLAYING;

        if (fireEvent) eventManager.handle(new com.srgood.reasons.audio.events.PlayEvent(this));
    }

    private void stop0(boolean fireEvent) {
        if (state == State.STOPPED) return;

        state = State.STOPPED;
        try {
            currentAudioStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            previousAudioSource = currentAudioSource;
            currentAudioSource = null;
            currentAudioStream = null;
        }

        if (fireEvent) eventManager.handle(new com.srgood.reasons.audio.events.StopEvent(this));
    }

    private void reload0(boolean autoPlay, boolean fireEvent) {
        if (previousAudioSource == null && currentAudioSource == null)
            throw new IllegalStateException("Cannot restart or reload a player that has never been started!");

        stop0(false);
        loadFromSource(previousAudioSource);

        if (autoPlay) play0(false);
        if (fireEvent) eventManager.handle(new com.srgood.reasons.audio.events.ReloadEvent(this));
    }

    private void playNext(boolean fireEvent) {
        stop0(false);
        if (audioQueue.isEmpty()) {
            if (fireEvent) eventManager.handle(new com.srgood.reasons.audio.events.FinishEvent(this));
            return;
        }

        AudioSource source;
        if (shuffle) {
            Random rand = new Random();
            source = audioQueue.remove(rand.nextInt(audioQueue.size()));
        } else source = audioQueue.removeFirst();
        loadFromSource(source);

        play0(false);
        if (fireEvent) eventManager.handle(new com.srgood.reasons.audio.events.NextEvent(this));
    }

    private static final boolean AUTO_CONTINUE = true;

    private void sourceFinished() {
        if (AUTO_CONTINUE) {
            if (repeat) {
                reload0(true, false);
                eventManager.handle(new com.srgood.reasons.audio.events.RepeatEvent(this));
            } else {
                playNext(true);
            }
        } else stop0(true);
    }

    private void loadFromSource(AudioSource source) {
        AudioStream stream = source.asStream();
        currentAudioSource = source;
        currentAudioStream = stream;
    }
}
