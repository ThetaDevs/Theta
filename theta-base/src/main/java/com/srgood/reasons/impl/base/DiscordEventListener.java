package com.srgood.reasons.impl.base;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.config.GuildConfigManager;
import com.srgood.reasons.impl.base.commands.CommandUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiPredicate;

import static com.srgood.reasons.impl.base.commands.CommandUtils.generatePossiblePrefixesForGuild;

/**
 * <h1>DiscordEventListener</h1>
 * <p>
 * DiscordEventListener deals with MessageReceived events, excluding its own.
 *
 * @author srgood
 * @version 0.8
 * @since 7/12/16
 */
public class DiscordEventListener extends ListenerAdapter {

    private BotManager botManager;
    private final Collection<BiPredicate<Message, BotManager>> messageFilters;
    private Future<BotManager> botManagerFuture;

    public DiscordEventListener(Future<BotManager> botManagerFuture, Collection<BiPredicate<Message, BotManager>> messageFilters) {
        this.botManagerFuture = botManagerFuture;
        this.messageFilters = messageFilters;
    }

    @Override
    public void onReady(ReadyEvent event) {
        try {
            botManager = botManagerFuture.get();
            botManagerFuture = null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        handleMessage(event.getMessage());
    }

    private void handleMessage(Message message) {
        if (Objects.equals(message.getAuthor(), message.getJDA().getSelfUser())) return;

        //if (!Objects.equals(botManager.getConfigManager()
        //                              .getGuildConfigManager(message.getGuild())
        //                              .getChannelConfigManager(message.getTextChannel())
        //                              .getProperty("listening", "true"), "true")) {
        //    return;
        //}

        if (!messageFilters.stream().allMatch(p -> p.test(message, botManager))) {
            return;
        }

        if (Objects.equals(message.getContent(), BaseConstants.TABLE_FLIP)) {
            message.getChannel().sendMessage(BaseConstants.TABLE_UNFLIP_JOKE).queue();
        }

        GuildConfigManager guildConfigManager = getGuildConfigManager(message.getGuild());
        if (CommandUtils.isCommandMessage(message.getRawContent(), generatePossiblePrefixesForGuild(guildConfigManager, message.getGuild()))) {
            botManager.getCommandManager().handleCommandMessage(message);
            botManager.getLogger().info("Got command message: " + message.getContent());
        }

        //CensorUtils.checkCensor(GuildDataManager.getGuildCensorList(botManager.getConfigManager(), message.getGuild()), message);
    }

    //@Override
    //public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    //    GreetingUtils.tryGreeting(event.getMember(), getGuildConfigManager(event));
    //}
    //@Override
    //public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
    //    GreetingUtils.tryGoodbye(event.getMember(), getGuildConfigManager(event));
    //}


    @Override
    public void onGuildJoin(GuildJoinEvent event) {
//          initGuild(event.getGuild());
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        handleMessage(event.getMessage());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (event.getChannelLeft().getMembers().size() == 1) {
            // TODO FIX AUDIO
//            AudioManager manager = event.getGuild();
//
//            manager.closeAudioConnection();
        }
    }

    private GuildConfigManager getGuildConfigManager(GenericGuildEvent guildEvent) {
        return getGuildConfigManager(guildEvent.getGuild());
    }

    private GuildConfigManager getGuildConfigManager(Guild guild) {
        return botManager.getConfigManager().getGuildConfigManager(guild);
    }
}