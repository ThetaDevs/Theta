package com.srgood.reasons.commands;

import java.awt.*;

public enum PermissionLevels {
    DEVELOPER(99, "DEV", "Bot Developer", false, Color.red), ADMINISTRATOR(3, "ADMIN", "Reasons Admin", true, Color.green), MUSIC_DJ(2, "MUSIC_DJ", "DJ", true, Color.cyan), STANDARD(0, "STANDARD", "Standard", false, Color.BLACK);

    public final int level;
    final String xmlName;
    final String readableName;
    final Color color;
    final Boolean visible;


    PermissionLevels(int lvl, String xmlName, String readableName, Boolean visible, Color color) {
        level = lvl;
        this.xmlName = xmlName;
        this.readableName = readableName;
        this.color = color;
        this.visible = visible;
    }


    public int getLevel() {
        return level;
    }

    public String getXMLName() {
        return xmlName;
    }

    public String getReadableName() {
        return readableName;
    }

    public Color getColor() {
        return color;
    }

    public Boolean isVisible() {
        return visible;
    }
}
