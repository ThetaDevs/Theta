package com.srgood.reasons;

import com.srgood.formatting.LoggedPrintStream;
import com.srgood.reasons.commands.Command;
import com.srgood.reasons.commands.CommandParser;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.utils.SecureOverrideKeyGenerator;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.reflections.Reflections;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import static net.dv8tion.jda.core.utils.SimpleLog.Level.ALL;
import static net.dv8tion.jda.core.utils.SimpleLog.Level.WARNING;

public class ReasonsMain {
    private static final boolean DO_FILE_LOG = false;

    public static final Instant START_INSTANT = Instant.now();
    public static final CommandParser COMMAND_PARSER = new CommandParser();

    public static JDA jda;
    public static SimpleLog log;
    private static File logFile;
    public static String prefix;
    public static String overrideKey = SecureOverrideKeyGenerator.nextOverrideKey();;


    public static JDA getJda() {
        return jda;
    }

    public static void main(String[] args) {
        initLog();
        initJDA();
        initConfig();
        initCommands();

    }

    private static void initJDA() {
        try {
            jda = new JDABuilder(AccountType.BOT).addListener(new DiscordEventListener())
                                                 .setToken(Reference.Strings.BOT_TOKEN_REASONS)
                                                 .setGame(Game.of("Type @Reasons help"))
                                                 .setAutoReconnect(true)
                                                 .buildBlocking();
        } catch (LoginException | IllegalArgumentException e) {

            log.fatal("**COULD NOT LOG IN** An invalid token was provided.");
        } catch (RateLimitedException e) {
            log.fatal("**We are being ratelimited**");
            e.printStackTrace();
        }  catch (InterruptedException e) {
            log.fatal("*Interrupted during login**");
            e.printStackTrace();
        }
    }

    private static void initConfig() {
        try {
            ConfigUtils.initConfig();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private static void initCommands() {
        log.info("Session override Key: " + overrideKey);

        try {
            String[] packages = { "com.srgood.reasons" };

            for (String pack : packages) {
                Reflections mReflect = new Reflections(pack);
                for (Class<? extends Command> cmdClass : mReflect.getSubTypesOf(Command.class)) {
                    if (!cmdClass.isInterface()) {
                        Command cmdInstance = cmdClass.newInstance();
                        String[] names = (String[]) cmdClass.getMethod("names").invoke(cmdInstance);

                        Arrays.stream(names).forEach(name -> CommandUtils.getCommandsMap().put(name, cmdInstance));
                    }
                }
            }
        } catch (Exception e) {
            log.warn("One or more of the commands failed to map");
            e.printStackTrace();
        }
    }

    private static void initLog() {
        log = SimpleLog.getLog("Reasons");
        log.setLevel(WARNING);
        File file = new File(System.getProperty("user.dir") + File.separator+ "log" + File.separator + new SimpleDateFormat("yyy"+ File.separator +"MM" + File.separator + "dd" + File.separator + "HH").format(new Date()));
        file.mkdirs();

        logFile = new File(file.getPath() + File.separator + "log.txt");
        try {
            logFile.createNewFile();
            if (DO_FILE_LOG) {
                log.addFileLog(ALL,logFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initLPS() {
        System.setOut(new LoggedPrintStream(System.out));
        System.setErr(new LoggedPrintStream(System.err));
    }

}
