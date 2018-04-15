package com.srgood.reasons.impl.commands.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GitUtils {
    private static Repository repository = null;

    private static final String DEFAULT_BRANCH = "master";
    private static final String DEFAULT_REMOTE_NAME = "origin";
    private static final String REMOTE_PREFIX = "refs/remotes/" + DEFAULT_REMOTE_NAME + "/";

    // Cached in CommandGitDescriptor#init
    private static final String cachedBranch;
    private static final String cachedRevision;

    static {
        generateRepository();
        cachedBranch = getCurrentBranch().orElse(null);
        cachedRevision = getCurrentRevision().orElse(null);
    }

    public static Optional<String> getCurrentBranch() {
        return executeOnRepositorySafe(Repository::getBranch);
    }

    public static boolean localRepoExists() {
        return repository != null;
    }

    private static <T> Optional<T> executeOnRepositorySafe(FallibleFunction<Repository, T> function) {
        if (!localRepoExists()) {
            return Optional.empty();
        }

        Function<Repository, T> safeFunction = FallibleFunction.exceptionProofFallibleFunction(function, null);
        return Optional.ofNullable(safeFunction.apply(repository));
    }

    public static Optional<String> getCurrentRevision() {
        return executeOnRepositorySafe(repo -> repo.findRef(Constants.HEAD).getObjectId().getName());
    }

    public static void updateRepo() {
        executeOnRepositorySafe(repo -> {
            Git git = new Git(repo);
            String currentBranch = repo.getBranch();

            git.stashCreate().call();
            git.fetch().setRemoveDeletedRefs(true).setCheckFetchedObjects(true).setRemote(DEFAULT_REMOTE_NAME).call();

            List<Ref> remoteBranchRefs = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
            List<String> remoteBranchNames = remoteBranchRefs.stream().map(Ref::getName).collect(Collectors.toList());
            boolean hasMatchingRemoteBranch = remoteBranchNames.contains(REMOTE_PREFIX + currentBranch);

            String actualBranch;

            if (!hasMatchingRemoteBranch) {
                actualBranch = DEFAULT_BRANCH;
            } else {
                actualBranch = currentBranch;
            }

            git.checkout().setName(actualBranch).call();

            Ref upstreamRef = repo.findRef(actualBranch);

            git.reset().setMode(ResetCommand.ResetType.HARD).setRef(upstreamRef.getName()).call();

            return null;
        });
    }

    public static Optional<String> getCachedBranch() {
        return executeOnRepositorySafe(repo -> cachedBranch);
    }

    public static Optional<String> getCachedRevision() {
        return executeOnRepositorySafe(repo -> cachedRevision);
    }

    private static void generateRepository() {
        try {
            FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder().readEnvironment().findGitDir();
            if (repositoryBuilder.getGitDir() == null) {
                return; // Not in a repo, just give up. Life's over... WHY SHOULD THE METHOD GO ON? I'M ENDING IT HERE!
            }
            repository = repositoryBuilder.setMustExist(true).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
