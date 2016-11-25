package com.srgood.reasons.commands.audio;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.audio.MusicPlayer;
import com.srgood.reasons.audio.Playlist;
import com.srgood.reasons.audio.source.AudioInfo;
import com.srgood.reasons.audio.source.AudioSource;
import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.utils.SimpleLog;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.srgood.reasons.ReasonsMain.jda;

public class CommandAudioPlay implements AudioCommand {

    private static final String HELP = "Used to play audio on this server. Use: '" + ReasonsMain.prefix + "play [URL]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendTyping();
        AudioManager manager = event.getGuild().getAudioManager();
        MusicPlayer player = AudioCommand.initAndGetPlayer(manager);


        if (event.getChannel()
                .checkPermission(jda.getSelfInfo(), Permission.MESSAGE_MANAGE)) {
            event.getMessage().deleteMessage();
        }

        if (args.length == 0) {
            if (player.isPlaying()) {
                event.getChannel().sendMessage("Audio is already playing!");
            } else if (player.isPaused()) {
                player.play();
                event.getChannel().sendMessage("The audio has been resumed.");
            } else {
                if (player.getAudioQueue().isEmpty())
                    event.getChannel().sendMessage("The audio playlist is currently empty, add something to it before you play.");
                else {
                    player.play();
                    event.getChannel().sendMessage("Audio is now playing.");
                }
            }
        } else if (args.length > 0) {
            String url = event.getMessage().getContent().replace(ConfigUtils.getGuildPrefix(event.getGuild())+ "play ","");
            StringBuilder msg = new StringBuilder();

            Playlist playlist = Playlist.getPlaylist(url);
            List<AudioSource> sources = new LinkedList<>(playlist.getSources());
            if (sources.size() > 1) {
                event.getChannel().sendMessage("You've queued a playlist with **" + sources.size() + "** videos. This may take time to load.");
                final MusicPlayer fPlayer = player;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        for (Iterator<AudioSource> it = sources.iterator(); it.hasNext(); ) {
                            AudioSource source = it.next();
                            AudioInfo info = source.getInfo();
                            List<AudioSource> queue = fPlayer.getAudioQueue();
                            if (info.getError() == null) {
                                queue.add(source);
                                if (fPlayer.isStopped()) fPlayer.play();
                            } else {
                                event.getChannel().sendMessage("An error occurred");
                                SimpleLog.getLog("Reasons").warn("Audio error: " + info.getError());
                                it.remove();
                            }
                        }
                        event.getChannel().sendMessage("Finished adding all " + sources.size() + " videos to the playlist!");
                    }
                };
                thread.start();
            } else if (sources.size() == 1) {
                AudioSource source = sources.get(0);
                AudioInfo info = source.getInfo();
                if (info.getError() == null) {
                    player.getAudioQueue().add(source);
                    msg.append("The following video has been added");
                    if (player.isStopped()) {
                        player.play();
                        msg.append(" and has started playing");
                    }
                    event.getChannel().sendMessage(msg.toString());
                } else {
                    event.getChannel().sendMessage("An error occurred");
                    SimpleLog.getLog("Reasons").warn("Audio error: " + info.getError());
                }
            }
        }

    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        

    }

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        
        return PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"play"};
    }

}
