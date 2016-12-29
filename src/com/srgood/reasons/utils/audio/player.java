package com.srgood.reasons.utils.audio;

import com.srgood.reasons.utils.audio.Stream.AudioStream;
import com.srgood.reasons.utils.audio.source.AudioSource;

import net.dv8tion.jda.core.audio.AudioConnection;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.utils.SimpleLog;


import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by srgood on 12/26/2016.
 */
public class player implements AudioSendHandler {
    private final boolean AUTO_CONTINUE = true;
    private static final int PCM_FRAME_SIZE = 4;
    public final LinkedList<AudioSource> audioQueue = new LinkedList<>();
    private AudioSource previousAudioSource = null;
    private AudioSource currentAudioSource = null;
    private AudioStream currentAudioStream = null;
    private State currentState = State.STOPPED;
    public boolean shuffle = false;
    public boolean repeat = false;
    public float volume = 1.0F;

    private final byte[] buffer = new byte[AudioConnection.OPUS_FRAME_SIZE * PCM_FRAME_SIZE];

    private enum State {
        PLAYING, PAUSED, STOPPED
    }

    public State getCurrentState() {
        return this.currentState;
    }

    private void loadAudio(AudioSource audioSource) {
        AudioStream stream = audioSource.asStream();
        currentAudioSource = audioSource;
        currentAudioStream = stream;
    }

    private void sourceFinished() {



        if(AUTO_CONTINUE) {
            if (repeat) {
                reload(false);
            } else {
                playNext();
            }
        } else {
            stop();
        }
    }

    public void skipToNext() {
        AudioSource skipped = currentAudioSource;
        playNext();
    }

    private void reload(boolean autoPlay) {
        if (previousAudioSource == null && currentAudioSource == null)
            throw new IllegalStateException("Cannot restart or reload a player that has never been started!");

        stop();
        loadAudio(previousAudioSource);

        if (autoPlay) play();
    }

    public void play() {
        if (currentState == State.PLAYING) return;

        if (currentAudioSource != null) {
            currentState = State.PLAYING;
            return;
        }

        if (audioQueue.isEmpty())
            throw new IllegalStateException("MusicPlayer: The audio queue is empty! Cannot start playing.");

        loadAudio(audioQueue.removeFirst());
        currentState = State.PLAYING;
    }

    private void playNext() {
        stop();

        AudioSource source;
        if (shuffle) {
            Random rand = new Random();
            source = audioQueue.remove(rand.nextInt(audioQueue.size()));
        } else source = audioQueue.removeFirst();
        loadAudio(source);

        play();
    }

    private void stop() {
        if (currentState == State.STOPPED) return;
        currentState = State.STOPPED;

        try {
            currentAudioStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            previousAudioSource = currentAudioSource;
            currentAudioSource = null;
            currentAudioStream = null;
        }
    }

    @Override
    public boolean canProvide() {
        return currentState.equals(State.PLAYING);
    }

    @Override
    public byte[] provide20MsAudio() {
        if (currentAudioStream == null) throw new IllegalStateException("The Audio source was never set for this player!\n" + "Please provide an AudioInputStream using setAudioSource.");
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

}
