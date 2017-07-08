package com.srgood.reasons.config;

import net.dv8tion.jda.core.entities.Guild;

public interface BotConfigManager extends BasicConfigManager, AutoCloseable {
    GuildConfigManager getGuildConfigManager(Guild guild);
}
