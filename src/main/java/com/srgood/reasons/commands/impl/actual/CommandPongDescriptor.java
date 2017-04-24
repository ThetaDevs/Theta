package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.impl.base.descriptor.SimpleTextCommandDescriptor;

public class CommandPongDescriptor extends SimpleTextCommandDescriptor {
    public CommandPongDescriptor() {
        super("pong", "Ping!");
    }
}
