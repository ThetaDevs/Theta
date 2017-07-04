package com.srgood.reasons.impl.base.config;

import com.srgood.reasons.config.MemberConfigManager;
import org.w3c.dom.Element;

public class MemberConfigManagerImpl extends BasicConfigManagerImpl implements MemberConfigManager {
    public MemberConfigManagerImpl(Element operationalElement) {
        super(operationalElement);
    }
}
