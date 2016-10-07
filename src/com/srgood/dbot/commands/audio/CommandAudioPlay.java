package com.srgood.dbot.commands.audio;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.audio.MusicPlayer;
import com.srgood.dbot.audio.Playlist;
import com.srgood.dbot.source.AudioInfo;
import com.srgood.dbot.source.AudioSource;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.utils.SimpleLog;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.srgood.dbot.BotMain.jda;

public class CommandAudioPlay implements AudioCommand {

    private static final String HELP = "Used to play audio on this server. Use: '" + BotMain.prefix + "play [URL]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"play"};
    }

}
