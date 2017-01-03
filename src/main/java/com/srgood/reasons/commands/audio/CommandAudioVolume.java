package com.srgood.reasons.commands.audio;

import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by srgood on 1/1/2017.
 */
public class CommandAudioVolume implements AudioCommand {

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        AudioCommand.init(event).getMusicManager().player.setVolume(Integer.parseInt(args[0]));
        event.getChannel()
             .sendMessage("Volume set to : " + args[0] + "\n" + "Note to self... add a fancy volume meter here[------0000]")
             .queue();
    }

    @Override
    public String help() {
        return null;
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
        return PermissionLevels.MUSIC_DJ;
    }
}
