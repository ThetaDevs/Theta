module theta.commands {
    requires java.desktop;
    requires java.logging;
    requires java.management;
    requires theta.api;
    requires theta.base;
    requires com.google.common;
    requires commons.codec;
    requires JDA;
    requires org.eclipse.jgit;

    exports com.srgood.reasons.impl.commands.main;
    exports com.srgood.reasons.impl.commands.utils;
}