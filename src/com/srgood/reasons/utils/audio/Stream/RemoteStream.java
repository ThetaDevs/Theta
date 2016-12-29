package com.srgood.reasons.utils.audio.Stream;

import com.srgood.reasons.utils.audio.source.AudioTimestamp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by srgood on 12/28/2016.
 */

public class RemoteStream extends AudioStream
{
    //Represent the processes that control the Python Youtube-dl and the FFmpeg program.
    private Process ytdlProcess;
    private Process ffmpegProcess;

    //Async threads that deal with the piping of the outputs of the processes.
    private Thread ytdlToFFmpegThread;
    private Thread ytdlErrGobler;
    private Thread ffmpegErrGobler;

    private List<String> ytdlLaunchArgs;
    private List<String> ffmpegLaunchArgs;
    private AudioTimestamp timestamp = AudioTimestamp.fromSeconds(0);

    public RemoteStream(List<String> ytdlLaunchArgs, List<String> ffmpegLaunchArgs, String guildId)
    {
        try
        {
            File directory = new File("cache/" + guildId);
            if(!directory.exists()) {
                directory.mkdirs();
            } else {
                directory.delete();
                directory.mkdirs();
            }
            ProcessBuilder pBuilder = new ProcessBuilder();
            pBuilder.directory(directory);
            pBuilder.command(ytdlLaunchArgs);
            ytdlProcess = pBuilder.start();

            pBuilder.command(ffmpegLaunchArgs);
            ffmpegProcess = pBuilder.start();

            final Process ytdlProcessF = ytdlProcess;
            final Process ffmpegProcessF = ffmpegProcess;

            ytdlToFFmpegThread = new Thread("RemoteSource ytdlToFFmpeg Bridge")
            {
                @Override
                public void run()
                {
                    InputStream fromYTDL = null;
                    OutputStream toFFmpeg = null;
                    try
                    {
                        fromYTDL = ytdlProcessF.getInputStream();
                        toFFmpeg = ffmpegProcessF.getOutputStream();

                        byte[] buffer = new byte[1024];
                        int amountRead = -1;
                        while (!isInterrupted() && ((amountRead = fromYTDL.read(buffer)) > -1))
                        {
                            toFFmpeg.write(buffer, 0, amountRead);
                        }
                        toFFmpeg.flush();
                    }
                    catch (IOException e)
                    {

                    }
                    finally
                    {
                        try
                        {
                            if (fromYTDL != null)
                                fromYTDL.close();
                        }
                        catch (Throwable e) {}
                        try
                        {
                            if (toFFmpeg != null)
                                toFFmpeg.close();
                        }
                        catch (Throwable e) {}
                    }
                }
            };

            ytdlErrGobler = new Thread("RemoteStream ytdlErrGobler")
            {
                @Override
                public void run()
                {
                    InputStream fromYTDL = null;
                    try
                    {
                        fromYTDL = ytdlProcessF.getErrorStream();

                        byte[] buffer = new byte[1024];
                        int amountRead = -1;
                        while (!isInterrupted() && ((amountRead = fromYTDL.read(buffer)) > -1))
                        {

                        }
                    }
                    catch (IOException e)
                    {

                    }
                    finally
                    {
                        try
                        {
                            if (fromYTDL != null)
                                fromYTDL.close();
                        }
                        catch (Throwable ignored) {}
                    }
                }
            };

            ffmpegErrGobler = new Thread("RemoteStream ffmpegErrGobler")
            {
                @Override
                public void run()
                {
                    InputStream fromFFmpeg = null;
                    try
                    {
                        fromFFmpeg = ffmpegProcessF.getErrorStream();



                        byte[] buffer = new byte[1024];
                        int amountRead = -1;
                        while (!isInterrupted() && ((amountRead = fromFFmpeg.read(buffer)) > -1))
                        {
                            String info = new String(Arrays.copyOf(buffer, amountRead));
                            if (info.contains("time="))
                            {
                                Matcher m = TIME_PATTERN.matcher(info);
                                if (m.find())
                                {
                                    timestamp = AudioTimestamp.fromFFmpegTimestamp(m.group());
                                }
                            }
                        }
                    }
                    catch (IOException e)
                    {

                    }
                    finally
                    {
                        try
                        {
                            if (fromFFmpeg != null)
                                fromFFmpeg.close();
                        }
                        catch (Throwable ignored) {}
                    }
                }
            };

            ytdlToFFmpegThread.start();
            ytdlErrGobler.start();
            ffmpegErrGobler.start();
            this.in = ffmpegProcess.getInputStream();
        }
        catch (IOException e)
        {

            try
            {
                close();
            }
            catch (IOException e1)
            {

            }
        }
    }

    @Override
    public AudioTimestamp getCurrentTimestamp()
    {
        return timestamp;
    }

    @Override
    public void close() throws IOException
    {
        try
        {
            if (in != null)
            {
                in.close();
                in = null;
            }
        }
        catch (Throwable ignored) {}
        try
        {
            if (ytdlToFFmpegThread != null)
            {
                ytdlToFFmpegThread.interrupt();
                ytdlToFFmpegThread = null;
            }
        }
        catch (Throwable ignored) {}
        try
        {
            if (ytdlErrGobler != null)
            {
                ytdlErrGobler.interrupt();
                ytdlErrGobler = null;
            }
        }
        catch (Throwable ignored) {}
        try
        {
            if (ffmpegErrGobler != null)
            {
                ffmpegErrGobler.interrupt();
                ffmpegErrGobler = null;
            }
        }
        catch (Throwable ignored) {}
        try
        {
            if (ffmpegProcess != null)
            {
                ffmpegProcess.destroyForcibly();
                ffmpegProcess = null;
            }
        }
        catch (Throwable ignored) {}
        try
        {
            if (ytdlProcess != null)
            {
                ytdlProcess.destroyForcibly();
                ytdlProcess = null;
            }
        }
        catch (Throwable ignored) {}
        try
        {
            super.close();
        }
        catch (Throwable ignored) {}
    }
}