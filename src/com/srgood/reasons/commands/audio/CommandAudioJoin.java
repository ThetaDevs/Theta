package com.srgood.reasons.commands.audio;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.utils.SimpleLog;

public class CommandAudioJoin implements AudioCommand {

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        AudioManager manager = event.getGuild().getAudioManager();

        VoiceChannel channel = null;

        StringBuilder chanName = new StringBuilder();

        for (int i = 0; i < args.length; i++) {

            chanName.append(args[i]);

            if (i < args.length - 1) {
                chanName.append(" ");
            }
        }

        if (chanName.toString().equals("")) {
            for (VoiceChannel i : event.getGuild().getVoiceChannels()) {
                if (i.getUsers().contains(event.getAuthor())) {
                    channel = i;
                }
            }
        } else {
            channel = event.getGuild().getVoiceChannels().stream().filter(vChan -> vChan.getName().equalsIgnoreCase(chanName.toString())).findFirst().orElse(null);
        }

        if (channel == null) {
            event.getChannel().sendMessage("I couldn't find a channel called: '" + chanName.toString() + "'");
            SimpleLog.getLog("Reasons").debug("Joined: " + chanName.toString());
            return;
        }
        try {
            manager.openAudioConnection(channel);
        } catch (IllegalStateException e){
            event.getChannel().sendMessage("Reasons is already connected to an audio channel");
        }
    }

    @Override
    public String help() {
        return "Adds Reasons to a voice channel. Use: '" + ReasonsMain.prefix + "join [channel name]' Default is the channel you are currently in.";
    }

    @Override
    public String[] names() {
        return new String[] {"join","joinchan","joinchannel"};
    }

}
