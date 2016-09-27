package com.srgood.dbot.utils;

import com.srgood.dbot.BotMain;
import net.dv8tion.jda.entities.Channel;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * Created by dmanl on 9/25/2016.
 */
public class Vote {

    public Vote(Map<String,Integer> choiceMap, int voteDuration, Channel channel, Runnable action) {
        final Instant begin = Instant.now();
        VoteListener voteListener = new VoteListener(channel,choiceMap);
        BotMain.jda.addEventListener(voteListener);

        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (Duration.between(begin, Instant.now()).getSeconds() >=  voteDuration) {
                        action.run();
                        BotMain.jda.removeEventListener(voteListener);
                        break;
                    }
                }
            }
        };
        thread.run();
    }
}
