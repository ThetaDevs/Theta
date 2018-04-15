package com.srgood.reasons.impl.base;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.BotConfigManager;
import net.dv8tion.jda.bot.sharding.ShardManager;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class BotManagerImpl implements BotManager {
    private ShardManager shardManager;
    private BotConfigManager configManager;
    private CommandManager commandManager;
    private Logger logger;
    private boolean active;

    public BotManagerImpl(ShardManager shardManager, BotConfigManager configManager, CommandManager commandManager, Logger logger) {
        this(shardManager, configManager, commandManager, logger, Collections.emptyList());
    }

    public BotManagerImpl(ShardManager shardManager, BotConfigManager configManager, CommandManager commandManager, Logger logger, List<Consumer<BotManager>> dependentFunctions) {
        this.shardManager = shardManager;
        this.configManager = configManager;
        this.commandManager = commandManager;
        this.logger = logger;
        this.active = true;
        for (Consumer<BotManager> consumer : dependentFunctions)
            consumer.accept(this);
    }

    @Override
    public void close() {
        checkActive();
        try {
            shardManager.shutdown();
            configManager.close();
            commandManager.close();
            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BotConfigManager getConfigManager() {
        checkActive();
        return configManager;
    }

    @Override
    public CommandManager getCommandManager() {
        checkActive();
        return commandManager;
    }

    @Override
    public Logger getLogger() {
        checkActive();
        return logger;
    }

    private void clearFields() {
        shardManager = null;
        configManager = null;
        commandManager = null;
        logger = null;
        active = false;
    }

    private void checkActive() {
        if (!active) {
            throw new IllegalStateException("This action cannot be performed unless the BotManager has been initialized.");
        }
    }
}
