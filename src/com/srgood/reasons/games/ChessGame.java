package com.srgood.reasons.games;


import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmanl on 9/11/2016.
 */
public class ChessGame {
    private Message board = null;
    private boolean ready = false;
    private Map pieces = new HashMap();

    public ChessGame(MessageChannel channel) {
        channel.sendMessage("**PREPPING CHESS GAME**").queue(m -> {
            board = m;
            ready = true;
        });
    }


}
