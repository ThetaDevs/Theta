package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.Reference;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.utils.GitUtils;

import java.util.Arrays;
import java.util.Optional;

public class CommandInfoDescriptor extends BaseCommandDescriptor {
    public CommandInfoDescriptor() {
        super(Executor::new, "Returns information about the bot, including the current version","<>", Arrays.asList("info", "version", "about"));
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(String.format("The current version is: %s%n%s", Reference.VERSION, Reference.CREDITS));

            String lineSep = System.lineSeparator();
            Optional<String> branchOptional = GitUtils.getCurrentBranch();
            Optional<String> commitOptional = GitUtils.getCurrentRevision();

            branchOptional.ifPresent(branch -> stringBuilder.append(lineSep).append(String.format("Local repo is on branch **`%s`**", branch)));
            commitOptional.ifPresent(commit -> stringBuilder.append(lineSep).append(String.format("Local repo is on commit **`%s`**", commit)));

            sendOutput(stringBuilder.toString());
        }
    }
}
