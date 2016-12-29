package com.srgood.reasons;

import java.awt.*;

public class Reference {

    public static class Strings {

        public static final String TABLE_FLIP = "(\u256F\u00B0\u25A1\u00B0\uFF09\u256F\uFE35 \u253B\u2501\u253B";
        private static final String TABLE_UNFLIP = "\u252C\u2500\u252C\uFEFF \u30CE( \u309C-\u309C\u30CE)";
        public static final String TABLE_UNFLIP_JOKE = TABLE_UNFLIP + " \n\n Calm down!";

        public static final String BOT_TOKEN_REASONS = "MjAxODEwODM4MDcwMzYyMTEy.CnW-4w.-U2KZFTgfzXFd1HNoC_yYjF426s";
        public static final String BOT_TOKEN_REASONS_DEV_1 = "MjA3MjIyNzY5MjA3NzM4MzY4.Cnf-vg.UNECSqJIHUX_9sXL-98W27XUlIU";
        public static final String BOT_TOKEN_REASONS_DEV_2 = "MjA3MjIzMTU4OTAwNjU0MDgw.Cnf_Gw.HKBXiCldwx57cN_1uHt6XhyNEwY";
        public static final Color[] COLORS = {new Color(58, 96, 110),new Color(0, 166, 166),new Color(85, 214, 190),new Color(117, 221, 221),new Color(192, 245, 250)};
        public static final String[] EIGHT_BALL = {"It is certain","It is decidedly so","Without a doubt","Yes, definitely","You may rely on it","As I see it, yes","Most likely","Outlook good","Yes","Signs point to yes","Reply hazy try again","Ask again later","Better not tell you now","Cannot predict now","Concentrate and ask again","Dount count on it","My reply is no","My source say no","Outlook not so good","Very doubtful"};
        //see http://stackoverflow.com/questions/396429/how-do-you-know-what-version-number-to-use
        public static final String VERSION = "1.0.0";
        public static final String CREDITS = "***LIBRARIES USED:***\n\t - **Reflections (https://github.com/ronmamo/reflections)**\n\t - **JDA (https://github.com/DV8FromTheWorld/JDA)**\n\t - **ffmpeg (https://www.ffmpeg.org)**\n\t - **ffprobe (https://www.ffmpeg.org)**\n\t - **youtube-dl (https://rg3.github.io/youtube-dl)**";

        public static final String INVITE_LINK = "https://discordapp.com/oauth2/authorize?client_id=201810822131875840&scope=bot&permissions=0x8";
    }

    public static class Numbers {
        public static final int MESSAGE_LIMIT = 2000;

        public static final int VOTE_IMAGE_WIDTH = 800;
        public static final int VOTE_IMAGE_HEIGHT = 600;
    }

    public static class Other {
        public static final java.util.List<String> BOT_DEVELOPERS = java.util.Collections.unmodifiableList(new java.util.ArrayList<String>() {
            {
                add("164117897025683456");
                add("138048665112543233");
            }
        });
    }
}