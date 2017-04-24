package com.srgood.reasons.utils;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.entities.Channel;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Vote {

    public Vote(Map<String,Integer> choiceMap, int voteDuration, Channel channel, Runnable action) {
        final Instant begin = Instant.now();
        VoteListener voteListener = new VoteListener(channel,choiceMap);
        ReasonsMain.getJda().addEventListener(voteListener);

        Thread thread = new Thread(() -> {
            while (true) {
                if (Duration.between(begin, Instant.now()).getSeconds() >=  voteDuration) {
                    action.run();
                    ReasonsMain.getJda().removeEventListener(voteListener);
                    break;
                }
            }
        });
        thread.start();
    }
}
