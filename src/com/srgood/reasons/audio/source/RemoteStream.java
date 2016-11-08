package com.srgood.reasons.audio.source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class RemoteStream extends AudioStream {
    //Represent the processes that control the Python Youtube-dl and the FFmpeg program.
    private Process ytdlProcess;
    private Process ffmpegProcess;

    //Async threads that deal with the piping of the outputs of the processes.
    private Thread ytdlToFFmpegThread;
    private Thread ytdlErrGobler;
    private Thread ffmpegErrGobler;

    @SuppressWarnings("unused")
    private List<String> ytdlLaunchArgs;

    @SuppressWarnings("unused")
    private List<String> ffmpegLaunchArgs;
    private AudioTimestamp timestamp = AudioTimestamp.fromSeconds(0);

    RemoteStream(List<String> ytdlLaunchArgs, List<String> ffmpegLaunchArgs) {
        try {
            ProcessBuilder pBuilder = new ProcessBuilder();

            pBuilder.command(ytdlLaunchArgs);
            System.out.println("Command: " + pBuilder.command());
            ytdlProcess = pBuilder.start();

            pBuilder.command(ffmpegLaunchArgs);
            System.out.println("Command: " + pBuilder.command());
            ffmpegProcess = pBuilder.start();

            final Process ytdlProcessF = ytdlProcess;
            final Process ffmpegProcessF = ffmpegProcess;

            ytdlToFFmpegThread = new Thread("RemoteSource ytdlToFFmpeg Bridge") {
                @Override
                public void run() {
                    InputStream fromYTDL = null;
                    OutputStream toFFmpeg = null;
                    try {
                        fromYTDL = ytdlProcessF.getInputStream();
                        toFFmpeg = ffmpegProcessF.getOutputStream();

                        byte[] buffer = new byte[1024];
                        int amountRead;
                        while (!isInterrupted() && ((amountRead = fromYTDL.read(buffer)) > -1)) {
                            toFFmpeg.write(buffer, 0, amountRead);
                        }
                        toFFmpeg.flush();
                    } catch (IOException e) {
                        //If the pipe being closed caused this problem, it was because it tried to write when it closed.
                        if (!e.getMessage().contains("The pipe has been ended")) e.printStackTrace();
                    } finally {
                        try {
                            if (fromYTDL != null) fromYTDL.close();
                        } catch (IOException ignored) {
                        }
                        try {
                            if (toFFmpeg != null) toFFmpeg.close();
                        } catch (IOException ignored) {
                        }
                    }
                }
            };

            ytdlErrGobler = new Thread("RemoteStream ytdlErrGobler") {
                @Override
                public void run() {

                    try {
                        InputStream fromYTDL;

                        fromYTDL = ytdlProcessF.getErrorStream();
                        if (fromYTDL == null) System.out.println("fromYTDL is null");

                        byte[] buffer = new byte[1024];
                        int amountRead;
                        while (!isInterrupted() && ((amountRead = fromYTDL.read(buffer)) > -1)) {
                            System.out.println("ERR YTDL: " + new String(Arrays.copyOf(buffer, amountRead)));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            ffmpegErrGobler = new Thread("RemoteStream ffmpegErrGobler") {
                @Override
                public void run() {
                    try {
                        InputStream fromFFmpeg;

                        fromFFmpeg = ffmpegProcessF.getErrorStream();
                        if (fromFFmpeg == null) System.out.println("fromYTDL is null");

                        byte[] buffer = new byte[1024];
                        int amountRead;
                        while (!isInterrupted() && ((amountRead = fromFFmpeg.read(buffer)) > -1)) {
                            String info = new String(Arrays.copyOf(buffer, amountRead));
                            if (info.contains("time=")) {
                                Matcher m = TIME_PATTERN.matcher(info);
                                if (m.find()) {
                                    timestamp = AudioTimestamp.fromFFmpegTimestamp(m.group());
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            ytdlToFFmpegThread.start();
            ytdlErrGobler.start();
            ffmpegErrGobler.start();
            this.in = ffmpegProcess.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public AudioTimestamp getCurrentTimestamp() {
        return timestamp;
    }

    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
            in = null;
        }
        if (ytdlToFFmpegThread != null) {
            ytdlToFFmpegThread.interrupt();
            ytdlToFFmpegThread = null;
        }
        if (ytdlErrGobler != null) {
            ytdlErrGobler.interrupt();
            ytdlErrGobler = null;
        }
        if (ffmpegErrGobler != null) {
            ffmpegErrGobler.interrupt();
            ffmpegErrGobler = null;
        }
        if (ffmpegProcess != null) {
            ffmpegProcess.destroy();
            ffmpegProcess = null;
        }
        if (ytdlProcess != null) {
            ytdlProcess.destroy();
            ytdlProcess = null;
        }
        super.close();
    }
}