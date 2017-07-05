package com.srgood.reasons.config;

public interface CommandConfigManager extends BasicConfigManager {
    String ENABLED_PROPERTY_NAME = "isEnabled";
    boolean DEFAULT_ENABLED_STATE = true;

    default boolean isEnabled() {
        return Boolean.parseBoolean(getProperty(ENABLED_PROPERTY_NAME, String.valueOf(DEFAULT_ENABLED_STATE)));
    }
    default void setEnabled(boolean enabled){
        setProperty(ENABLED_PROPERTY_NAME, String.valueOf(enabled));
    }
}
