package com.srgood.reasons.impl.base.commands.executor;

import com.srgood.reasons.commands.CommandExecutionData;
import net.dv8tion.jda.core.entities.MessageChannel;

public abstract class DMOutputCommandExecutor extends BaseCommandExecutor {
    public DMOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    @Override
    boolean showEmbedFooter() {
        return false;
    }

    @Override
    MessageChannel outputChannel() {
        return executionData.getSender().getUser().openPrivateChannel().complete();
    }
}
