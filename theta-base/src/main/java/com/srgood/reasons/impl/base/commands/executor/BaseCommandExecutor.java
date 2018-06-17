package com.srgood.reasons.impl.base.commands.executor;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandExecutor;
import com.srgood.reasons.impl.base.BaseConstants;
import com.srgood.reasons.permissions.PermissionProvider;
import com.srgood.reasons.permissions.PermissionStatus;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.util.Map;

public abstract class BaseCommandExecutor implements CommandExecutor {

    protected final CommandExecutionData executionData;

    BaseCommandExecutor(CommandExecutionData executionData) {
        this.executionData = executionData;
    }

    protected void checkCallerPermissions() {}

    protected void checkBotPermissions() {}

    protected void customPreExecuteCheck() {}

    protected static class CannotExecuteException extends RuntimeException {
        public CannotExecuteException(String message) {
            super(message);
        }
    }

    protected void requirePermission(String permission) {
        if (BaseConstants.BOT_DEVELOPERS.contains(executionData.getSender().getUser().getId())) return;

        if (!checkPermission(permission)) {
            throw new CannotExecuteException("You don't have needed permission \"" + permission + "\"!");
        }
    }

    protected boolean checkPermission(String permission) {
        return executionData.getSenderPermissions().contains(permission);
    }

    protected PermissionProvider permissionProvider() {
        return executionData.getBotManager().getPermissionProvider();
    }

    protected Map<String, PermissionStatus> callerPermissionMap() {
        return executionData.getBotManager().getPermissionProvider().getPermissionsMap(executionData.getSender());
    }

    protected void requirePermission(Permission permission) {
        if (!executionData.getGuild().getSelfMember().hasPermission(permission)) {
            throw new CannotExecuteException("I don't have needed permission \"" + permission.getName() + "\"!");
        }
    }

    protected void dontExecute(String reason) {
        throw new CannotExecuteException(reason);
    }

    @Override
    public boolean shouldExecute() {
        try {
            checkCallerPermissions();
            checkBotPermissions();
            customPreExecuteCheck();
        } catch (CannotExecuteException e) {
            sendError(e.getMessage());
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
