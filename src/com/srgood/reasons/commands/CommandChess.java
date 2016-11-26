package com.srgood.reasons.commands;

import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.games.ChessGame;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by dmanl on 9/11/2016.
 */
public class CommandChess implements Command {
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length >= 1) {
            ChessGame chessGame = new ChessGame(event.getChannel());
        }
    }

    @Override
    public String help() {
        return "A WIP chess game. Use: no usage available";
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.ADMINISTRATOR;
    }
}
