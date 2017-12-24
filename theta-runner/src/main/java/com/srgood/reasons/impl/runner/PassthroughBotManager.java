package com.srgood.reasons.impl.runner;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.BotConfigManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class PassthroughBotManager implements BotManager {
    private Future<BotManager> botManagerFuture;
    private BotManager botManager;

    public PassthroughBotManager(Future<BotManager> botManagerFuture) {
        this.botManagerFuture = botManagerFuture;
    }

    private BotManager getBotManager() {
        if (botManager == null) {
            try {
                botManager = botManagerFuture.get();
                botManagerFuture = null;
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        }
        return botManager;
    }

    @Override
    public BotConfigManager getConfigManager() {
        return getBotManager().getConfigManager();
    }

    @Override
    public CommandManager getCommandManager() {
        return getBotManager().getCommandManager();
    }

    @Override
    public Logger getLogger() {
        return getBotManager().getLogger();
    }

    @Override
    public void close() {
        try {
            getBotManager().close();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
