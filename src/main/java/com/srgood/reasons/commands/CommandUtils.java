package com.srgood.reasons.commands;

import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUtils {

    public static boolean isCommandMessage(Message message) {
        String prefix = ConfigUtils.getGuildPrefix(message.getGuild());
        String normalMention = message.getGuild().getSelfMember().getAsMention();
        String altMention = new StringBuilder(normalMention).insert(2, '!').toString(); // Discord is weird
        return message.getRawContent().startsWith(prefix) ||
                message.getRawContent().startsWith(normalMention) ||
                message.getRawContent().startsWith(altMention);
    }

    public static String getCommandMessageArgsSection(Message message) {
        if (!isCommandMessage(message)) {
            return null;
        }
        String noPrefix = removeCommandMessagePrefix(message);
        String command = getCalledCommand(message);
        return noPrefix.substring(command.length());
    }

    public static String getCalledCommand(Message message) {
        if (!isCommandMessage(message)) {
            return null;
        }
        String noPrefix = removeCommandMessagePrefix(message).trim();
        return noPrefix.split("\\s")[0];
    }

    public static String removeCommandMessagePrefix(Message message) {
        if (!isCommandMessage(message)) {
            return null;
        }
        String basicPrefix = ConfigUtils.getGuildPrefix(message.getGuild());
        String mention = message.getGuild().getSelfMember().getAsMention();
        String altMention = new StringBuilder(mention).insert(2, '!').toString();
        String rawContent = message.getRawContent();
        String actualPrefix = rawContent.startsWith(basicPrefix) ? basicPrefix :
                rawContent.startsWith(mention) ? mention : altMention;
        return rawContent.substring(actualPrefix.length());
    }

    public static List<String> parseCommandMessageArguments(Message message) {
        if (!isCommandMessage(message)) {
            return Collections.emptyList();
        }
        String argsOnly = getCommandMessageArgsSection(message);

        boolean isEscaped = false;
        boolean inQuote = false;
        List<String> args = new ArrayList<>();
        StringBuilder currentArgBuilder = new StringBuilder();

        for (char c : argsOnly.toCharArray()) {
            if (!isEscaped) {
                if (Character.isWhitespace(c)) {
                    if (!inQuote) { // Not in a quote, so whitespace separates the args
                        String arg = currentArgBuilder.toString();
                        if (!arg.isEmpty()) {
                            args.add(arg);
                            currentArgBuilder = new StringBuilder();
                        }
                    } else { // In a quote, just append to the arg
                        currentArgBuilder.append(c);
                    }
                } else if (c == '\\') { // Next character is escaped, set the flag
                    isEscaped = true;
                } else if (c == '\"') { // Either start or end a quote. Add anything in the arg
                    String arg = currentArgBuilder.toString();
                    if (!arg.isEmpty()) {
                        args.add(arg);
                        currentArgBuilder = new StringBuilder();
                    }
                    inQuote = !inQuote;
                } else { // No special character, just append to arg
                    currentArgBuilder.append(c);
                }
            } else { // Escaped, bypass any special character behavior
                currentArgBuilder.append(c);
                isEscaped = false;
            }
        }

        // Add final arg, if any
        String arg = currentArgBuilder.toString();
        if (!arg.isEmpty()) {
            args.add(arg);
        }

        return args;
    }

}
