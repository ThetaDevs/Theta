package com.srgood.reasons.commands.audio;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.audio.MusicPlayer;
import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioVolume implements AudioCommand {

    public void action(String[] args, GuildMessageReceivedEvent event) {
        AudioManager manager = event.getGuild().getAudioManager();
        MusicPlayer player = AudioCommand.initAndGetPlayer(manager);

        try {
            float volume = Float.parseFloat(args[0]);
            volume = Math.min(100000F, Math.max(0F, volume));
            player.setVolume(volume / 100);
            event.getChannel().sendMessage("Volume was changed to: " + volume);
        } catch (Exception e) {
            event.getChannel().sendMessage("Volume is: " + player.getVolume() * 100);
        }
    }

    public String help() {
        return "Used to get or set the audio volume on this server. No argument will get, one argument will set. Use: '" + ReasonsMain.prefix + "volume [0-100]'";
    }

    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.MUSIC_DJ;
    }

    public String[] names() {
        return new String[] { "volume","vol" };
    }

}
