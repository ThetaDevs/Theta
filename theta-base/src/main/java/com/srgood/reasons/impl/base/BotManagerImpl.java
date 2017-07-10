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
