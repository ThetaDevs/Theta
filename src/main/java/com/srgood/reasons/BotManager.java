package com.srgood.reasons;

import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.BotConfigManager;

import java.time.Instant;
import java.util.logging.Logger;

public interface BotManager {
    void init(String token);
    void shutdown();

    BotConfigManager getConfigManager();
    CommandManager getCommandManager();
    Logger getLogger();

    Instant getStartTime();
}
