package com.srgood.reasons.utils.audio;

import com.srgood.reasons.utils.audio.source.AudioSource;
import com.srgood.reasons.utils.audio.source.RemoteSource;
import org.json.JSONObject;
import sun.misc.IOUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by srgood on 12/28/2016.
 */
public class Playlist {
    private static final List<String> YOUTUBE_DL_PLAYLIST_ARGS = Collections.unmodifiableList(Arrays.asList("python",               //Launch python executor
            "./youtube-dl",         //youtube-dl program file
            "-q",                   //quiet. No standard out.
            "-j",                   //Print JSON
            "--flat-playlist"       //Get ONLY the urls of the playlist if this is a playlist.
    ));
    private List<AudioSource> sources;
    protected Queue<AudioSource> currentOrder;
    protected AudioSource endOfList;
    protected boolean repeatSingle = false;
    protected boolean repeatList = false;
    protected boolean shuffle = false;

    private Playlist(String name) {
        this.sources = new ArrayList<>();
    }

    public List<AudioSource> getSources() {
        return Collections.unmodifiableList(sources);
    }

    public static Playlist getPlaylist(String url,String guildId) {
        List<String> infoArgs = new LinkedList<>();
        infoArgs.addAll(YOUTUBE_DL_PLAYLIST_ARGS);
        infoArgs.add("--"); //Url separator. Deals with YT ids that start with --
        infoArgs.add(url);

        //Fire up Youtube-dl and get all sources from the provided url.
        List<AudioSource> sources = new ArrayList<>();
        Scanner scan = null;
        try {
            Process infoProcess = new ProcessBuilder().command(infoArgs).start();
            byte[] infoData = IOUtils.readFully(infoProcess.getInputStream(), -1, false);
            if (infoData == null || infoData.length == 0)
                throw new NullPointerException("The YT-DL playlist process resulted in a null or zero-length INFO!");

            String sInfo = new String(infoData);
            scan = new Scanner(sInfo);

            JSONObject source = new JSONObject(scan.nextLine());
            if (source.has("_type"))//Is a playlist
            {
                sources.add(new RemoteSource(source.getString("url"),guildId));
                while (scan.hasNextLine()) {
                    source = new JSONObject(scan.nextLine());
                    sources.add(new RemoteSource(source.getString("url"),guildId));
                }
            } else                    //Single source link
            {
                sources.add(new RemoteSource(source.getString("webpage_url"),guildId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scan != null) scan.close();
        }

        //Now that we have all the sources we can create our Playlist object.
        Playlist playlist = new Playlist("New Playlist");
        playlist.sources = sources;
        return playlist;
    }
}
