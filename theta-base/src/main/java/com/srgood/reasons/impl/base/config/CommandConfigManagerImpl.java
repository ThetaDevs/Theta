package com.srgood.reasons.impl.base.config;

import com.srgood.reasons.config.CommandConfigManager;
import org.w3c.dom.Element;

public class CommandConfigManagerImpl extends BasicConfigManagerImpl implements CommandConfigManager {
    public CommandConfigManagerImpl(Element commandElement) {
        super(commandElement);
    }
}
