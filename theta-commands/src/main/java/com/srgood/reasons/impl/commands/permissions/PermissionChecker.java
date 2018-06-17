package com.srgood.reasons.impl.commands.permissions;

import com.srgood.reasons.impl.base.BaseConstants;
import net.dv8tion.jda.core.entities.Member;

import java.util.Optional;

/**
 * The methods in this class take a {@link net.dv8tion.jda.core.entities.Member} (and possibly some other arguments), returning an empty {@link java.util.Optional} if the check succeeds, otherwise a non-empty {@link java.util.Optional}
 */
public class PermissionChecker {
    /**
     * Returns an {@link java.util.Optional} containing an error if the {@link net.dv8tion.jda.core.entities.Member} is not an administrator for the bot, otherwise an empty optional.
     * Currently, the only way to be an administrator is to be a recognized developer, as in {@link com.srgood.reasons.impl.base.BaseConstants#BOT_DEVELOPERS}.
     *
     * @param member The {@link net.dv8tion.jda.core.entities.Member} to check for being a bot admin.
     *
     * @return A non-empty {@link java.util.Optional} if the {@link net.dv8tion.jda.core.entities.Member} is not an administrator for the bot, otherwise an empty {@link java.util.Optional}
     */
    public static Optional<String> checkBotAdmin(Member member) {
        if (!BaseConstants.BOT_DEVELOPERS.contains(member.getUser().getId())) {
            return Optional.of("You are not a bot owner.");
        }

        return Optional.empty();
    }
}
