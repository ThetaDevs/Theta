package com.srgood.reasons.impl.commands.utils;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MemberUtils {

    public static List<Member> getOnlineMembers(Guild guild) {
        return getOnlineMembers(guild.getMembers());
    }

    public static List<Member> getOnlineMembers(List<Member> memberList) {
        return memberList.stream()
                         .filter(m -> Objects.equals(m.getOnlineStatus(), OnlineStatus.ONLINE))
                         .collect(Collectors.toCollection(LinkedList::new));
    }


    public static List<Member> getMembersWithRole(Guild guild, Role role) {
        return getMembersWithRole(guild.getMembers(), role);
    }

    public static List<Member> getMembersWithRole(List<Member> memberList, Role role) {
        return memberList.stream()
                         .filter(m -> m.getRoles().contains(role))
                         .collect(Collectors.toCollection(LinkedList::new));
    }

    public static List<Member> getMembersByName(Guild guild, String name) {
        return getMembersByName(guild.getMembers(), name);
    }

    public static List<Member> getMembersByName(List<Member> memberList, String name) {
        return memberList.stream().filter(m -> m.getEffectiveName().equals(name)).collect(Collectors.toList());
    }

    public static Member getUniqueMember(Guild guild, String nameOrID) {
        final List<Member> foundMembers = getMembersByName(guild, nameOrID);

        if (foundMembers.size() < 1) {
            Member memberById = guild.getMemberById(nameOrID);
            if (memberById == null) {
                throw new IllegalArgumentException(String.format("Found no members called `%s`", nameOrID));
            } else {
                return memberById;
            }
        } else if (foundMembers.size() > 1) {
            throw new IllegalArgumentException(String.format("Found more than one member by the name `%s`. Please use a specific ID.", nameOrID));
        } else {
            return foundMembers.get(0);
        }
    }
}
