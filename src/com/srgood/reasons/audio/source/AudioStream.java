package com.srgood.reasons.audio.source;

import java.io.BufferedInputStream;
import java.util.regex.Pattern;

public abstract class AudioStream extends BufferedInputStream {
    static final Pattern TIME_PATTERN = Pattern.compile("(?<=time=).*?(?= bitrate)");

    AudioStream() {
        super(null);
    }

    public abstract AudioTimestamp getCurrentTimestamp();
}