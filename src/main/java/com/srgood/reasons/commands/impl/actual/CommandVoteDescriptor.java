package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.Reference;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.permissions.Permission;
import com.srgood.reasons.permissions.PermissionChecker;
import com.srgood.reasons.utils.ImageUtils;
import com.srgood.reasons.utils.Vote;
import net.dv8tion.jda.core.entities.Channel;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CommandVoteDescriptor extends BaseCommandDescriptor {
    public CommandVoteDescriptor() {
        super(Executor::new, "Starts a vote","<duration (seconds)> <option 1> <option 2> <...>", "vote");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() >= 3 && executionData.getParsedArguments().size() <= Reference.COLORS.length + 2) {
                Map<String,Integer> voteMap = new LinkedHashMap<>();

                for (int i = 2; i < executionData.getParsedArguments().size(); i++) {
                    voteMap.put(executionData.getParsedArguments().get(i), 0);
                }

                sendOutput("A vote has started! use `vote <option>` to vote!");
                new Vote(voteMap, Integer.parseInt(executionData.getParsedArguments().get(0)), (Channel) executionData.getChannel(), new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder sb = new StringBuilder();

                        for (String st : voteMap.keySet()) {
                            sb.append(st).append(": ").append(voteMap.get(st)).append("\n");
                        }
                        sendOutput(sb.toString());
                        try {
                            executionData.getChannel().sendFile(ImageUtils.renderVote(
                                    executionData.getParsedArguments().get(1), voteMap.keySet().toArray(new String[0]),
                                    integerArrToIntArr(
                                            voteMap.values()
                                                    .<Integer>toArray(
                                                            new Integer[] {}
                                                    )
                                    )
                            ), null).queue();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    private int[] integerArrToIntArr(Integer[] integers) {
                        int[] ret = new int[integers.length];
                        for (int i = 0; i < integers.length; i++) {
                            ret[i] = integers[i];
                        }
                        return ret;
                    }
                });
            } else {
                sendOutput("Incorrect arguments, correct usage: " + ConfigUtils.getGuildPrefix(executionData.getGuild()) + "vote <duration (seconds)> <option 1> <option 2> ... [option 5 (up to " +  Reference.COLORS.length + " max)]");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getSender(), Permission.START_VOTE);
        }
    }
}
