package com.srgood.dbot;
 
import java.util.HashMap;
import java.util.Map;

import com.srgood.dbot.ref.RefStrings;
import com.srgood.dbot.utils.PermissionOps;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;

import net.dv8tion.jda.audio.player.Player;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.SimpleLog;
 
/**
 * <h1>Bot Listener</h1>
 *
 * Bot Listener deals with MessageRecieved events, excluding its own.
 *
 * @author srgood
 * @version 0.8
 * @since 7/12/16
 */
 
public class BotListener extends ListenerAdapter {
    public static Map<String,Player> players = new HashMap<>();
   
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String localPrefix = XMLHandler.getGuildPrefix(event.getGuild());

        if (event.getMessage().getContent().equals(RefStrings.TABLE_FLIP)) {
            event.getChannel().sendMessage(RefStrings.TABLE_UNFLIP_JOKE);
        }

        BotMain.storeMessage(event);

        if (event.getMessage().getContent().startsWith(localPrefix)
                && event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()) {
            BotMain.handleCommand(BotMain.parser.parse(event.getMessage().getContent().toLowerCase(), event, localPrefix));
            SimpleLog.getLog("Reasons").info("Got Valid Input");
        } else {
            try {

                if (event.getJDA().getSelfInfo().getAsMention()
                        .equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {

                    SimpleLog.getLog("Reasons").info("Got Valid Input (mention)");
                    BotMain.handleCommand(BotMain.parser.parse(event.getMessage().getContent().toLowerCase(), event, localPrefix));
                }
            } catch (Exception e) {

            }
        }

    }
       
        //does stuff once JDA is loaded
        @Override
        public void onReady(ReadyEvent event){
           
        }
       
        @Override
        public void onGuildJoin(GuildJoinEvent event) {
//          initGuild(event.getGuild());
        }

    public static void initGuild(Guild guild) {

        XMLHandler.initGuildXML(guild);

        try {
            for (Permissions permission : Permissions.values()) {
                PermissionOps.createRole(permission, guild, true);
            }
        } catch (PermissionException e3) {
            SimpleLog.getLog("Reasons").warn("Could not create custom role! Possible permissions problem?");
        }

        XMLHandler.initGuildCommands(guild);
    }
}