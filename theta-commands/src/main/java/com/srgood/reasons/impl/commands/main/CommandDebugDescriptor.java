package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class CommandDebugDescriptor extends MultiTierCommandDescriptor {
    private static final boolean ALLOW_DEBUG = true;

    public CommandDebugDescriptor() {
        super(new LinkedHashSet<>(Arrays.asList(new DeleteGuildDescriptor(), new RemoveRolesDescriptor())), "FOR DEBUG ONLY", false, new HashSet<String>(){{
            add("debug");
        }}, "debug");
    }

    private static abstract class BaseExecutor extends ChannelOutputCommandExecutor {
        public BaseExecutor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        protected void checkCallerPermissions() {
            requirePermission("debug");
        }

        @Override
        protected void customPreExecuteCheck() {
            if (!ALLOW_DEBUG) {
                dontExecute("Debug mode is disabled.");
            }
        }
    }

    private static class DeleteGuildDescriptor extends BaseCommandDescriptor {
        public DeleteGuildDescriptor() {
            super(Executor::new, "Deletes the current guild from the config", null, "deleteguild");
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                executionData.getBotManager()
                             .getConfigManager()
                             .getGuildConfigManager(executionData.getGuild())
                             .delete();
            }
        }
    }

    private static class RemoveRolesDescriptor extends BaseCommandDescriptor {
        public RemoveRolesDescriptor() {
            super(Executor::new, "Removes roles from old bot system.", null, "removeroles");
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                for (Role r : executionData.getGuild().getRoles()) {
                    if (r.getName().equals("Reasons Admin") || r.getName().equals("DJ")) {
                        r.delete().queue(role -> sendSuccess("Removed role: **`%s`**", r.getName()));
                    }
                }
            }
        }
    }
}
