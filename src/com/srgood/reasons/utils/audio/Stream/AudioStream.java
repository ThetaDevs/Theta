package com.srgood.reasons.utils.audio.Stream;

/**
 * Created by srgood on 12/28/2016.
 */
import com.srgood.reasons.utils.audio.source.AudioTimestamp;

import java.io.BufferedInputStream;
import java.util.regex.Pattern;

public abstract class AudioStream extends BufferedInputStream {
    static final Pattern TIME_PATTERN = Pattern.compile("(?<=time=).*?(?= bitrate)");

    AudioStream() {
        super(null);
    }

    public abstract AudioTimestamp getCurrentTimestamp();
}