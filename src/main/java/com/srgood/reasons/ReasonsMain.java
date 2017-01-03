package com.srgood.reasons;

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

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

import static com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers.registerLocalSource;
import static com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers.registerRemoteSources;
import static com.srgood.reasons.utils.audio.AudioUtils.playerManager;

public class ReasonsMain {

    public static final Instant START_INSTANT = Instant.now();
    public static final CommandParser COMMAND_PARSER = new CommandParser();

    public static JDA jda;
    public static String prefix;
    public static String overrideKey = SecureOverrideKeyGenerator.nextOverrideKey();
    public static ByteArrayOutputStream out, errOut;

    private boolean firstTime;
    private TrayIcon trayIcon;

    public static JDA getJda() {
        return jda;
    }

    public static void main(String[] args) {
        String token = getToken(args);
        new ReasonsMain().init(token);
    }

    private static String getToken(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("No token was provided");
        } else if (args.length > 1) {
            throw new IllegalArgumentException("Only one argument should be provided");
        }
        return args[0];
    }

    public void init(String token) {
        initJDA(token);
        initConfig();
        initCommands();

        initAudio();

        addToTray();
    }

    private void initJDA(String token) {
        try {
            jda = new JDABuilder(AccountType.BOT).addListener(new DiscordEventListener())
                                                 .setToken(token)
                                                 .setGame(Game.of("Type @Reasons help"))
                                                 .setAutoReconnect(true)
                                                 .buildBlocking();
        } catch (LoginException | IllegalArgumentException e) {
            SimpleLog.getLog("Reasons").fatal("**COULD NOT LOG IN** An invalid token was provided.");
            System.exit(-1);
        } catch (RateLimitedException e) {
            SimpleLog.getLog("Reasons").fatal("**We are being ratelimited**");
            e.printStackTrace();
            System.exit(-1);
        } catch (InterruptedException e) {
            SimpleLog.getLog("Reasons").fatal("InterruptedException");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initConfig() {
        try {
            ConfigUtils.initConfig();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void initCommands() {
        SimpleLog.getLog("Reasons").info("Session override Key: " + overrideKey);

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
            SimpleLog.getLog("Reasons").warn("One or more of the commands failed to map");
            e.printStackTrace();
        }
    }

    private void initAudio() {
        registerRemoteSources(playerManager);
        registerLocalSource(playerManager);
    }

    public void addToTray() {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            //load image here
            Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("/Nicholas.gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            final ActionListener closeListener = e -> {
                System.exit(0);
            };

            PopupMenu popup = new PopupMenu();

            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);

            trayIcon = new TrayIcon(image, "Reasons", popup);

            try {
                systemTray.add(trayIcon);
                trayIcon.setImageAutoSize(true); // <- Sets the image size properly
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        jda.shutdown();
    }

}
