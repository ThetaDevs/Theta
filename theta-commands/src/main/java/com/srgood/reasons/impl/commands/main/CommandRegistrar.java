package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandManager;

public class CommandRegistrar {
    public static void registerCommands(CommandManager commandManager) {
        commandManager.registerCommand(new Command8BallDescriptor());
        commandManager.registerCommand(new CommandBlacklistDescriptor());
        commandManager.registerCommand(new CommandCensorDescriptor());
        commandManager.registerCommand(new CommandCoinFlipDescriptor());
        commandManager.registerCommand(new CommandDebugDescriptor());
        commandManager.registerCommand(new CommandDeleteDescriptor());
        commandManager.registerCommand(new CommandDiceRollDescriptor());
        commandManager.registerCommand(new CommandDisableDescriptor());
        commandManager.registerCommand(new CommandEnableDescriptor());
        commandManager.registerCommand(new CommandEvalDescriptor());
        commandManager.registerCommand(new CommandExportDescriptor());
        commandManager.registerCommand(new CommandGetEnabledDescriptor());
        commandManager.registerCommand(new CommandGetPrefixDescriptor());
        commandManager.registerCommand(new CommandGitDescriptor());
        commandManager.registerCommand(new CommandHelpDescriptor());
        commandManager.registerCommand(new CommandImportDescriptor());
        commandManager.registerCommand(new CommandInfoDescriptor());
        commandManager.registerCommand(new CommandInviteDescriptor());
        commandManager.registerCommand(new CommandListenDescriptor());
        commandManager.registerCommand(new CommandNotifyRandDescriptor());
        commandManager.registerCommand(new CommandPermissionsDescriptor());
        commandManager.registerCommand(new CommandPingDescriptor());
        commandManager.registerCommand(new CommandPongDescriptor());
        commandManager.registerCommand(new CommandRolesDescriptor());
        commandManager.registerCommand(new CommandSetGoodbyeDescriptor());
        commandManager.registerCommand(new CommandSetPrefixDescriptor());
        commandManager.registerCommand(new CommandSetWelcomeDescriptor());
        commandManager.registerCommand(new CommandShutdownDescriptor());
        commandManager.registerCommand(new CommandStatusDescriptor());
    }
}
