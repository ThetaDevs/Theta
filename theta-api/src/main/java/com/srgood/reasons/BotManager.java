package com.srgood.reasons;

import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.BotConfigManager;

import java.time.Instant;
import java.util.logging.Logger;

public interface BotManager extends AutoCloseable {
    void init(String token);

    BotConfigManager getConfigManager();
    CommandManager getCommandManager();
    Logger getLogger();

    Instant getStartTime();
}
