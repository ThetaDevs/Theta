package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.permissions.PermissionChecker;
import com.srgood.reasons.utils.GitUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static com.srgood.reasons.utils.GitUtils.localRepoExists;

public class CommandGitDescriptor extends MultiTierCommandDescriptor {
    public CommandGitDescriptor() {
        super(new HashSet<>(Arrays.asList(new InfoDescriptor(), new UpdateDescriptor())), "Manages the local git repo, if present", "<info | update>", Arrays.asList("git", "vcs"));
    }

    private static abstract class BaseExecutor extends ChannelOutputCommandExecutor {
        private final boolean checkPermissions;

        public BaseExecutor(CommandExecutionData executionData, boolean checkPermissions) {
            super(executionData);
            this.checkPermissions = checkPermissions;
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return checkPermissions ? PermissionChecker.checkBotAdmin(executionData.getSender()) : Optional.empty();
        }

        @Override
        protected Optional<String> customPreExecuteCheck() {
            if (!localRepoExists()) {
                return Optional.of("The bot is not running in a local repo.");
            }
            return Optional.empty();
        }
    }

    private static class InfoDescriptor extends BaseCommandDescriptor {
        public InfoDescriptor() {
            super(Executor::new, "Prints the branch and commit of the current local repo, if present", "<>", Arrays.asList("info", "version"));
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData, false);
            }

            @Override
            public void execute() {
                StringBuilder stringBuilder = new StringBuilder();

                String lineSep = System.lineSeparator();
                Optional<String> branchOptional = GitUtils.getCurrentBranch();
                Optional<String> commitOptional = GitUtils.getCurrentRevision();

                branchOptional.ifPresent(branch -> stringBuilder.append(lineSep).append(String.format("Local repo is on branch **`%s`**", branch)));
                commitOptional.ifPresent(commit -> stringBuilder.append(lineSep).append(String.format("Local repo is on commit **`%s`**", commit)));

                sendOutput(stringBuilder.toString());
            }
        }
    }
    private static class UpdateDescriptor extends BaseCommandDescriptor {
        public UpdateDescriptor() {
            super(Executor::new, "Updates the local repo, if present", "<>", Arrays.asList("update", "pull"));
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData, true);
            }

            @Override
            public void execute() {
                GitUtils.updateRepo();
                // We already know we are in a repo. If it's empty, we have a problem
                //noinspection ConstantConditions
                sendOutput("Done. New revision is commit **`%s`**", GitUtils.getCurrentRevision().get());
            }
        }
    }

}
