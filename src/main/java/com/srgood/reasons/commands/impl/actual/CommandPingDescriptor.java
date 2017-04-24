package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.impl.base.descriptor.SimpleTextCommandDescriptor;

public class CommandPingDescriptor extends SimpleTextCommandDescriptor {
    public CommandPingDescriptor() {
        super("ping", "Pong!");
    }
}
