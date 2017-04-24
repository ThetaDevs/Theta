package com.srgood.reasons.utils;

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

    static {
        generateRepository();
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

        Function<Repository, T> safeFunction = exceptionProofFallibleFunction(function, null);
        return Optional.ofNullable(safeFunction.apply(repository));
    }

    private static <T, R> Function<T, R> exceptionProofFallibleFunction(FallibleFunction<T, R> function, R defaultValue) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                return defaultValue;
            }
        };
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

    private static void generateRepository() {
        try {
            repository = new FileRepositoryBuilder().readEnvironment().findGitDir().setMustExist(true).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    private interface FallibleFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
