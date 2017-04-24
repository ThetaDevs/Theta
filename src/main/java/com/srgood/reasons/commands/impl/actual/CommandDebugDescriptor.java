package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.permissions.PermissionChecker;
import com.srgood.reasons.utils.GuildUtils;
import net.dv8tion.jda.core.entities.Role;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

public class CommandDebugDescriptor extends MultiTierCommandDescriptor {
    private static final boolean ALLOW_DEBUG = true;

    public CommandDebugDescriptor() {
        super(new HashSet<>(
              Arrays.asList(
                        new DeleteGuildDescriptor(),
                        new RemoveRolesDescriptor(),
                        new UptimeDescriptor())),
              "FOR DEBUG ONLY",
              "<deleteguild | removeroles | uptime>",
              Collections.singletonList("debug"));
    }

    private static abstract class BaseExecutor extends ChannelOutputCommandExecutor {
        public BaseExecutor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public boolean shouldExecute() {
            return ALLOW_DEBUG && super.shouldExecute();
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkBotAdmin(executionData.getSender());
        }
    }

    private static class DeleteGuildDescriptor extends BaseCommandDescriptor {
        public DeleteGuildDescriptor() {
            super(Executor::new, "Deletes the current guild from the config", "<>" ,"deleteguild");
        }

        private static class Executor extends BaseExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                GuildUtils.deleteGuild(executionData.getGuild());
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
                        r.delete()
                         .queue(role -> sendOutput("Removed role: **`%s`**", r.getName()));
                    }
                }
            }
        }
    }

    private static class UptimeDescriptor extends BaseCommandDescriptor {
        public UptimeDescriptor() {
            super(Executor::new, "Gets the current uptime", "<>", "uptime");
        }

        private static class Executor extends BaseExecutor {

            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                long seconds = Duration.between(ReasonsMain.START_INSTANT, Instant.now()).getSeconds();
                long absSeconds = Math.abs(seconds);
                String positive = String.format("%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
                String x = seconds < 0 ? "-" + positive : positive;
                sendOutput(x);
            }
        }
    }
}
