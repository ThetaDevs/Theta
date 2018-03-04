package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.permissions.Permission;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.srgood.reasons.impl.base.BaseConstants.GLOBAL_RANDOM;

public class CommandDiceRollDescriptor extends BaseCommandDescriptor {
    private static final int MAX_ROLL = 6;
    private static final int MAX_DICE = 50;

    public CommandDiceRollDescriptor() {
        super(Executor::new, "Rolls some dice", null, "diceroll");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() > 0) {
                int numRolls;
                if (Integer.parseInt(executionData.getParsedArguments().get(0)) > MAX_DICE) {
                    sendOutput("Whoa there, Im not going to roll %s dice, how about %s instead?", executionData.getParsedArguments()
                                                                                                               .get(0), MAX_DICE);
                    numRolls = MAX_DICE;
                } else {
                    numRolls = Integer.parseInt(executionData.getParsedArguments().get(0));
                }

                List<Integer> results = new ArrayList<>();

                for (int roll = 0; roll < numRolls; roll++) {
                    int randNum = GLOBAL_RANDOM.nextInt(MAX_ROLL) + 1;
                    results.add(randNum);
                }

                String output = String.join(", ", results.stream()
                                                         .map(x -> Integer.toString(x))
                                                         .collect(Collectors.toList())) + ".";

                sendSuccess(output);
            } else {
                sendSuccess(GLOBAL_RANDOM.nextInt(MAX_ROLL) + 1 + ".");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager()
                                                                        .getConfigManager(), executionData.getSender(), Permission.DO_CHANCE_GAME);
        }
    }
}
