package com.srgood.reasons.impl.commands;
import com.srgood.reasons.BotManager;
import com.srgood.reasons.impl.commands.utils.CensorUtils;
import com.srgood.reasons.impl.commands.utils.GuildDataManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CensorEventListener extends ListenerAdapter {
    private final BotManager botManager;

    public CensorEventListener(BotManager botManager) {
        this.botManager = botManager;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        CensorUtils.checkCensor(GuildDataManager.getGuildCensorList(botManager.getConfigManager(), event.getGuild()), event.getMessage());
    }
}