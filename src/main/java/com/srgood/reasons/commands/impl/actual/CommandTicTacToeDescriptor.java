package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.games.TicTacToeGame;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class CommandTicTacToeDescriptor extends BaseCommandDescriptor {
    public CommandTicTacToeDescriptor() {
        super(Executor::new, "Displays the current status of the bot","I HAVE NO WORLDLY IDEA", "tictactoe");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        private static final HashMap<MessageChannel, TicTacToeGame> tictactoe = new HashMap<>();

        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public boolean shouldExecute() {
                boolean o = true;
                if(executionData.getParsedArguments().size() == 0) {} else if(executionData.getParsedArguments().size() == 1 && Objects
                        .equals(executionData.getParsedArguments().get(0), "check")) {} else if(executionData.getParsedArguments().size() == 2) {
                    try {
                        int n = Integer.parseInt(executionData.getParsedArguments().get(0));
                        n = Integer.parseInt(executionData.getParsedArguments().get(1));
                    } catch(Exception e) {
                        o = false;
                    }
                } else {
                    o = false;
                }
                return o;
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() == 2) {
                if (tictactoe.containsKey(executionData.getChannel())) {
                    tictactoe.get(executionData.getChannel()).checkWin();
                    if (tictactoe.get(executionData.getChannel()).dead) {
                        tictactoe.remove(executionData.getChannel());
                        executionData.getChannel().sendMessage("No game currently in this channel").queue();
                    } else {
                        int ex = Integer.parseInt(executionData.getParsedArguments().get(0));
                        int why = Integer.parseInt(executionData.getParsedArguments().get(0));
                        tictactoe.get(executionData.getChannel()).play(ex, why);
                        tictactoe.get(executionData.getChannel()).checkWin();
                        if (!tictactoe.get(executionData.getChannel()).dead) {
                            if (tictactoe.get(executionData.getChannel()).valid) {
                                tictactoe.get(executionData.getChannel()).AIplay();
                                tictactoe.get(executionData.getChannel()).checkWin();
                                tictactoe.get(executionData.getChannel()).drawBoard();
                                if (tictactoe.get(executionData.getChannel()).dead) {
                                    tictactoe.remove(executionData.getChannel());
                                }
                            }
                        } else {
                            tictactoe.get(executionData.getChannel()).drawBoard();
                            tictactoe.remove(executionData.getChannel());
                        }
                    }
                } else {
                    executionData.getChannel().sendMessage("No game currently in this channel").queue();
                }
            } else if (executionData.getParsedArguments().size() == 1) {
                if (tictactoe.containsKey(executionData.getChannel())) {
                    tictactoe.get(executionData.getChannel()).debug();
                }
            } else {
                if (tictactoe.containsKey(executionData.getChannel())) {
                    sendOutput("There is already a game in this channel");
                } else {
                    tictactoe.put(executionData.getChannel(), new TicTacToeGame(executionData.getChannel()));
                }
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return super.checkCallerPermissions();
        }
    }
}
