package com.srgood.reasons.config;

import net.dv8tion.jda.core.entities.Guild;

public interface BotConfigManager extends BasicConfigManager {
    GuildConfigManager getGuildConfigManager(Guild guild);

}
