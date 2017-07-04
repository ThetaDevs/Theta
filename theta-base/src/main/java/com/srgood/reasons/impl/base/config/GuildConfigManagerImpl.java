package com.srgood.reasons.impl.base.config;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.config.*;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.w3c.dom.Element;

import java.util.Collections;

public class GuildConfigManagerImpl extends BasicConfigManagerImpl implements GuildConfigManager {
    private static final String ROLES_TAG_NAME = "roles";
    private static final String ROLE_TAG_NAME = "role";
    private static final String ROLE_ID_ATTRIBUTE_NAME = "id";
    private static final String MEMBERS_TAG_NAME = "members";
    private static final String MEMBER_TAG_NAME = "member";
    private static final String MEMBER_ID_ATTRIBUTE_NAME = "id";
    private static final String CHANNELS_TAG_NAME = "channels";
    private static final String CHANNEL_TAG_NAME = "channel";
    private static final String CHANNEL_ID_ATTRIBUTE_NAME = "id";
    private static final String COMMANDS_TAG_NAME = "commands";
    private static final String COMMAND_TAG_NAME = "command";
    private static final String COMMAND_NAME_ATTRIBUTE_NAME = "name";

    private Element guildElement;

    public GuildConfigManagerImpl(Element guildElement) {
        super(guildElement);
        this.guildElement = guildElement;
    }

    @Override
    public MemberConfigManager getMemberConfigManager(Member member) {
        checkState();
        Element roleElement = ConfigUtils.getOrCreateChildElementWithAttributes(
                ConfigUtils.getOrCreateChildElement(
                        guildElement,
                        MEMBERS_TAG_NAME),
                MEMBER_TAG_NAME,
                Collections.singletonMap(MEMBER_ID_ATTRIBUTE_NAME, member.getUser().getId()));
        return new MemberConfigManagerImpl(roleElement);
    }

    @Override
    public RoleConfigManager getRoleConfigManager(Role role) {
        checkState();
        Element roleElement = ConfigUtils.getOrCreateChildElementWithAttributes(
                ConfigUtils.getOrCreateChildElement(
                        guildElement,
                        ROLES_TAG_NAME),
                ROLE_TAG_NAME, Collections.singletonMap(ROLE_ID_ATTRIBUTE_NAME, role.getId()));
        return new RoleConfigManagerImpl(roleElement);
    }

    @Override
    public ChannelConfigManager getChannelConfigManager(Channel channel) {
        checkState();
        Element channelElement = ConfigUtils.getOrCreateChildElementWithAttributes(
                ConfigUtils.getOrCreateChildElement(
                        guildElement,
                        CHANNELS_TAG_NAME),
                CHANNEL_TAG_NAME,
                Collections.singletonMap(CHANNEL_ID_ATTRIBUTE_NAME, channel.getId()));
        return new ChannelConfigManagerImpl(channelElement);
    }

    @Override
    public CommandConfigManager getCommandConfigManager(CommandDescriptor command) {
        checkState();
        Element commandElement = ConfigUtils.getOrCreateChildElementWithAttributes(
                ConfigUtils.getOrCreateChildElement(
                        guildElement,
                        COMMANDS_TAG_NAME),
                COMMAND_TAG_NAME,
                Collections.singletonMap(COMMAND_NAME_ATTRIBUTE_NAME, command.getPrimaryName()));
        return new CommandConfigManagerImpl(commandElement);
    }

    @Override
    public void delete() {
        checkState();
        guildElement.getParentNode().removeChild(guildElement);
        guildElement = null;
    }

    private void checkState() {
        if (guildElement == null) {
            throw new IllegalStateException("This GuildConfigManager has been deleted. If more operations must be done, get another one from the BotConfigManager.");
        }
    }
}
