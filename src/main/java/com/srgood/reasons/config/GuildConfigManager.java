package com.srgood.reasons.config;

import com.srgood.reasons.commands.CommandDescriptor;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public interface GuildConfigManager extends BasicConfigManager {
    String PREFIX_PROPERTY_NAME = "prefix";
    String DEFAULT_PREFIX = "#!";

    MemberConfigManager getMemberConfigManager(Member member);
    RoleConfigManager getRoleConfigManager(Role role);
    ChannelConfigManager getChannelConfigManager(Channel channel);
    CommandConfigManager getCommandConfigManager(CommandDescriptor command);

    default String getPrefix() {
        return getProperty(PREFIX_PROPERTY_NAME, DEFAULT_PREFIX, true);
    }

    default void setPrefix(String prefix) {
        setProperty(PREFIX_PROPERTY_NAME, prefix);
    }

    void delete();
}
