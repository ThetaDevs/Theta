package com.srgood.reasons;

import com.srgood.reasons.commands.Command;
import com.srgood.reasons.commands.CommandParser;
import com.srgood.reasons.utils.config.ConfigUtils;
import com.srgood.reasons.utils.CommandUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
import java.io.PrintStream;
import java.time.Instant;
import java.util.Arrays;

public class ReasonsMain extends Application {

    public static JDA jda;

    //Global
    public static final Instant startInstant = Instant.now();
    // prefix and shutdown override key
    public static String prefix;
    public static String overrideKey = com.srgood.reasons.utils.SecureOverrideKeyGenerator.nextOverrideKey();
    public static PrintStream outPS,errOutPS;
    public static ByteArrayOutputStream out,errOut;

    public static final CommandParser parser = new CommandParser();

    private static com.srgood.reasons.app.Controller controller = null;

    private boolean firstTime;
    private TrayIcon trayIcon;

    @Override public void init() throws RateLimitedException {
        out = new ByteArrayOutputStream();
        errOut = new ByteArrayOutputStream();

        outPS = new PrintStream(out) {
            @Override
            public void println(Object o) {
                super.println(o);
                if (controller != null)
                    controller.updateConsole();
            }

            @Override
            public void println(String s) {
                super.println(s);
                if (controller != null)
                    controller.updateConsole();
            }
        };

        errOutPS = new PrintStream(errOut) {
            @Override
            public void println(Object o) {
                super.println(o);
                if (controller != null)
                    controller.updateErrConsole();
            }

            @Override
            public void println(String s) {
                super.println(s);
                if (controller != null)
                    controller.updateErrConsole();
            }

        };



        System.setOut(outPS);
        System.setErr(errOutPS);

        //catch exceptions when building JDA

        try {
            //create a JDA with one Event listener
            jda = new JDABuilder(AccountType.BOT).addListener(new DiscordEventListener()).setToken(Reference.Strings.BOT_TOKEN_REASONS).setGame(Game.of("Type @Reasons help")).setAutoReconnect(true).buildBlocking();
        } catch (LoginException e) {
            SimpleLog.getLog("Reasons").fatal("**COULD NOT LOG IN**");
        } catch (InterruptedException e) {
            SimpleLog.getLog("JDA").fatal("**AN UNKNOWN ERROR OCCURRED DURING LOGIN**");
            e.printStackTrace();
        }

        //load global parameters
        try {
            ConfigUtils.initConfig();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //TODO make the null checks modular and in the LoadParams method, not here

        SimpleLog.getLog("Reasons").info("Session override Key: " + overrideKey);

        //catch null pointer exceptions when creating commands
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

        }  catch (Exception e) {
            SimpleLog.getLog("Reasons").warn("One or more of the commands failed to map");
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/app.fxml"));

        Parent root = loader.load();

        controller = loader.getController();

        controller.updateConsole();

        primaryStage.setTitle("Reasons Console");
        primaryStage.setScene(new Scene(root));

        addToTray(primaryStage);
        firstTime = true;
        Platform.setImplicitExit(false);

        primaryStage.show();
    }

    public void stop () {
        jda.shutdown();
    }


    public static void main(String[] args) {
        launch(args);
    }


    public void addToTray(Stage stage) {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            //load image here
            Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("/Nicholas.gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            stage.setOnCloseRequest(event -> hide(stage));

            final ActionListener closeListener = e -> {
                System.exit(0);
            };

            final ActionListener showListener = e -> Platform.runLater(stage::show);

            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);

            trayIcon = new TrayIcon(image,"Reasons",popup);

            trayIcon.addActionListener(showListener);

            try {
                systemTray.add(trayIcon);
                trayIcon.setImageAutoSize(true); // <- Sets the image size properly
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void showPrgmIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Alert","Reasons has been minimised to tray",TrayIcon.MessageType.INFO);
            firstTime = false;

        }
    }

    private void hide(final Stage stage) {
        Platform.runLater(() -> {
            if (SystemTray.isSupported()) {
                stage.hide();
                showPrgmIsMinimizedMsg();
            } else {
                System.exit(0);
            }
        });
    }


//    private final static int[] illegalChars = { 34, 60, 62, 124, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 58, 42, 63, 92, 46, 47 };

//    static {
//        Arrays.sort(illegalChars);
//    }

}
