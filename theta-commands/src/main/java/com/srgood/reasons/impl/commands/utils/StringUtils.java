package com.srgood.reasons.impl.commands.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    public static List<String> groupMessagesToLength(List<String> messages, int maxLength, String prefix, String suffix) {
        messages = messages.stream().map(x -> x + "\n").collect(Collectors.toList());
        List<String> outputs = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        for (String message : messages) {
            if (stringBuilder.length() + message.length() + suffix.length() > maxLength) {
                outputs.add(stringBuilder.append(suffix).toString());
                stringBuilder = new StringBuilder(prefix);
            }
            stringBuilder.append(message);
        }
        outputs.add(stringBuilder.append(suffix).toString());
        return outputs;
    }
}
