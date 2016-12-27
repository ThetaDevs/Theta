package com.srgood.reasons.commands.Audio;

import com.srgood.reasons.commands.PermissionLevels;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.managers.AudioManager;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;

/**
 * Created by srgood on 12/26/2016.
 */
public class CommandAudioJoin implements AudioCommand {
    private AudioManager manager;


    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return args.length >= 1;
    }


    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        manager = AudioCommand.init(event).manager;

        manager.

        List<VoiceChannel> voiceChannels = event.getGuild().getVoiceChannels();





        for(VoiceChannel channel : voiceChannels) {
            if (channel.getName().toLowerCase().trim().equals(args[0])) {
                event.getGuild().getAudioManager().openAudioConnection(channel);
                event.getChannel().sendMessage("Connected to *" + channel.getName() + "*").queue();
                break;
            }
        }
    }

    @Override
    public String help() {
        return null;
    }


    @Override
    public PermissionLevels defaultPermissionLevel() {
        return null;
    }
}
