package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.games.TicTacToeGame;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by HiItsMe on 11/28/2016.
 */
public class CommandTicTacToe implements Command {

    private static final String HELP = "Starts a game of Tic Tac Toe. Use: '" + ReasonsMain.prefix + "tictactoe'\n\tUse '" + ReasonsMain.prefix + "tictactoe <x> <y> to place an X";
    static HashMap<TextChannel, TicTacToeGame> tictactoe = new HashMap<>();
    
    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        boolean o = true;
        if(args.length == 0) {} else if(args.length == 2) {
            try {
                int n = Integer.parseInt(args[0]);
                n = Integer.parseInt(args[1]);
            } catch(Exception e) {
                o = false;
            }
        } else {
            o = false;
        }
        if(!o)
            event.getChannel().sendMessage("Invalid arguments").queue();
        return o;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if(args.length == 2) {
            if(tictactoe.containsKey(event.getChannel())) {
                tictactoe.get(event.getChannel()).checkWin();
                if(tictactoe.get(event.getChannel()).dead) {
                    tictactoe.remove(event.getChannel());
                    event.getChannel().sendMessage("No game currently in this channel").queue();
                } else {
                    int ex = Integer.parseInt(args[0]);
                    int why = Integer.parseInt(args[1]);
                    tictactoe.get(event.getChannel()).play(ex, why);
                    tictactoe.get(event.getChannel()).checkWin();
                    if(!tictactoe.get(event.getChannel()).dead) {
                        tictactoe.get(event.getChannel()).AIplay();
                        tictactoe.get(event.getChannel()).checkWin();
                        tictactoe.get(event.getChannel()).drawBoard();
                        if(tictactoe.get(event.getChannel()).dead)
                            tictactoe.remove(event.getChannel());
                    } else {
                        tictactoe.get(event.getChannel()).drawBoard();
                        tictactoe.remove(event.getChannel());
                    }       
                }
            } else {
                event.getChannel().sendMessage("No game currently in this channel").queue();
            }
        } else {
            if(tictactoe.containsKey(event.getChannel())) {
                event.getChannel().sendMessage("There is already a game in this channel");
            } else {
                tictactoe.put(event.getChannel(), new TicTacToeGame(event.getChannel()));
            }
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {}

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
        return new String[] {"tictactoe"};
    }

}
