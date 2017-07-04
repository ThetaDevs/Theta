package com.srgood.reasons.impl.commands.permissions;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.io.Serializable;
import java.util.*;

public class GuildPermissionSet implements Serializable {
    private final String guildID;
    private final Map<String, BasicPermissionSet> rolePermissions = new HashMap<>();

    private static final Collection<Permission> PERMISSIBLE_ACTION_COLLECTION = Arrays.asList(Permission.values());

    public GuildPermissionSet(Guild guild) {
        guildID = guild.getId();
    }

    public void clean(JDA jda) {
        Guild guild = jda.getGuildById(guildID);
        checkGuild(guild);

        removeUnusedRoles(guild);

        for (Role role : guild.getRoles()) {
            ensureRoleKnown(role);
            setupNewPermissions(role);
        }
    }

    public PermissionStatus getPermissionStatus(Role role, Permission action) {
        checkRole(role);
        return rolePermissions.get(role.getId()).getActionStatus(action);
    }

    public void setPermissionStatus(Role role, Permission action, PermissionStatus permissionStatus) {
        checkRole(role);
        if (role.isPublicRole() && permissionStatus == PermissionStatus.DEFERRED) {
            throw new IllegalArgumentException("Cannot defer on the everyone role");
        }
        rolePermissions.get(role.getId()).setActionStatus(action, permissionStatus);
    }

    public boolean checkMemberPermission(Member member, Permission action) {
        if (!Objects.equals(member.getGuild().getId(), guildID)) {
            throw new IllegalArgumentException("Member must be in same guild as registered");
        }
        for (Role role : member.getRoles()) {
            PermissionStatus roleActionStatus = rolePermissions.get(role.getId()).getActionStatus(action);
            if (roleActionStatus != PermissionStatus.DEFERRED) {
                return roleActionStatus == PermissionStatus.ALLOWED;
            }
        }
        // If no custom roles match, check everyone role
        return rolePermissions.get(member.getGuild().getPublicRole().getId()).getActionStatus(action) == PermissionStatus.ALLOWED;
    }

    private void checkGuild(Guild guild) {
        if (guild == null) throw new IllegalStateException("Associated Guild has been deleted");
    }

    private void removeUnusedRoles(Guild guild) {
        for (String roleID : rolePermissions.keySet()) {
            if (guild.getRoleById(roleID) == null) {
                rolePermissions.remove(roleID);
            }
        }
    }

    private void ensureRoleKnown(Role role) {
        if (!rolePermissions.containsKey(role.getId())) {
            if (!role.isPublicRole()) {
                rolePermissions.put(role.getId(), new BasicPermissionSet());
            } else {
                rolePermissions.put(role.getId(), new BasicPermissionSet(Collections.emptyList(), PERMISSIBLE_ACTION_COLLECTION));
            }
        }
    }

    private void setupNewPermissions(Role role) {
        BasicPermissionSet rolePermissionSet = rolePermissions.get(role.getId());
        Set<Permission> roleKnownPermissions = rolePermissionSet.getKnownPermissions();
        for (Permission permission : PERMISSIBLE_ACTION_COLLECTION) {
            if (!roleKnownPermissions.contains(permission)) {
                if (!role.isPublicRole()) {
                    rolePermissionSet.setActionStatus(permission, PermissionStatus.DEFERRED);
                } else {
                    rolePermissionSet.setActionStatus(permission, PermissionStatus.DENIED);
                }
            }
        }
    }

    private void checkRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (!Objects.equals(role.getGuild().getId(), guildID)) {
            throw new IllegalArgumentException("Role must be in same guild as registered");
        }

        ensureRoleKnown(role);
    }
}
