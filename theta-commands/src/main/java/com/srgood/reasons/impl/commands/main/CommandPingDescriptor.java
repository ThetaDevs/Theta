package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.impl.base.commands.descriptor.SimpleTextCommandDescriptor;

public class CommandPingDescriptor extends SimpleTextCommandDescriptor {
    public CommandPingDescriptor() {
        super("ping", "Pong!");
    }
}
