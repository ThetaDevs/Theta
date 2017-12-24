module theta.base {
    requires java.logging;
    requires java.xml;
    requires theta.api;
    requires com.google.common;
    requires JDA;

    exports com.srgood.reasons.impl.base;
    exports com.srgood.reasons.impl.base.commands;
    exports com.srgood.reasons.impl.base.commands.descriptor;
    exports com.srgood.reasons.impl.base.commands.executor;
    exports com.srgood.reasons.impl.base.config;
}