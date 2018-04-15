package com.srgood.reasons.impl.commands.audio;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;

/**
 * Created by srgood on 4/14/2018.
 */
public class CommandAudioLeaveDescriptor extends BaseCommandDescriptor {
    public CommandAudioLeaveDescriptor() {
        super(Executor::new,"Makes the bot leave the current audio channel", null, "leave");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getGuild().getAudioManager().isConnected()) {
                executionData.getGuild().getAudioManager().closeAudioConnection();
            } else {
                this.sendOutput("Cannot leave, as no current connection is active");
            }
        }
    }
}
