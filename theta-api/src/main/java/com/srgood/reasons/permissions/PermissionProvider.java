package com.srgood.reasons.permissions;

import net.dv8tion.jda.core.entities.IPermissionHolder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.*;
import java.util.stream.Collectors;

public interface PermissionProvider extends AutoCloseable {
    /// MUST return a map that can be modified without other side-effects (eg is independent of this Object)
    Map<String, PermissionStatus> getPermissionsMap(IPermissionHolder permissionHolder);

    void setPermissions(IPermissionHolder permissionHolder, Map<String, PermissionStatus> permissions);

    default Set<String> getActualPermissions(IPermissionHolder permissionHolder) {
        return new HashSet<String>() {{
            addAll(getRegisteredPermissions().stream()
                                             .filter(p -> checkPermission(permissionHolder, p))
                                             .collect(Collectors.toSet()));
        }};
    }

    Set<String> getRegisteredPermissions();

    default void registerPermissions(String... permissions) {
        registerPermissions(Arrays.asList(permissions));
    }

    void registerPermissions(Collection<String> permissions);

    default PermissionStatus getPermission(IPermissionHolder permissionHolder, String permission) {
        PermissionStatus mapValue = getPermissionsMap(permissionHolder).get(permission);
        boolean isEveryoneRole = permissionHolder instanceof Role && ((Role) permissionHolder).isPublicRole();
        // If @everyone role, ALLOWED by default, otherwise DEFERRED
        return mapValue != null ? mapValue : isEveryoneRole ? PermissionStatus.ALLOWED : PermissionStatus.DEFERRED;
    }

    default void setPermission(IPermissionHolder permissionHolder, String permission, PermissionStatus permissionStatus) {
        Map<String, PermissionStatus> permissionMap = getPermissionsMap(permissionHolder);
        if (permissionMap.get(permission) != permissionStatus) { // Don't write if there is no change
            permissionMap.put(permission, permissionStatus);
            setPermissions(permissionHolder, permissionMap);
        }
    }

    default boolean checkPermission(IPermissionHolder permissionHolder, String permission) {
        if (permissionHolder instanceof Role) {
            return getPermission(permissionHolder, permission) == PermissionStatus.ALLOWED;
        } else {
            if (getPermission(permissionHolder, permission) == PermissionStatus.ALLOWED)
                return true;

            List<Role> roles = ((Member) permissionHolder).getRoles();
            for (Role role : roles) {
                if (getPermission(role, permission) == PermissionStatus.ALLOWED) return true;
                if (getPermission(role, permission) == PermissionStatus.DENIED) return false;
            }
            return checkPermission(permissionHolder.getGuild().getPublicRole(), permission);
        }
    }
}
