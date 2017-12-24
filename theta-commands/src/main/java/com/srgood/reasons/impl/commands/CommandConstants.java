package com.srgood.reasons.impl.commands;

import com.srgood.reasons.impl.commands.utils.GitUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;

public class CommandConstants {
    public static final String[] EIGHT_BALL = { "It is certain", "It is decidedly so", "Without a doubt", "Yes, definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Dount count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful" };

    public static final Color[] COLORS = { new Color(58, 96, 110), new Color(0, 166, 166), new Color(85, 214, 190), new Color(117, 221, 221), new Color(192, 245, 250) };

    public static final int VOTE_IMAGE_WIDTH = 800;
    public static final int VOTE_IMAGE_HEIGHT = 600;

    public static final Supplier<String> VERSION_SUPPLIER = () -> GitUtils.getCurrentRevision()
                                                                          .orElse("unknown (not in a Git repository)");
    public static final java.util.List<String> LIBRARIES = Collections.unmodifiableList(Arrays.asList("JDA (https://github.com/DV8FromTheWorld/JDA)", "JGit (https://eclipse.org/jgit/)", "Google Guava (https://github.com/google/guava)"));
}
