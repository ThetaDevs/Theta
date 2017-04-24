package com.srgood.reasons.utils;

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
        return memberList.stream().filter(m -> Objects.equals(m.getOnlineStatus(), OnlineStatus.ONLINE)).collect(Collectors.toCollection(LinkedList::new));
    }


    public static List<Member> getMembersWithRole(Guild guild, Role role) {
        return getMembersWithRole(guild.getMembers(),role);
    }

    public static List<Member> getMembersWithRole(List<Member> memberList, Role role) {
        return memberList.stream().filter(m -> m.getRoles().contains(role)).collect(Collectors.toCollection(LinkedList::new));
    }
}
