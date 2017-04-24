package com.srgood.reasons.commands.audio;

import com.srgood.reasons.utils.audio.AudioUtils;
import com.srgood.reasons.utils.audio.GuildMusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by srgood on 1/1/2017.
 */
public class CommandAudioSkip implements AudioCommand {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        GuildMusicManager musicManager = AudioCommand.init(event).getMusicManager();
        if (args.length > 0) {
            for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                AudioUtils.skipTrack(event.getGuild());
            }
            event.getChannel().sendMessage("Skipped " + args[0] + " tracks").queue();
        } else {
            AudioUtils.skipTrack(event.getChannel());
        }



    }

    @Override
    public String help() {
        return null;
    }

}
