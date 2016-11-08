package com.srgood.reasons.utils;

import com.srgood.reasons.Constants;
import net.dv8tion.jda.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageUtils {

    /**
     * Sends a Message to a Channel, splitting if needed.
     *
     * @param channel The Channel to send the message into
     * @param message The message to send to the channel
     */
    public static void sendMessageSafe(MessageChannel channel, String message) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < (message.length() / Constants.Numbers.MESSAGE_LIMIT) + 1; i++) {
            int endIndex = (i + 1) * Constants.Numbers.MESSAGE_LIMIT;
            stringList.add(message.substring(i * Constants.Numbers.MESSAGE_LIMIT, endIndex > message.length() ? message.length() : endIndex));
        }

        stringList.stream().filter(s -> s.length() > 0).forEach(s -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            channel.sendMessage(s);
        });
    }

    public static void sendMessageSafeSplitOnChar(MessageChannel channel, String message, char splitOn) {
        List<String> subMessageList = new ArrayList<>();
        List<String> splitMessages = Arrays.asList(message.split("" + splitOn));

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : splitMessages) {
            if (stringBuilder.length() + s.length() + 1 > Constants.Numbers.MESSAGE_LIMIT) {
                subMessageList.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append(s).append(splitOn);
        }

        subMessageList.add(stringBuilder.toString());

        subMessageList.stream().filter(s -> s.length() > 0).forEach(s -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            channel.sendMessage(s);
        });
    }
}
