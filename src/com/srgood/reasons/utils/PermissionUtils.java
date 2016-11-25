package com.srgood.reasons.utils.Permissions;

import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.utils.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.managers.RoleManager;

import java.util.Collection;

public class PermissionUtils {

    public static Collection<PermissionLevels> getPermissions(Guild guild, User user) {
        return rolesToPermissions(guild, guild.getMember(user).getRoles());
    }


    private static Collection<PermissionLevels> rolesToPermissions(Guild guild, Collection<? extends Role> roles) {
        return roles.stream().map(role -> ConfigUtils.roleToPermission(role)).collect(java.util.stream.Collectors.toList());
    }

    private static boolean containsAny(Collection<?> container, Collection<?> checkFor) {
        for (Object o : checkFor) {
            if (container.contains(o)) {
                return true;
            }
        }
        return false;
    }

    public static PermissionLevels intToEnum(int level) {
        for (PermissionLevels p : PermissionLevels.values()) {
            if (p.level == level) {
                return p;
            }
        }
        return null;
    }

    /**
     * @deprecated
     */
    public static int enumToInt (PermissionLevels level) {
        return level.getLevel();
    }


    public static Role createRole(PermissionLevels roleLevel, Guild guild, boolean addToXML) throws RateLimitedException {
        if (!roleLevel.isVisible()) return null;

        RoleManager roleMgr = guild.getController().createRole().block().getManager();
        roleMgr.setName(roleLevel.getReadableName()).queue();
        roleMgr.setColor(roleLevel.getColor()).queue();

        Role role = roleMgr.getRole();

        if (addToXML) {
            ConfigUtils.registerRoleConfig(guild, role, roleLevel);
        }


        return role;
    }

    public static PermissionLevels stringToRole(String uRole) {
        System.out.println("" + uRole);

        for (PermissionLevels p : PermissionLevels.values()) {
            if (p.getXMLName().toLowerCase().equals(uRole.toLowerCase())) {
                return p;
            }
        }
        return PermissionLevels.STANDARD;
    }
}
