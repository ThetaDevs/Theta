package com.srgood.reasons.impl.base.commands.executor;

import com.srgood.reasons.commands.CommandExecutionData;
import net.dv8tion.jda.core.entities.MessageChannel;

public abstract class ChannelOutputCommandExecutor extends BaseCommandExecutor {
    public ChannelOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    @Override
    boolean showEmbedFooter() {
        return true;
    }

    @Override
    MessageChannel outputChannel() {
        return executionData.getChannel();
    }
}
