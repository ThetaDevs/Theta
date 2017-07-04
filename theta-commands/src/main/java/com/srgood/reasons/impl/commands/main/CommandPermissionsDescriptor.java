package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.commands.permissions.GuildPermissionSet;
import com.srgood.reasons.impl.commands.permissions.Permission;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;
import com.srgood.reasons.impl.commands.permissions.PermissionStatus;
import com.srgood.reasons.impl.commands.utils.GuildDataManager;
import com.srgood.reasons.impl.commands.utils.RoleUtils;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.DMOutputCommandExecutor;
import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;

public class CommandPermissionsDescriptor extends MultiTierCommandDescriptor {
    public CommandPermissionsDescriptor() {
        super(new LinkedHashSet<>(Arrays.asList(new ListDescriptor(), new SetDescriptor())), "Gets and modifies information about permissions for roles", "<list | set> <...>", "permissions");
    }

    private static class ListDescriptor extends BaseCommandDescriptor {
        public ListDescriptor() {
            super(Executor::new, "Lists permissions for all Roles in the current Guild", "<>", "list");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                if (executionData.getParsedArguments().size() == 0) {
                    displayAllRoles();
                } else {
                    try {
                        sendOutput(formatPermissions(RoleUtils.getUniqueRole(executionData.getGuild(), executionData.getParsedArguments()
                                                                                                                    .get(0))));
                    } catch (IllegalArgumentException e) {
                        sendOutput(e.getMessage());
                    }
                }
            }

            private void displayAllRoles() {
                sendOutput("**`Roles in %s`**", executionData.getGuild().getName());
                for (Role role : executionData.getGuild().getRoles()) {
                    sendOutput(formatPermissions(role));
                }
            }

            private String formatPermissions(Role role) {
                GuildPermissionSet permissionSet = GuildDataManager.getGuildPermissionSet(executionData.getBotManager().getConfigManager(), role.getGuild());
                StringBuilder builder = new StringBuilder();
                builder.append(String.format("```Markdown\n[%s]\n", role.getName()));
                for (Permission permission : Permission.values()) {
                    builder.append(String.format("[%s](%s)\n", permission, permissionSet.getPermissionStatus(role, permission)));
                }
                builder.append("```");
                return builder.toString();
            }
        }
    }

    private static class SetDescriptor extends BaseCommandDescriptor {
        public SetDescriptor() {
            super(Executor::new, "Sets a permission's status for the given role", "<role> <permission> <status>", "set");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                Role role;
                PermissionStatus status;

                try {
                    role = getRoleArg();
                    checkRoleArg(role);

                    status = getPermissionStatusArg();
                    checkPermissionStatusArg(role, status);
                } catch (IllegalArgumentException e) {
                    sendOutput(e.getMessage());
                    return;
                }

                GuildPermissionSet permissionSet = GuildDataManager.getGuildPermissionSet(executionData.getBotManager().getConfigManager(), executionData.getGuild());
                if (shouldSetAll()) {
                    for (Permission permission : Permission.values()) {
                        try {
                            setPermissionStatus(role, permission, status);
                        } catch (IllegalArgumentException e) {
                            sendOutput(e.getMessage());
                        }
                    }
                } else {
                    try {
                        setPermissionStatus(role, parsePermissionArg(), status);
                    } catch (IllegalArgumentException e) {
                        sendOutput(e.getMessage());
                    }
                }
                GuildDataManager.setGuildPermissionSet(executionData.getBotManager().getConfigManager(), executionData.getGuild(), permissionSet);
            }

            private boolean shouldSetAll() {
                return executionData.getParsedArguments().get(1).equalsIgnoreCase("all");
            }

            private Permission parsePermissionArg() {
                try {
                    return Permission.valueOf(executionData.getParsedArguments()
                                                           .get(1)
                                                           .toUpperCase()
                                                           .replaceAll("\\s+", "_"));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Found no permission by that name", e);
                }
            }

            private PermissionStatus getPermissionStatusArg() {
                try {
                    return getPermissionStatus(executionData.getParsedArguments().get(2));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid permission state. Options are `ALLOW`, `DEFER`, `DENY`");
                }
            }

            private void checkPermissionStatusArg(Role role, PermissionStatus status) {
                if (status == PermissionStatus.DEFERRED && role.isPublicRole()) {
                    throw new IllegalArgumentException("Cannot defer on the everyone role!");
                }
            }

            private Role getRoleArg() {
                return RoleUtils.getUniqueRole(executionData.getGuild(), executionData.getParsedArguments().get(0));
            }

            private void checkRoleArg(Role role) {
                if (!executionData.getSender().canInteract(role)) {
                    throw new IllegalArgumentException(String.format("You cannot set permissions for the role **`%s`**!", role.getName()));
                }
            }

            private void setPermissionStatus(Role role, Permission permission, PermissionStatus status) {
                GuildPermissionSet permissionSet = GuildDataManager.getGuildPermissionSet(executionData.getBotManager().getConfigManager(), role.getGuild());

                if (PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), permission).isPresent()) {
                    throw new IllegalArgumentException(String.format("Not setting permission **`%s`** because you do not have it.", permission));
                }

                PermissionStatus backupPermissionStatus = permissionSet.getPermissionStatus(role, permission);

                permissionSet.setPermissionStatus(role, permission, status);

                if (status != PermissionStatus.ALLOWED) {
                    if (PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), permission).isPresent()) {
                        permissionSet.setPermissionStatus(role, permission, backupPermissionStatus);
                        throw new IllegalArgumentException(String.format("Not setting permission **`%s`** because doing so would deny it from you.", permission));
                    }
                }

                GuildDataManager.setGuildPermissionSet(executionData.getBotManager().getConfigManager(), role.getGuild(), permissionSet);

                sendOutput("Permission **`%s`** set to state **`%s`** for role **`%s`**", permission, status, role.getName());
            }

            private PermissionStatus getPermissionStatus(String name) {
                name = name.toLowerCase().trim();
                if (name.matches("allow(ed)?")) {
                    return PermissionStatus.ALLOWED;
                }
                if (name.matches("den(y|ied)")) {
                    return PermissionStatus.DENIED;
                }
                if (name.matches("defer(red)?")) {
                    return PermissionStatus.DEFERRED;
                }
                throw new IllegalArgumentException();
            }

            @Override
            protected Optional<String> checkCallerPermissions() {
                return PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), Permission.MANAGE_PERMISSIONS);
            }
        }
    }
}
