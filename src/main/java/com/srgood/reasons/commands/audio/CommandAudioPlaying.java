package com.srgood.reasons.commands.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by srgood on 1/2/2017.
 */
public class CommandAudioPlaying implements AudioCommand {

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        AudioTrackInfo ai = AudioCommand.init(event).getMusicManager().player.getPlayingTrack().getInfo();
        event.getChannel().sendMessage("Playing " + ai.title + " by: " + ai.author).queue();
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
    public String help() {
        return null;
    }
}
