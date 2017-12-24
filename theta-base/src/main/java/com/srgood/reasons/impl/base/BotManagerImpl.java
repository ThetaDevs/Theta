package com.srgood.reasons.impl.base;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.BotConfigManager;
import net.dv8tion.jda.bot.sharding.ShardManager;

import java.util.logging.Logger;

public class BotManagerImpl implements BotManager {
    private ShardManager shardManager;
    private BotConfigManager configManager;
    private CommandManager commandManager;
    private Logger logger;
    private boolean active;

    public BotManagerImpl(ShardManager shardManagerSupplier, BotConfigManager configManagerSupplier, CommandManager commandManagerSupplier, Logger loggerSupplier) {
        this.shardManager = shardManagerSupplier;
        this.configManager = configManagerSupplier;
        this.commandManager = commandManagerSupplier;
        this.logger = loggerSupplier;
        this.active = true;
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
