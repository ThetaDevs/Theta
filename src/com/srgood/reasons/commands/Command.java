package com.srgood.reasons.commands;


import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public interface Command {
    //use this to decide if your commands action should run or not (if unneeded just return true)
    default boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    //the commands action
    void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException;

    //used by the #!help command to provide information about the command
    String help();

    //use this to run post command actions if need be
    default void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    //required permission
    default PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermission(guild, this);
    }

    //used for XML
    default PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.STANDARD;
    }

    default String[] names() {
        return new String[] {this.getClass().getSimpleName().toLowerCase().replaceAll("command(audio)?", "")};
    }

    default boolean canSetEnabled() {
        return true;
    }
}
