package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.impl.base.commands.descriptor.SimpleTextCommandDescriptor;

public class CommandPongDescriptor extends SimpleTextCommandDescriptor {
    public CommandPongDescriptor() {
        super("pong", "Ping!");
    }
}
