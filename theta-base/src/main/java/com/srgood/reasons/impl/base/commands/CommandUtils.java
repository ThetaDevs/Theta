package com.srgood.reasons.impl.base.commands;

import com.srgood.reasons.config.GuildConfigManager;
import net.dv8tion.jda.core.entities.Guild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandUtils {

    public static String[] generatePossiblePrefixesForGuild(GuildConfigManager guildConfigManager, Guild guild) {
        return new String[] { guildConfigManager.getPrefix(), guild.getSelfMember().getAsMention() };
    }

    public static boolean isCommandMessage(String content, String... potentialPrefixes) {
        return Arrays.stream(potentialPrefixes).anyMatch(content::startsWith);
    }

    public static String getCommandMessageArgsSection(String content, String... potentialPrefixes) {
        if (!isCommandMessage(content, potentialPrefixes)) {
            return null;
        }
        String noPrefix = removeCommandMessagePrefix(content, potentialPrefixes);
        String command = getCalledCommand(content, potentialPrefixes);
        return noPrefix.substring(command.length());
    }

    public static String getCalledCommand(String content, String... potentialPrefixes) {
        if (!isCommandMessage(content, potentialPrefixes)) {
            return null;
        }
        String noPrefix = removeCommandMessagePrefix(content, potentialPrefixes).trim();
        return noPrefix.split("\\s")[0];
    }

    public static String removeCommandMessagePrefix(String content, String... potentialPrefixes) {
        if (!isCommandMessage(content, potentialPrefixes)) {
            return null;
        }

        for (String prefix : potentialPrefixes) {
            if (content.startsWith(prefix)) {
                return content.substring(prefix.length()).trim();
            }
        }


        // Just in case the laws of physics are rewritten
        return null;
    }

    public static List<String> parseCommandMessageArguments(String content, String... potentialPrefixes) {
        if (!isCommandMessage(content, potentialPrefixes)) {
            return Collections.emptyList();
        }
        String argsOnly = getCommandMessageArgsSection(content, potentialPrefixes);

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
