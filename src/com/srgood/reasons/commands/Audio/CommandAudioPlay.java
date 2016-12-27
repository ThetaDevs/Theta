package com.srgood.reasons.commands.Audio;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * Created by srgood on 12/26/2016.
 */
public class CommandAudioPlay implements AudioCommand {

    private UrlValidator validator = new UrlValidator();

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return args.length >= 1 && validator.isValid(args[0]);
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {

    }

    @Override
    public String help() {
        return null;
    }
}
