package com.srgood.reasons.commands;

import com.srgood.reasons.utils.config.ConfigUtils;
import com.srgood.reasons.games.ChessGame;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Created by dmanl on 9/11/2016.
 */
public class CommandChess implements Command {
    private static final String HELP = "A WIP chess game. Use: no usage available";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        if (args.length >= 1) {
            ChessGame chessGame = new ChessGame(event.getChannel().sendMessage("").block());
        }
    }

    @Override
    public String help() {
        return HELP;
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
    public String[] names() {
        return new String[] {"chess"};
    }
}
