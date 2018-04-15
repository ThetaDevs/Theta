package com.srgood.reasons.impl.commands.audio;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.ArgsBuilder;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 * Created by srgood on 4/14/2018.
 */
public class CommandAudioJoinDescriptor extends BaseCommandDescriptor {

    public CommandAudioJoinDescriptor() {
        super(Executor::new,"Joins the audio channel the caller is in if no arguments given, otherwise will join by name", ArgsBuilder.create().build(),"join");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            VoiceChannel channel = executionData.getSender().getVoiceState().getChannel();
            if (channel != null) {
                executionData.getGuild().getAudioManager().openAudioConnection(channel);
                sendSuccess("Done");
            } else {
                sendError("I can only join a voice channel that you are in.");
            }
        }
    }
}
