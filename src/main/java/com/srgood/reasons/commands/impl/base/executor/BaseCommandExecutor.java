package com.srgood.reasons.commands.impl.base.executor;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandExecutor;
import net.dv8tion.jda.core.entities.Message;

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
            sendOutput(callerPermissionResult.get());
            return false;
        }

        Optional<String> botPermissionResult = checkBotPermissions();
        if (botPermissionResult.isPresent()) {
            sendOutput(botPermissionResult.get());
            return false;
        }

        Optional<String> customCheckResult = customPreExecuteCheck();
        if (customCheckResult.isPresent()) {
            sendOutput(customCheckResult.get());
            return false;
        }

        return true;
    }

    protected abstract void sendOutput(String format, Object... arguments);

    protected abstract void sendOutput(Message message);
}
