package com.srgood.reasons.commands.audio;


import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.validator.routines.UrlValidator;

import static com.srgood.reasons.utils.audio.AudioUtils.loadAndPlay;

/**
 * Created by srgood on 12/26/2016.
 */
public class CommandAudioPlay implements AudioCommand {

    private UrlValidator validator = new UrlValidator();

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        AudioContainer container = AudioCommand.init(event);

        if (args.length > 0) {
            loadAndPlay(event.getChannel(), event.getMessage()
                                                 .getContent()
                                                 .replace(ConfigUtils.getGuildPrefix(event.getGuild()) + "play", "")
                                                 .trim());
        } else {
            container.getMusicManager().player.setPaused(false);
        }
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

    @Override
    public String help() {
        return null;
    }
}
