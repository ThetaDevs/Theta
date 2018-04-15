package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.CommandConstants;
import com.srgood.reasons.impl.commands.utils.GitUtils;

import java.util.Optional;

import static com.srgood.reasons.impl.commands.CommandConstants.LIBRARIES;

public class CommandInfoDescriptor extends BaseCommandDescriptor {
    public CommandInfoDescriptor() {
        super(Executor::new, "Returns information about the bot, including the current version", null, "info", "version", "about");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(String.format("The current version is: %s%n%s", CommandConstants.VERSION_SUPPLIER.get(), getLibrariesText()));

            String lineSep = System.lineSeparator();
            Optional<String> branchOptional = GitUtils.getCachedBranch();
            Optional<String> commitOptional = GitUtils.getCachedRevision();

            branchOptional.ifPresent(branch -> stringBuilder.append(lineSep)
                                                            .append(String.format("Running branch **`%s`**", branch)));
            commitOptional.ifPresent(commit -> stringBuilder.append(lineSep)
                                                            .append(String.format("Running commit **`%s`**", commit)));

            sendSuccess(stringBuilder.toString());
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
