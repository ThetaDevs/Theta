package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandSetPrefix implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        try {
            ConfigUtils.setGuildPrefix(event.getGuild(), args[0]);
            event.getChannel().sendMessage("Prefix set to: " + args[0]).queue();
        } catch (Exception e) {
            event.getChannel().sendMessage(help()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return "Sets the prefix on this server. Use: '" + ReasonsMain.prefix + "setprefix <prefix>'";
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.ADMINISTRATOR;
    }
}
