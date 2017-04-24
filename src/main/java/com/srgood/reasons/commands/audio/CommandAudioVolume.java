package com.srgood.reasons.commands.audio;

import com.srgood.reasons.commands.PermissionLevels;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by srgood on 1/1/2017.
 */
public class CommandAudioVolume implements AudioCommand {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        AudioCommand.init(event).getMusicManager().player.setVolume(Integer.parseInt(args[0]));
        event.getChannel()
             .sendMessage("Volume set to : " + args[0] + "\n")
             .queue();
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.MUSIC_DJ;
    }
}
