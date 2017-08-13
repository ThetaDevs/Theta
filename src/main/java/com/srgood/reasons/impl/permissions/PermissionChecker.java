package com.srgood.reasons.impl.permissions;

import com.srgood.reasons.config.BotConfigManager;
import com.srgood.reasons.impl.Reference;
import com.srgood.reasons.impl.utils.GuildDataManager;
import net.dv8tion.jda.core.entities.Member;

import java.util.Optional;

/**
 * The methods in this class take a {@link net.dv8tion.jda.core.entities.Member} (and possibly some other arguments), returning an empty {@link java.util.Optional} if the check succeeds, otherwise a non-empty {@link java.util.Optional}
 */
public class PermissionChecker {
    /**
     * Returns an {@link java.util.Optional} containing an error if the {@link net.dv8tion.jda.core.entities.Member} does not have the permission to perform the {@link Permission}, otherwise an empty {@link java.util.Optional}.
     * If the {@link net.dv8tion.jda.core.entities.Member} is the owner of their {@link net.dv8tion.jda.core.entities.Guild}, as returned by {@link net.dv8tion.jda.core.entities.Member#isOwner()}, then the {@link java.util.Optional} will be empty.
     *
     * @param member The {@link net.dv8tion.jda.core.entities.Member} to check the permission for
     * @param action The {@link Permission} to check
     *
     * @return A non-empty {@link java.util.Optional} if the {@link net.dv8tion.jda.core.entities.Member} does not have the permission required, otherwise an empty {@link java.util.Optional}
     */
    public static Optional<String> checkMemberPermission(BotConfigManager botConfigManager, Member member, Permission action) {
        if (member.isOwner() || member.hasPermission(net.dv8tion.jda.core.Permission.ADMINISTRATOR) || !checkBotAdmin(member).isPresent()) {
            return Optional.empty();
        }
        if (!GuildDataManager.getGuildPermissionSet(botConfigManager, member.getGuild()).checkMemberPermission(member, action)) {
            return Optional.of(String.format("You do not have permission to perform the action **`%s`**", action));
        }

        return Optional.empty();
    }

    /**
     * Returns an {@link java.util.Optional} containing an error if the {@link net.dv8tion.jda.core.entities.Member} is not an administrator for the bot, otherwise an empty optional.
     * Currently, the only way to be an administrator is to be a recognized developer, as in {@link com.srgood.reasons.impl.Reference#BOT_DEVELOPERS}.
     *
     * @param member The {@link net.dv8tion.jda.core.entities.Member} to check for being a bot admin.
     *
     * @return A non-empty {@link java.util.Optional} if the {@link net.dv8tion.jda.core.entities.Member} is not an administrator for the bot, otherwise an empty {@link java.util.Optional}
     */
    public static Optional<String> checkBotAdmin(Member member) {
        if (!Reference.BOT_DEVELOPERS.contains(member.getUser().getId())) {
            return Optional.of("You are not a bot owner.");
        }

        return Optional.empty();
    }
}
