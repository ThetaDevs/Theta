package com.srgood.reasons.source;

public class AudioTimestamp {
    private final int hours;
    private final int minutes;
    private final int seconds;
    private final int milliseconds;

    private AudioTimestamp(int hours, int minutes, int seconds, int milliseconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    public String getTimestamp() {
        String timestamp = "";
        timestamp += hours != 0 ? String.format("%02d:", hours) : "";
        timestamp += String.format("%02d:%02d", minutes, seconds);
        return timestamp;
    }

    private String getFullTimestamp() {
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public int getTotalSeconds() {
        return (hours * 3600) + (minutes * 60) + seconds;
    }

    @Override
    public String toString() {
        return "AudioTimeStamp(" + getFullTimestamp() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AudioTimestamp)) return false;

        AudioTimestamp oTime = (AudioTimestamp) o;
        return oTime.hours == hours && oTime.minutes == minutes && oTime.seconds == seconds && oTime.milliseconds == milliseconds;
    }

    public static AudioTimestamp fromFFmpegTimestamp(String ffmpegTimestamp) {
        String[] timeParts = ffmpegTimestamp.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        timeParts = timeParts[2].split("\\.");
        int seconds = Integer.parseInt(timeParts[0]);
        int milliseconds = Integer.parseInt(timeParts[1]) * 10; //Multiply by 10 because it gives us .##, instead of .###

        return new AudioTimestamp(hours, minutes, seconds, milliseconds);
    }

    public static AudioTimestamp fromSeconds(int seconds) {
        int hours = seconds / 3600;
        seconds = seconds % 3600;

        int minutes = seconds / 60;
        seconds = seconds % 60;

        return new AudioTimestamp(hours, minutes, seconds, 0);
    }
}