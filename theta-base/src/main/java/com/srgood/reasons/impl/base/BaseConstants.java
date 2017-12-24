package com.srgood.reasons.impl.base;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BaseConstants {
    public static final String TABLE_FLIP = "(\u256F\u00B0\u25A1\u00B0\uFF09\u256F\uFE35 \u253B\u2501\u253B";
    public static final String TABLE_UNFLIP = "\u252C\u2500\u252C\uFEFF \u30CE( \u309C-\u309C\u30CE)";
    public static final String TABLE_UNFLIP_JOKE = TABLE_UNFLIP + " \n\n Calm down!";

    //see http://stackoverflow.com/questions/396429/how-do-you-know-what-version-number-to-use

    public static final Charset FILE_CHARSET = StandardCharsets.US_ASCII;

    public static final List<String> BOT_DEVELOPERS = Collections.unmodifiableList(Arrays.asList("138048665112543233", // srgood
            "164117897025683456", // MeowingTwurtle
            "181061030799998977")); // HiItsMe

    public static final Random GLOBAL_RANDOM = new Random();
}
