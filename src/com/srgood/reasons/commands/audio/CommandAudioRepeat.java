package com.srgood.reasons.commands.audio;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.audio.MusicPlayer;
import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioRepeat implements AudioCommand {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {

        AudioManager manager = event.getGuild().getAudioManager();
        MusicPlayer player = AudioCommand.initAndGetPlayer(manager);

        if (player.isRepeat()) {
            player.setRepeat(false);
            event.getChannel().sendMessage("The player has been set to **not** repeat.");
        } else {
            player.setRepeat(true);
            event.getChannel().sendMessage("The player been set to repeat.");
        }
    }

    @Override
    public String help() {
        return "Used to repeat the audio that is playing on this server. Use: '" + ReasonsMain.prefix + "pause'";
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        
        return PermissionLevels.MUSIC_DJ;
    }



}
