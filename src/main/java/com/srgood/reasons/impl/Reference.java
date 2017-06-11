package com.srgood.reasons.impl;

import java.awt.Color;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Reference {
    public static final String TABLE_FLIP = "(\u256F\u00B0\u25A1\u00B0\uFF09\u256F\uFE35 \u253B\u2501\u253B";
    public static final String TABLE_UNFLIP = "\u252C\u2500\u252C\uFEFF \u30CE( \u309C-\u309C\u30CE)";
    public static final String TABLE_UNFLIP_JOKE = TABLE_UNFLIP + " \n\n Calm down!";

    public static final String[] EIGHT_BALL = {"It is certain","It is decidedly so","Without a doubt","Yes, definitely","You may rely on it","As I see it, yes","Most likely","Outlook good","Yes","Signs point to yes","Reply hazy try again","Ask again later","Better not tell you now","Cannot predict now","Concentrate and ask again","Dount count on it","My reply is no","My sources say no","Outlook not so good","Very doubtful"};
    //see http://stackoverflow.com/questions/396429/how-do-you-know-what-version-number-to-use

    public static final String VERSION = "2.2.0";
    public static final List<String> LIBRARIES = Collections.unmodifiableList(Arrays.asList(
            "JDA (https://github.com/DV8FromTheWorld/JDA)",
            "JGit (https://eclipse.org/jgit/)",
            "Google Guava (https://github.com/google/guava)"
    ));

    public static final String INVITE_LINK = "https://discordapp.com/oauth2/authorize?client_id=201810822131875840&scope=bot&permissions=0x8";

    public static final Color[] COLORS = {
            new Color(58, 96, 110),
            new Color(0, 166, 166),
            new Color(85, 214, 190),
            new Color(117, 221, 221),
            new Color(192, 245, 250)
    };

    public static final int VOTE_IMAGE_WIDTH = 800;
    public static final int VOTE_IMAGE_HEIGHT = 600;

    public static final Charset FILE_CHARSET = StandardCharsets.US_ASCII;

    public static final List<String> BOT_DEVELOPERS = java.util.Collections.unmodifiableList(Arrays.asList(
            "138048665112543233", // srgood
            "164117897025683456", // MeowingTwurtle
            "181061030799998977" // HiItsMe
    ));
}
