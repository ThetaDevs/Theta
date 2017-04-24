package com.srgood.reasons.commands.impl.base.executor;

import com.srgood.reasons.commands.CommandExecutor;

public enum EmptyCommandExecutor implements CommandExecutor {
    INSTANCE;

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public void execute() {}
}
