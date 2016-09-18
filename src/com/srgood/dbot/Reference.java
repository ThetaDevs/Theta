package com.srgood.dbot;

public class Reference {

    public static class Strings {

        public static final String TABLE_FLIP = "(\u256F\u00B0\u25A1\u00B0\uFF09\u256F\uFE35 \u253B\u2501\u253B";
        private static final String TABLE_UNFLIP = "\u252C\u2500\u252C\uFEFF \u30CE( \u309C-\u309C\u30CE)";
        public static final String TABLE_UNFLIP_JOKE = TABLE_UNFLIP + " \n\n Calm down!";

        public static final String BOT_TOKEN_REASONS = "MjAxODEwODM4MDcwMzYyMTEy.CnW-4w.-U2KZFTgfzXFd1HNoC_yYjF426s";
        public static final String BOT_TOKEN_REASONS_DEV_1 = "MjA3MjIyNzY5MjA3NzM4MzY4.Cnf-vg.UNECSqJIHUX_9sXL-98W27XUlIU";
        public static final String BOT_TOKEN_REASONS_DEV_2 = "MjA3MjIzMTU4OTAwNjU0MDgw.Cnf_Gw.HKBXiCldwx57cN_1uHt6XhyNEwY";

        public static final String[] EIGHT_BALL = {"It is certain","It is decidedly so","Without a doubt","Yes, definitely","You may rely on it","As I see it, yes","Most likely","Outlook good","Yes","Signs point to yes","Reply hazy try again","Ask again later","Better not tell you now","Cannot predict now","Concentrate and ask again","Dount count on it","My reply is no","My sources say no","Outlook not so good","Very doubtful"};
        //see http://stackoverflow.com/questions/396429/how-do-you-know-what-version-number-to-use
        public static final String VERSION = "1.0.0";
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