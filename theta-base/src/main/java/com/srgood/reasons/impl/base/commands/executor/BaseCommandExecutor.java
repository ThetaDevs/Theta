package com.srgood.reasons.impl.base.commands.executor;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandExecutor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.util.Optional;

public abstract class BaseCommandExecutor implements CommandExecutor {

    protected final CommandExecutionData executionData;

    BaseCommandExecutor(CommandExecutionData executionData) {
        this.executionData = executionData;
    }

    protected Optional<String> checkCallerPermissions() {
        return Optional.empty();
    }

    protected Optional<String> checkBotPermissions() {
        return Optional.empty();
    }

    protected Optional<String> customPreExecuteCheck() {
        return Optional.empty();
    }

    @Override
    public boolean shouldExecute() {
        Optional<String> callerPermissionResult = checkCallerPermissions();
        if (callerPermissionResult.isPresent()) {
            sendError(callerPermissionResult.get());
            return false;
        }

        Optional<String> botPermissionResult = checkBotPermissions();
        if (botPermissionResult.isPresent()) {
            sendError(botPermissionResult.get());
            return false;
        }

        Optional<String> customCheckResult = customPreExecuteCheck();
        if (customCheckResult.isPresent()) {
            sendError(customCheckResult.get());
            return false;
        }

        return true;
    }

    abstract boolean showEmbedFooter();

    abstract MessageChannel outputChannel();

    private void sendOutput(Color embedColor, String format, Object... arguments) {
        sendOutput(new MessageBuilder(createEmbed(showEmbedFooter(), format, arguments).setColor(embedColor)).build());
    }

    protected void sendOutput(String format, Object... arguments) {
        sendOutput(null, format, arguments);
    }

    protected void sendOutput(Message message) {
        outputChannel().sendMessage(message).queue();
    }

    protected void sendSuccess(String format, Object... arguments) {
        sendOutput(Color.GREEN, format, arguments);
    }

    protected void sendError(String format, Object... arguments) {
        sendOutput(Color.RED, format, arguments);
    }

    protected void sendRaw(String format, Object... arguments) {
        sendOutput(new MessageBuilder(String.format(format, arguments)).build());
    }

    private final EmbedBuilder createEmbed(boolean footer, String format, Object... arguments) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.appendDescription(String.format(format, arguments));
        if (footer) {
            embedBuilder.setFooter("Requested by: " + executionData.getSender()
                                                                   .getEffectiveName(), executionData.getSender()
                                                                                                     .getUser()
                                                                                                     .getAvatarUrl());
        }
        return embedBuilder;
    }
}
