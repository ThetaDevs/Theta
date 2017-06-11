package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.Reference;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.utils.GitUtils;

import java.util.Optional;

import static com.srgood.reasons.impl.Reference.LIBRARIES;

public class CommandInfoDescriptor extends BaseCommandDescriptor {
    public CommandInfoDescriptor() {
        super(Executor::new, "Returns information about the bot, including the current version","<>", "info", "version", "about");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(String.format("The current version is: %s%n%s", Reference.VERSION, getLibrariesText()));

            String lineSep = System.lineSeparator();
            Optional<String> branchOptional = GitUtils.getCurrentBranch();
            Optional<String> commitOptional = GitUtils.getCurrentRevision();

            branchOptional.ifPresent(branch -> stringBuilder.append(lineSep).append(String.format("Local repo is on branch **`%s`**", branch)));
            commitOptional.ifPresent(commit -> stringBuilder.append(lineSep).append(String.format("Local repo is on commit **`%s`**", commit)));

            sendOutput(stringBuilder.toString());
        }

        private String getLibrariesText() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("**```Markdown\n");
            stringBuilder.append("LIBRARIES USED:\n");
            for (int i = 0; i < LIBRARIES.size(); i++) {
                stringBuilder.append(i + 1);
                stringBuilder.append(". ");
                stringBuilder.append(LIBRARIES.get(i));
                stringBuilder.append("\n");
            }
            stringBuilder.append("```**");
            return stringBuilder.toString();
        }
    }
}
