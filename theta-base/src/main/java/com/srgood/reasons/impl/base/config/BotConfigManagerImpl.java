package com.srgood.reasons.impl.base.config;

import com.srgood.reasons.config.BotConfigManager;
import com.srgood.reasons.config.GuildConfigManager;
import net.dv8tion.jda.core.entities.Guild;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class BotConfigManagerImpl extends BasicConfigManagerImpl implements BotConfigManager {

    private static String GUILDS_TAG_NAME = "servers";
    private static String GUILD_TAG_NAME = "server";
    private static String GUILD_ID_ATTRIBUTE_NAME = "id";

    private final ConfigFileManager fileManager;

    private final ReadWriteLock documentLock = new ReentrantReadWriteLock();

    public BotConfigManagerImpl(ConfigFileManager fileManager) {
        super(fileManager.parse().getDocument().getDocumentElement());
        this.fileManager = fileManager;
    }

    @Override
    public GuildConfigManager getGuildConfigManager(Guild guild) {
        Map<String, String> attributeHashMap = new HashMap<>();
        attributeHashMap.put(GUILD_ID_ATTRIBUTE_NAME, guild.getId());
        return new GuildConfigManagerImpl(ConfigUtils.getOrCreateChildElementWithAttributes(ConfigUtils.getOrCreateChildElement(applyOnDocument(Document::getDocumentElement), GUILDS_TAG_NAME), GUILD_TAG_NAME, attributeHashMap));
    }

    public <T> T applyOnDocument(Function<Document, T> function) {
        return applyOnDocument(function, false);
    }

    public <T> T applyOnDocument(Function<Document, T> function, boolean readOnly) {
        Lock actualLock = readOnly ? documentLock.readLock() : documentLock.writeLock();
        actualLock.lock();

        try {
            return function.apply(fileManager.getDocument());
        } finally {
            actualLock.unlock();
        }
    }

    @Override
    public void close() {
        fileManager.save();
    }
}
