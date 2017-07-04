package com.srgood.reasons.impl.base.commands.executor;

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
