package com.srgood.reasons.impl.commands.utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleUtils {

    public static List<Role> getRolesByName(Guild guild,String name) {
        return getRolesByName(guild.getRoles(),name);
    }

    public static List<Role> getRolesByName(List<Role> roleList,String name) {
        return roleList.stream().filter(role -> role.getName().equalsIgnoreCase(name)).collect(Collectors.toCollection(LinkedList::new));
    }

    public static Role getUniqueRole(Guild guild, String nameOrID) {
        if (nameOrID.equalsIgnoreCase("everyone")) {
            return guild.getPublicRole();
        }

        final List<Role> foundRoles = getRolesByName(guild, nameOrID);

        if (foundRoles.size() < 1) {
            Role roleById = guild.getRoleById(nameOrID);
            if (roleById == null) {
                throw new IllegalArgumentException(String.format("Found no roles called `%s`", nameOrID));
            } else {
                return roleById;
            }
        } else if (foundRoles.size() > 1) {
            throw new IllegalArgumentException(String.format("Found more than one role by the name `%s`. Please use a specific ID. These can be found with #!roles.", nameOrID));
        } else {
            return foundRoles.get(0);
        }
    }
}
