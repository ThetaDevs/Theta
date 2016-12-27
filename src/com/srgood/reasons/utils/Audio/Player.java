package com.srgood.reasons.utils.Audio;

import net.dv8tion.jda.core.audio.AudioSendHandler;

/**
 * Created by srgood on 12/26/2016.
 */
public class Player extends AudioSendHandler {
    @Override
    public boolean canProvide() {
        return false;
    }

    @Override
    public byte[] provide20MsAudio() {
        return new byte[0];
    }


}
