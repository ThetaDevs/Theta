package com.srgood.reasons.commands.audio;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.audio.MusicPlayer;
import com.srgood.reasons.audio.source.AudioInfo;
import com.srgood.reasons.audio.source.AudioSource;
import com.srgood.reasons.audio.source.AudioTimestamp;
import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

import java.util.List;

public class CommandAudioList implements AudioCommand {


    //TODO: migrate ifs to that thingy method thats inherited.
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        AudioManager manager = event.getGuild().getAudioManager();
        MusicPlayer player = AudioCommand.initAndGetPlayer(manager);


        List<AudioSource> queue = player.getAudioQueue();
        if (queue.isEmpty()) {
            event.getChannel().sendMessage("The queue is currently empty!");
            return;
        }


        MessageBuilder builder = new MessageBuilder();
        builder.appendString("__Current Queue.  Entries: " + queue.size() + "__\n");

        //player.getCurrentAudioSource().
        builder.appendString("`[");
        AudioTimestamp dur1 = player.getCurrentAudioSource().getInfo().getDuration();
        if (dur1 == null) builder.appendString("N/A");
        else builder.appendString(dur1.getTimestamp());
        builder.appendString("]` " + player.getCurrentAudioSource().getInfo().getTitle() + "\n");

        for (int i = 0; i < queue.size() && i < 10; i++) {
            AudioInfo info = queue.get(i).getInfo();
//            builder.appendString("**(" + (i + 1) + ")** ");
            if (info == null) builder.appendString("*Could not get info for this song.*");
            else {
                AudioTimestamp duration = info.getDuration();
                builder.appendString("`[");
                if (duration == null) builder.appendString("N/A");
                else builder.appendString(duration.getTimestamp());
                builder.appendString("]` " + info.getTitle() + "\n");
            }
        }

        boolean error = false;
        int totalSeconds = 0;
        for (AudioSource source : queue) {
            AudioInfo info = source.getInfo();
            if (info == null || info.getDuration() == null) {
                error = true;
                continue;
            }
            totalSeconds += info.getDuration().getTotalSeconds();
        }

        builder.appendString("\nTotal Queue Time Length: " + AudioTimestamp.fromSeconds(totalSeconds).getTimestamp());
        if (error) builder.appendString("`An error occurred calculating total time. Might not be completely valid.");
        event.getChannel().sendMessage(builder.build());
    }

    @Override
    public String help() {
        return "Lists the current audio queue for this server. Use: '" + ReasonsMain.prefix + "list'";
    }

    @Override
    public String[] names() {
        return new String[] {"list","playlist","musiclist"};
    }

}
