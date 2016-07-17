package com.srgood.dbot.commands.audio;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.srgood.dbot.Command;
import com.srgood.dbot.Main;
import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.Playlist;
import com.srgood.dbot.source.AudioInfo;
import com.srgood.dbot.source.AudioSource;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class Play implements Command {
	
	private final String help = "Used to play audio Use: " + Main.prefix + "play [URL]";
	
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
		
		
		float defvol = 0.35f;

		 String message = event.getMessage().getContent();
		 event.getMessage().deleteMessage();
	        AudioManager manager = event.getGuild().getAudioManager();
	        MusicPlayer player;
	        if (manager.getSendingHandler() == null)
	        {
	            player = new MusicPlayer();
	            player.setVolume(defvol);
	            manager.setSendingHandler(player);
	        }
	        else
	        {
	            player = (MusicPlayer) manager.getSendingHandler();
	        }

		
		 if (message.equals("!play"))
         {
             if (player.isPlaying())
             {
                 event.getChannel().sendMessage("Player is already playing!");
                 return;
             }
             else if (player.isPaused())
             {
                 player.play();
                 event.getChannel().sendMessage("Playback as been resumed.");
             }
             else
             {
                 if (player.getAudioQueue().isEmpty())
                     event.getChannel().sendMessage("The current audio queue is empty! Add something to the queue first!");
                 else
                 {
                     player.play();
                     event.getChannel().sendMessage("Player has started playing!");
                 }
             }
         }
         else if (message.startsWith("!play "))
         {
        	 
             String msg = "";
             String url = message.substring("!play ".length());
             Playlist playlist = Playlist.getPlaylist(url);
             List<AudioSource> sources = new LinkedList<AudioSource>(playlist.getSources());
//             AudioSource source = new RemoteSource(url);
//             AudioSource source = new LocalSource(new File(url));
//             AudioInfo info = source.getInfo();   //Preload the audio info.
             if (sources.size() > 1)
             {
                 event.getChannel().sendMessage("Found a playlist with **" + sources.size() + "** entries.\n" +
                         "Proceeding to gather information and queue sources. This may take some time...");
                 final MusicPlayer fPlayer = player;
                 Thread thread = new Thread()
                 {
                     @Override
                     public void run()
                     {
                         for (Iterator<AudioSource> it = sources.iterator(); it.hasNext();)
                         {
                             AudioSource source = it.next();
                             AudioInfo info = source.getInfo();
                             List<AudioSource> queue = fPlayer.getAudioQueue();
                             if (info.getError() == null)
                             {
                                 queue.add(source);
                                 if (fPlayer.isStopped())
                                     fPlayer.play();
                             }
                             else
                             {
                                 event.getChannel().sendMessage("Error detected, skipping source. Error:\n" + info.getError());
                                 it.remove();
                             }
                         }
                         event.getChannel().sendMessage("Finished queuing provided playlist. Successfully queued **" + sources.size() + "** sources");
                     }
                 };
                 thread.start();
             }
             else
             {
                 AudioSource source = sources.get(0);
                 AudioInfo info = source.getInfo();
                 if (info.getError() == null)
                 {
                     player.getAudioQueue().add(source);
                     msg += "The provided URL has been added the to queue";
                     if (player.isStopped())
                     {
                         player.play();
                         msg += " and the player has started playing";
                     }
                     event.getChannel().sendMessage(msg + ".");
                 }
                 else
                 {
                     event.getChannel().sendMessage("There was an error while loading the provided URL.\n" +
                             "Error: " + info.getError());
                 }
             }
         }
		 
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return help;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}

}