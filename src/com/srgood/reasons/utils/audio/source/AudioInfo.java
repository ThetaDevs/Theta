package com.srgood.reasons.utils.audio.source;

import org.json.JSONObject;

/**
 * Created by srgood on 12/28/2016.
 */
public class AudioInfo {
    JSONObject jsonInfo;
    String title;
    String origin;
    String id;
    String encoding;
    String description;
    String extractor;
    String thumbnail;
    String error;
    boolean isLive;
    AudioTimestamp duration;

    public JSONObject getJsonInfo() {
        return jsonInfo;
    }

    public String getTitle() {
        return title;
    }

    public String getOrigin() {
        return origin;
    }

    public String getId() {
        return id;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getDescription() {
        return description;
    }

    public String getExtractor() {
        return extractor;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getError() {
        return error;
    }

    public boolean isLive() {
        return isLive;
    }

    public AudioTimestamp getDuration() {
        return duration;
    }
}
