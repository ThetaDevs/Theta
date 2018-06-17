package com.srgood.reasons.permissions;

import com.srgood.reasons.config.BasicConfigManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public interface PermissionHolderConfigManager extends BasicConfigManager {
    String PERMISSIONS_PROPERTY_NAME = "permissions";

    default void setPermissions(Map<String, PermissionStatus> permissions) {
        Map<String, PermissionStatus> serializableMap = permissions instanceof Serializable ? permissions : new HashMap<>(permissions);
        setSerializedProperty(PERMISSIONS_PROPERTY_NAME, serializableMap);
    }

    default Map<String, PermissionStatus> getPermissions() {
        return getSerializedProperty(PERMISSIONS_PROPERTY_NAME, new HashMap<>());
    }
}
