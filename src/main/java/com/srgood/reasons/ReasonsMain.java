package com.srgood.reasons;

import com.srgood.reasons.commands.impl.actual.CommandRegistrar;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.SimpleLog;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.Instant;

import static com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers.registerLocalSource;
import static com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers.registerRemoteSources;
import static com.srgood.reasons.utils.audio.AudioUtils.playerManager;

public class ReasonsMain {
    public static final Instant START_INSTANT = Instant.now();

    public static JDA jda;
    public static String prefix;

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
            jda = new JDABuilder(AccountType.BOT).addEventListener(new DiscordEventListener())
                                                 .setToken(token)
                                                 .setGame(Game.of("Type @Theta help"))
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
        CommandRegistrar.registerCommands();
    }


    private void initAudio() {
        registerRemoteSources(playerManager);
        registerLocalSource(playerManager);
    }



    private void addToTray() {

        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            //load image here
            Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("/Nicholas.gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            final ActionListener closeListener = e -> System.exit(0);

            PopupMenu popup = new PopupMenu();

            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);

            TrayIcon trayIcon = new TrayIcon(image, "Reasons", popup);

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
