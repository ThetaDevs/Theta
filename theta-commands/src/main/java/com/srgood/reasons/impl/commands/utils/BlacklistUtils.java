package com.srgood.reasons.impl.commands.utils;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class BlacklistUtils {
    public static boolean isBlacklisted(BotManager botManager, Member member, List<String> blacklist) {
        return PermissionChecker.checkBotAdmin(member)
                                .isPresent() && !botManager.getPermissionProvider().checkPermission(member, "blacklist") && (member.getRoles()
                                                                                        .stream()
                                                                                        .map(Role::getId)
                                                                                        .anyMatch(blacklist::contains) || blacklist
                .contains(member.getUser().getId()));
    }
}
