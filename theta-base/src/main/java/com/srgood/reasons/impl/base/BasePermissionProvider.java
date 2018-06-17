package com.srgood.reasons.impl.base;

import com.srgood.reasons.permissions.PermissionProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class BasePermissionProvider implements PermissionProvider {
    private static Set<String> registeredPermissions = new HashSet<>();

    @Override
    public void registerPermissions(Collection<String> permissions) {
        registeredPermissions.addAll(permissions);
    }

    @Override
    public Set<String> getRegisteredPermissions() {
        return new HashSet<>(registeredPermissions);
    }
}
