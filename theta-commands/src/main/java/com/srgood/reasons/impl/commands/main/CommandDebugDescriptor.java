package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;
import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;

public class CommandDebugDescriptor extends MultiTierCommandDescriptor {
    private static final boolean ALLOW_DEBUG = true;

    public CommandDebugDescriptor() {
        super(new LinkedHashSet<>(Arrays.asList(new DeleteGuildDescriptor(), new RemoveRolesDescriptor())), "FOR DEBUG ONLY", "<deleteguild | removeroles>", false, "debug");
    }

    private static abstract class BaseExecutor extends ChannelOutputCommandExecutor {
        public BaseExecutor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkBotAdmin(executionData.getSender());
        }

        @Override
        protected Optional<String> customPreExecuteCheck() {
            if (!ALLOW_DEBUG) {
                return Optional.of("Debug mode is disabled in this version.");
            } else {
                return Optional.empty();
            }
        }
    }

    private static class DeleteGuildDescriptor extends BaseCommandDescriptor {
        public DeleteGuildDescriptor() {
            super(Executor::new, "Deletes the current guild from the config", "<>", "deleteguild");
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
            super(Executor::new, "Removes roles from old bot system.", "<>", "removeroles");
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
