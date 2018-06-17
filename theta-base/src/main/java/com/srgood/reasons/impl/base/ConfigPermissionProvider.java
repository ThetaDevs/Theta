package com.srgood.reasons.impl.base;

import com.srgood.reasons.config.BotConfigManager;
import com.srgood.reasons.config.GuildConfigManager;
import com.srgood.reasons.permissions.PermissionHolderConfigManager;
import com.srgood.reasons.permissions.PermissionProvider;
import com.srgood.reasons.permissions.PermissionStatus;
import net.dv8tion.jda.core.entities.IPermissionHolder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.Map;

public class ConfigPermissionProvider extends BasePermissionProvider implements PermissionProvider {
    private final BotConfigManager botConfigManager;

    public ConfigPermissionProvider(BotConfigManager botConfigManager) {
        this.botConfigManager = botConfigManager;
    }

    @Override
    public void close() {}

    @Override
    public Map<String, PermissionStatus> getPermissionsMap(IPermissionHolder permissionHolder) {
        return permissionHolderConfigManager(permissionHolder).getPermissions();
    }

    private PermissionHolderConfigManager permissionHolderConfigManager(IPermissionHolder member) {
        GuildConfigManager guildConfigManager = botConfigManager.getGuildConfigManager(member.getGuild());
        return
            member instanceof Role ?
            guildConfigManager.getRoleConfigManager((Role) member) :
            guildConfigManager.getMemberConfigManager((Member) member);
    }

    @Override
    public void setPermissions(IPermissionHolder permissionHolder, Map<String, PermissionStatus> permissions) {
        permissionHolderConfigManager(permissionHolder).setPermissions(permissions);
    }
}
