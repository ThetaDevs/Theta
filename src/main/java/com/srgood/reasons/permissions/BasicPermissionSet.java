package com.srgood.reasons.permissions;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.srgood.reasons.permissions.PermissionStatus.*;

public class BasicPermissionSet implements Serializable {

    private Map<Permission, PermissionStatus> permissionMap = new HashMap<>();



    public BasicPermissionSet(Collection<Permission> allowedActions) {
        this(allowedActions, null, null);
    }

    public BasicPermissionSet(Collection<Permission> allowedActions, Collection<Permission> deniedActions) {
        this(allowedActions, deniedActions, null);
    }

    public BasicPermissionSet(Collection<Permission> allowedActions, Collection<Permission> deniedActions, Collection<Permission> deferredActions) {
        this();

        if (allowedActions != null) {
            for (Permission action : allowedActions) {
                permissionMap.put(action, ALLOWED);
            }
        }

        if (deniedActions != null) {
            for (Permission action : deniedActions) {
                permissionMap.put(action, DENIED);
            }
        }

        if (deferredActions != null) {
            for (Permission action : deferredActions) {
                permissionMap.put(action, DEFERRED);
            }
        }
    }

    public BasicPermissionSet(BasicPermissionSet setToDuplicate) {
        permissionMap = new HashMap<>(new HashMap<>(setToDuplicate.permissionMap));
    }

    public BasicPermissionSet() {
        populatePermissionMap();
    }

    private void populatePermissionMap() {
        for (Permission action : Permission.values()) {
            if (!permissionMap.containsKey(action)) {
                permissionMap.put(action, DEFERRED);
            }
        }
    }



    public Set<Permission> getAllowedActions() {
        return getActionsByStatus(ALLOWED);
    }

    public Set<Permission> getDeniedActions() {
        return getActionsByStatus(DENIED);
    }

    public Set<Permission> getDeferredActions() {
        return getActionsByStatus(DEFERRED);
    }

    public Set<Permission> getActionsByStatus(PermissionStatus action) {
        return permissionMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() == action)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());
    }

    public PermissionStatus getActionStatus(Permission action) {
        return permissionMap.get(action);
    }

    public void setActionStatus(Permission action, PermissionStatus status) {
        permissionMap.put(action, status);
    }

}
