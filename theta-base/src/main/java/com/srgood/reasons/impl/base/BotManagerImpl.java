package com.srgood.reasons.impl.base;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.BotConfigManager;
import net.dv8tion.jda.core.JDA;

import java.time.Instant;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class BotManagerImpl implements BotManager {
    private final Supplier<JDA> jdaSupplier;
    private final Supplier<BotConfigManager> configManagerSupplier;
    private final Supplier<CommandManager> commandManagerSupplier;
    private final Supplier<Logger> loggerSupplier;
    private JDA jdaCache;
    private BotConfigManager configManagerCache;
    private CommandManager commandManagerCache;
    private Logger loggerCache;
    private Instant startInstant;
    private boolean active;

    public static void main(String[] args) {
        String token = getToken(args);
        //new BotManagerImpl().init(token);
    }

    private static String getToken(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("No token was provided");
        } else if (args.length > 1) {
            throw new IllegalArgumentException("Only one argument should be provided");
        }
        return args[0];
    }

    public BotManagerImpl(Supplier<JDA> jdaSupplier, Supplier<BotConfigManager> configManagerSupplier, Supplier<CommandManager> commandManagerSupplier, Supplier<Logger> loggerSupplier) {
        this.jdaSupplier = jdaSupplier;
        this.configManagerSupplier = configManagerSupplier;
        this.commandManagerSupplier = commandManagerSupplier;
        this.loggerSupplier = loggerSupplier;
        clearFields();
    }

    @Override
    public void init() {
        checkInactive();

        try {
            jdaCache = jdaSupplier.get();
            configManagerCache = configManagerSupplier.get();
            commandManagerCache = commandManagerSupplier.get();
            loggerCache = loggerSupplier.get();
            startInstant = Instant.now();
            active = true;
        } catch (Exception e) {
            clearFields();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        checkActive();
        try {
            jdaCache.shutdown();
            configManagerCache.close();
            commandManagerCache.close();
            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BotConfigManager getConfigManager() {
        checkActive();
        return configManagerCache;
    }

    @Override
    public CommandManager getCommandManager() {
        checkActive();
        return commandManagerCache;
    }

    @Override
    public Logger getLogger() {
        checkActive();
        return loggerCache;
    }

    @Override
    public Instant getStartTime() {
        checkActive();
        return startInstant;
    }

    private void clearFields() {
        jdaCache = null;
        configManagerCache = null;
        commandManagerCache = null;
        loggerCache = null;
        startInstant = null;
        active = false;
    }

    //private void initLogger() {
    //    loggerCache = Logger.getLogger("Theta");
    //    Formatter loggerFormatter = new Formatter() {
    //        @Override
    //        public String format(LogRecord record) {
    //            ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(record.getMillis()), ZoneOffset.systemDefault());
    //            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    //            return String.format("[%s] [%s] [%s]: %s %n", dateFormatter.format(dateTime), StringUtils.capitalize(record
    //                    .getLevel()
    //                    .toString()
    //                    .toLowerCase()), record.getLoggerName(), record.getMessage());
    //        }
    //    };
    //
    //    StreamHandler streamHandler = new StreamHandler(System.out, loggerFormatter) {
    //        @Override
    //        public synchronized void publish(LogRecord record) {
    //            super.publish(record);
    //            super.flush();
    //        }
    //    };
    //    streamHandler.flush();
    //
    //    loggerCache.setUseParentHandlers(false);
    //    loggerCache.addHandler(streamHandler);
    //    loggerCache.setLevel(Level.ALL);
    //}
    //
    //private void initJDA(String token) {
    //    try {
    //        jdaCache = new JDABuilder(AccountType.BOT).addEventListener(new DiscordEventListener(this, Collections.emptyList())) // TODO Add messageChecks for eventlistener
    //                                                  .setToken(token)
    //                                                  .setGame(Game.of("Type @Theta help"))
    //                                                  .setAutoReconnect(true)
    //                                                  .buildBlocking();
    //    } catch (LoginException | IllegalArgumentException e) {
    //        getLogger().severe("**COULD NOT LOG IN** An invalid token was provided.");
    //        throw new RuntimeException(e);
    //    } catch (RateLimitedException e) {
    //        getLogger().severe("**We are being ratelimited**");
    //        e.printStackTrace();
    //        throw new RuntimeException(e);
    //    } catch (InterruptedException e) {
    //        getLogger().severe("InterruptedException");
    //        e.printStackTrace();
    //        throw new RuntimeException(e);
    //    }
    //}
    //
    //private void initConfig() {
    //    //configFileManager = new ConfigFileManager("theta.xml");
    //    //configManagerCache = new BotConfigManagerImpl(configFileManager);
    //}

    private void checkActive() {
        if (!active) {
            throw new IllegalStateException("This action cannot be performed unless the BotManager has been initialized.");
        }
    }

    private void checkInactive() {
        if (active) {
            throw new IllegalStateException("This action cannot be performed unless the BotManager has not yet been initialized.");
        }
    }
}
