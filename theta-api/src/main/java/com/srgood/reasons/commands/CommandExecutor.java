package com.srgood.reasons.commands;

@FunctionalInterface
public interface CommandExecutor {
    default boolean shouldExecute() { return true; }

    void execute();

    default void postExecution() {}
}
