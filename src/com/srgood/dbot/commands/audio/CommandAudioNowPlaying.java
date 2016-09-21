package com.srgood.dbot.commands.audio;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.audio.MusicPlayer;
import com.srgood.dbot.source.AudioInfo;
import com.srgood.dbot.source.AudioTimestamp;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioNowPlaying implements AudioCommand {

    private final String help = "Displays information about the song that is playing on this server. Use: '" + BotMain.prefix + "now-playing'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        AudioManager manager = event.getGuild().getAudioManager();
        MusicPlayer player = AudioCommand.initAndGetPlayer(manager);


        if (player.isPlaying()) {
            AudioTimestamp currentTime = player.getCurrentTimestamp();
            AudioInfo info = player.getCurrentAudioSource().getInfo();
            if (info.getError() == null) {
                event.getChannel().sendMessage("**Playing:** " + info.getTitle() + "\n" + "**Time:**    [" + currentTime.getTimestamp() + " / " + info.getDuration().getTimestamp() + "]");
            } else {
                event.getChannel().sendMessage("**Playing:** Info Error. Known source: " + player.getCurrentAudioSource().getSource() + "\n" + "**Time:**    [" + currentTime.getTimestamp() + " / (N/A)]");
            }
        } else {
            event.getChannel().sendMessage("The player is not currently playing anything!");
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        return help;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"nowplaying", "now-playing"};
    }

}
