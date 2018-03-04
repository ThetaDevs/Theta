package com.srgood.reasons.commands;

public interface HelpData {
    String description();

    Argument args();

    boolean visible();
}
