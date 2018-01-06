package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.utils.MemberUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandDeleteDescriptor extends BaseCommandDescriptor {
    private static final int DEFAULT_MAX = 100;

    public CommandDeleteDescriptor() {
        super(Executor::new, "If specified, deletes messages only from sender, and if specified, deletes at max the specified number of messages, otherwise 100", "{sender} {max number}", "delete", "purge");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            Predicate<Message> messagePredicate = m -> m.getCreationTime().isAfter(OffsetDateTime.now().minusWeeks(2));
            int numToDelete = DEFAULT_MAX;
            if (executionData.getParsedArguments().size() == 1) {
                try {
                    Member sender = MemberUtils.getUniqueMember(executionData.getGuild(), executionData.getParsedArguments()
                                                                                                       .get(0));
                    messagePredicate = messagePredicate.and(m -> m.getMember().equals(sender));
                } catch (IllegalArgumentException iae) {
                    try {
                        numToDelete = Integer.parseInt(executionData.getParsedArguments().get(0));
                    } catch (NumberFormatException nfe) {
                        sendOutput(iae.getMessage());
                        return;
                    }
                }
            }
            if (executionData.getParsedArguments().size() == 2) {
                Member sender = MemberUtils.getUniqueMember(executionData.getGuild(), executionData.getParsedArguments()
                                                                                                   .get(0));
                messagePredicate = messagePredicate.and(m -> m.getMember().equals(sender));
                numToDelete = Integer.parseInt(executionData.getParsedArguments().get(1));
            }
            while (numToDelete > 0) {
                List<Message> lastMessages = executionData.getChannel()
                                                          .getHistory()
                                                          .retrievePast(Math.min(numToDelete, 100))
                                                          .complete()
                                                          .stream()
                                                          .filter(messagePredicate)
                                                          .collect(Collectors.toList());
                if (lastMessages.isEmpty()) {
                    break;
                }
                if (lastMessages.size() == 1) {
                    executionData.getChannel().deleteMessageById(lastMessages.get(0).getIdLong()).queue();
                } else {
                    ((TextChannel) executionData.getChannel()).deleteMessages(lastMessages).queue();
                    numToDelete -= lastMessages.size();
                }
            }
        }
    }
}
