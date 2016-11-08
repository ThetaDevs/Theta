package com.srgood.reasons.commands;


import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public interface Command {
    //use this to decide if your commands action should run or not (if unneeded just return true)
    boolean called(String[] args, GuildMessageReceivedEvent event);

    //the commands action
    void action(String[] args, GuildMessageReceivedEvent event);

    //used by the #!help command to provide information about the command
    String help();

    //use this to run post command actions if need be
    void executed(boolean success, GuildMessageReceivedEvent event);

    //required permission
    PermissionLevels permissionLevel(Guild guild);

    //used for XML
    PermissionLevels defaultPermissionLevel();

    default String[] names() {
        return new String[] {this.getClass().getSimpleName().toLowerCase().replaceAll("command(audio)?", "")};
    }

    default boolean canSetEnabled() {
        return true;
    }
}
