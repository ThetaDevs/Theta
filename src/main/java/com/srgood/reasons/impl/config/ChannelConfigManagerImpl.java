package com.srgood.reasons.impl.config;

import com.srgood.reasons.config.ChannelConfigManager;
import org.w3c.dom.Element;

public class ChannelConfigManagerImpl extends BasicConfigManagerImpl implements ChannelConfigManager {
    public ChannelConfigManagerImpl(Element channelElement) {
        super(channelElement);
    }
}
