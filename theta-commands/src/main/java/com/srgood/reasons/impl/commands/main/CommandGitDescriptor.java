package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;
import com.srgood.reasons.impl.commands.utils.GitUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;

import static com.srgood.reasons.impl.commands.utils.GitUtils.localRepoExists;


public class CommandGitDescriptor extends MultiTierCommandDescriptor {
    public CommandGitDescriptor() {
        super(new LinkedHashSet<>(Arrays.asList(new InfoDescriptor(), new UpdateDescriptor())), "Manages the local git repo, if present", false, "git", "vcs");
    }

    @Override
    public void init(BotManager botManager) {
        // Used to load GitUtils and cache repository
        GitUtils.getCachedRevision();
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
            super(Executor::new, "Prints the branch and commit of the current local repo, if present", null, "info", "version");
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData, false);
            }

            @Override
            public void execute() {
                StringBuilder stringBuilder = new StringBuilder();

                String lineSep = System.lineSeparator();

                Optional<String> cachedBranch = GitUtils.getCachedBranch();
                Optional<String> cachedRevision = GitUtils.getCachedRevision();
                Optional<String> branchOptional = GitUtils.getCurrentBranch();
                Optional<String> commitOptional = GitUtils.getCurrentRevision();

                cachedBranch.ifPresent(branch -> stringBuilder.append(lineSep)
                                                              .append(String.format("Running on branch **`%s`**", branch)));
                cachedRevision.ifPresent(commit -> stringBuilder.append(lineSep)
                                                                .append(String.format("Running on commit **`%s`**", commit)));
                branchOptional.ifPresent(branch -> stringBuilder.append(lineSep)
                                                                .append(String.format("Local repo is on branch **`%s`**", branch)));
                commitOptional.ifPresent(commit -> stringBuilder.append(lineSep)
                                                                .append(String.format("Local repo is on commit **`%s`**", commit)));

                sendSuccess(stringBuilder.toString());
            }
        }
    }

    private static class UpdateDescriptor extends BaseCommandDescriptor {
        public UpdateDescriptor() {
            super(Executor::new, "Updates the local repo, if present", null, "update", "pull");
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
                sendSuccess("Done. New revision is commit **`%s`**", GitUtils.getCurrentRevision().get());
            }
        }
    }

}
