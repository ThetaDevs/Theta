package com.srgood.reasons.impl.commands.audio;

import com.srgood.reasons.commands.CommandManager;

public class AudioCommandsRegistrar {
    public static void registerCommands(CommandManager commandManager) {
        commandManager.registerCommand(new CommandAudioJoinDescriptor());
        commandManager.registerCommand(new CommandAudioLeaveDescriptor());
    }
}