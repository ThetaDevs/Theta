package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.ArgsBuilder;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.DMOutputCommandExecutor;
import com.srgood.reasons.impl.commands.utils.RoleUtils;
import com.srgood.reasons.permissions.PermissionProvider;
import com.srgood.reasons.permissions.PermissionStatus;
import net.dv8tion.jda.core.entities.IPermissionHolder;
import net.dv8tion.jda.core.entities.Role;

import java.util.*;

public class CommandPermissionsDescriptor extends MultiTierCommandDescriptor {
    public CommandPermissionsDescriptor() {
        super(new LinkedHashSet<>(Arrays.asList(new ListDescriptor(), new SetDescriptor())), "Gets and modifies information about permissions for roles", true,
                new HashSet<String>(){{
                    add("permissions");
                }}, "permissions");
    }

    private static class ListDescriptor extends BaseCommandDescriptor {
        public ListDescriptor() {
            super(Executor::new, "Lists permissions for all Roles in the current Guild", null, "list");
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
                        sendSuccess(formatPermissions(RoleUtils.getUniqueRole(executionData.getGuild(), executionData.getParsedArguments()
                                                                                                                    .get(0))));
                    } catch (IllegalArgumentException e) {
                        sendError(e.getMessage());
                    }
                }
            }

            private void displayAllRoles() {
                sendRaw("**`Roles in %s`**", executionData.getGuild().getName());
                for (Role role : executionData.getGuild().getRoles()) {
                    sendRaw(formatPermissions(role));
                }
            }

            private String formatPermissions(Role role) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.format("```Markdown\n[%s]\n", role.getName()));

                List<String> registeredPermissions = new ArrayList<>(permissionProvider().getRegisteredPermissions());
                Map<String, PermissionStatus> rolePermissionMap = permissionProvider().getPermissionsMap(role);
                Collections.sort(registeredPermissions);
                PermissionStatus defaultPermission = role.isPublicRole() ? PermissionStatus.ALLOWED : PermissionStatus.DEFERRED;
                for (String permission : registeredPermissions) {
                    stringBuilder.append("[")
                                 .append(permission)
                                 .append("]")
                                 .append("(")
                                 .append(rolePermissionMap.getOrDefault(permission, defaultPermission).toString())
                                 .append(")")
                                 .append("\n");
                }
                stringBuilder.append("```");
                return stringBuilder.toString();
            }
        }
    }

    private static class SetDescriptor extends BaseCommandDescriptor {
        public SetDescriptor() {
            super(Executor::new, "Sets a permission's status for the given role", ArgsBuilder.create().addString("role").addString("permission").addString("status").build(), "set");
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
                    sendError(e.getMessage());
                    return;
                }

                if (shouldSetAll()) {
                    for (String permission : permissionProvider().getRegisteredPermissions()) {
                        try {
                            setPermissionStatus(role, permission, status);
                        } catch (IllegalArgumentException e) {
                            sendError(e.getMessage());
                        }
                    }
                } else {
                    try {
                        setPermissionStatus(role, executionData.getParsedArguments().get(1), status);
                    } catch (IllegalArgumentException e) {
                        sendError(e.getMessage());
                    }
                }
            }

            private boolean shouldSetAll() {
                return executionData.getParsedArguments().get(1).equalsIgnoreCase("all");
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
                    throw new IllegalArgumentException(String.format("You cannot set permissions for the role **`%s`**!", role
                            .getName()));
                }
            }

            private void setPermissionStatus(Role role, String permission, PermissionStatus status) {
                if (!checkPermission(permission)) {
                    throw new IllegalArgumentException(String.format("Not setting permission **`%s`** because you do not have it.", permission));
                }

                if (status != PermissionStatus.ALLOWED)
                    checkDoesNotDenyFromSender(permission);

                permissionProvider().setPermission(role, permission, status);

                sendSuccess("Permission **`%s`** set to state **`%s`** for role **`%s`**", permission, status, role.getName());
            }

            private void checkDoesNotDenyFromSender(String permission) {
                class TestPermissionProvider implements PermissionProvider {
                    private Map<IPermissionHolder, Map<String, PermissionStatus>> permissionsMap;

                    public TestPermissionProvider(Map<IPermissionHolder, Map<String, PermissionStatus>> permissionsMap) {
                        this.permissionsMap = permissionsMap;
                    }

                    @Override
                    public Map<String, PermissionStatus> getPermissionsMap(IPermissionHolder permissionHolder) {
                        Map<String, PermissionStatus> holderMap = permissionsMap.get(permissionHolder);
                        if (holderMap == null)
                            throw new IllegalArgumentException();
                        return holderMap;
                    }

                    @Override
                    public void setPermissions(IPermissionHolder permissionHolder, Map<String, PermissionStatus> permissions) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Set<String> getRegisteredPermissions() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void registerPermissions(Collection<String> permissions) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void close() throws Exception {
                        throw new UnsupportedOperationException();
                    }
                }

                Map<IPermissionHolder, Map<String, PermissionStatus>> relevantPermissions = getPermissionsRelevantToSender(permissionProvider());

                TestPermissionProvider testPermissionProvider = new TestPermissionProvider(relevantPermissions);

                if (!testPermissionProvider.checkPermission(executionData.getSender(), permission))
                    throw new IllegalArgumentException("Cannot perform change, doing so would deny permission from yourself!");
            }

            private Map<IPermissionHolder, Map<String, PermissionStatus>> getPermissionsRelevantToSender(PermissionProvider permissionProvider) {
                Map<IPermissionHolder, Map<String, PermissionStatus>> relevantPermissions = new HashMap<>();
                relevantPermissions.put(executionData.getSender(), callerPermissionMap());

                {
                    Role publicRole = executionData.getGuild().getPublicRole();
                    relevantPermissions.put(publicRole, permissionProvider.getPermissionsMap(publicRole));
                }

                for (Role senderRole : executionData.getSender().getRoles()) {
                    relevantPermissions.put(senderRole, permissionProvider.getPermissionsMap(senderRole));
                }
                return relevantPermissions;
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
            protected void checkCallerPermissions() {
                requirePermission("permissions");
            }
        }
    }
}
