package com.srgood.reasons.commands.audio;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.List;

/**
 * Created by srgood on 12/26/2016.
 */
public class CommandAudioJoin implements AudioCommand {
    private AudioManager manager;

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        manager = AudioCommand.init(event).getAudioManager();

        List<VoiceChannel> voiceChannels = event.getGuild().getVoiceChannels();

        if (args.length > 0) {
            for (VoiceChannel channel : voiceChannels) {
                if (channel.getName().toLowerCase().trim().equals(args[0])) {
                    event.getGuild().getAudioManager().openAudioConnection(channel);
                    event.getChannel().sendMessage("Connected to *" + channel.getName() + "*").queue();
                    break;
                }
            }
        } else {
            for (VoiceChannel channel : voiceChannels) {
                if (channel.getMembers().contains(event.getMember())) {
                    event.getGuild().getAudioManager().openAudioConnection(channel);
                    event.getChannel().sendMessage("Connected to *" + channel.getName() + "*").queue();
                    break;
                }
            }
        }
    }

    @Override
    public String help() {
        return null;
    }
}
