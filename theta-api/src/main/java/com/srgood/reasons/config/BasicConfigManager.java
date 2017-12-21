package com.srgood.reasons.config;

import java.util.Collections;
import java.util.Map;

public interface BasicConfigManager {
    default String getProperty(String property) {
        return getProperty(property, null);
    }
    String getProperty(String property, String defaultValue);
    void setProperty(String property, String value);

    default <T> T getSerializedProperty(String property) {
        return getSerializedProperty(property,  null);
    }
    default <T> T getSerializedProperty(String property, T defaultValue) {
        return getSerializedProperty(property, defaultValue, Collections.emptyMap());
    }
    <T> T getSerializedProperty(String property, T defaultValue, Map<String, String> classReplacements);
    void setSerializedProperty(String property, Object value);
}
