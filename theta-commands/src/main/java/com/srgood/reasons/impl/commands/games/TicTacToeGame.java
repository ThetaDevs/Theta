package com.srgood.reasons.impl.commands.games;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Arrays;

public class TicTacToeGame {
    public final int[][] magicSquare = {
        {4, 9, 2},
        {3, 5, 7},
        {8, 1, 6}
    };
    public final String[][] board = {
        {" ", " ", " "},
        {" ", " ", " "},
        {" ", " ", " "}
    };
    public int[] x = new int[0];
    public int[] o = new int[0];
    public boolean turn = false;
    public boolean ready = false;
    public final String[] boardFormat = {" ", " | ", " | ", "\n-----------\n"};
    public final MessageChannel channel;
    public boolean dead = false;
    public boolean valid = true;
    public TicTacToeGame(MessageChannel ch) {
        channel = ch;
        StringBuilder outputBuilder = new StringBuilder();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                outputBuilder.append(boardFormat[j]);
                outputBuilder.append(board[i][j]);
            }
            if(i < 2) {
                outputBuilder.append(boardFormat[3]);
            }
        }
        channel.sendMessage(outputBuilder.toString()).queue();
    }
    public void play(int X, int Y) {
        x = Arrays.copyOf(x, x.length+1);
        valid = true;
        if(X > 3 || X < 1 || Y > 3 || Y < 1) {
            valid = false;
        }
        for(int i = 0; i < o.length; i++) {
            if(x[x.length-1] == o[i]) {
                valid = false;
            }
        }
        for(int i = 0; i < x.length - 1; i++) {
            if(x[x.length-1] == x[i]) {
                valid = false;
            }
        }
        if(valid) {
            turn = true;
            x[x.length-1] = magicSquare[Y-1][X-1];
        } else {
            x = Arrays.copyOf(x, x.length-1);
            channel.sendMessage("Please input a valid position").queue();
        }
    }
    public void AIplay() {
        o = Arrays.copyOf(o, o.length+1);
        int out;
        int finout = 0;
        int t;
        if(o.length >= 2) {
            //Check for win
            for(int i = 0; i < o.length-1; i++) {
                for(int j = 0; j < o.length-1; j++) {
                    if(i != j) {
                        t = o[i] + o[j];
                        if(t < 15) {
                            t = 15 - t;
                            if(t != o[i] && t != o[j] && t < 10) {
                                out = t;
                                for(int k = 0; k < x.length; k++) {
                                    if (x[k] == t) {
                                        out = 0;
                                    }
                                }
                                for(int k = 0; k < o.length-1; k++) {
                                    if(o[k] == t) {
                                        out = 0;
                                    }
                                }
                                if (out != 0) {
                                    finout = out;
                                    //System.out.println(finout + ", " + o[i] + ", " + o[j]);
                                }
                            }
                        }
                    }
                }
            }
            if(finout != 0) {
                o[o.length - 1] = finout;
            } else {
                //check for block
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        if(i != j) {
                            t = x[i]+x[j];
                            if(t < 15) {
                                t = 15 - t;
                                if(t != x[i] && t != x[j] && t < 10) {
                                    out = t;
                                    for(int k = 0; k < o.length-1; k++) {
                                        if(o[k] == t) {
                                            out = 0;
                                        }
                                    }
                                    for(int k = 0; k < x.length; k++) {
                                        if (x[k] == t) {
                                            out = 0;
                                        }
                                    }
                                    if(out != 0) {
                                        finout = out;
                                        //System.out.println(finout + ", " + x[i] + ", " + x[j]);
                                    }
                                }
                            }
                        }
                    }
                } if(finout != 0) {
                    o[o.length - 1] = finout;
                } else {
                    boolean loop = true;
                    while(loop) {
                        o[o.length-1] = (int)(Math.floor(Math.random()*9)+1);
                        loop = false;
                        for(int i = 0; i < x.length; i++) {
                            if(o[o.length-1] == x[i])
                                loop = true;
                        }
                        for(int i = 0; i < o.length - 1; i++) {
                            if(o[o.length-1] == o[i])
                                loop = true;
                        }
                    }
                }
            }
        } else {
            boolean loop = true;
            while(loop) {
                o[o.length-1] = (int)(Math.floor(Math.random()*9)+1);
                loop = false;
                for(int i = 0; i < x.length; i++) {
                    if(o[o.length-1] == x[i])
                        loop = true;
                }
                for(int i = 0; i < o.length - 1; i++) {
                    if(o[o.length-1] == o[i])
                        loop = true;
                }
            }
        }
        turn = false;
    }
    public void drawBoard() {
        for(int i = 0; i < x.length; i++) {
            for(int j = 0; j < 3; j++) {
                for(int k = 0; k < 3; k++) {
                    if(x[i] == magicSquare[j][k]) {
                        board[j][k] = "X";
                    }
                }
            }
        }
        for(int i = 0; i < o.length; i++) {
            for(int j = 0; j < 3; j++) {
                for(int k = 0; k < 3; k++) {
                    if(o[i] == magicSquare[j][k]) {
                        board[j][k] = "O";
                    }
                }
            }
        }
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                output.append(boardFormat[j]);
                output.append(board[i][j]);
            }
            if(i < 2)
                output.append(boardFormat[3]);
        }
        channel.sendMessage(output.toString()).queue();
    }
    public void checkWin() {
        if(x.length == 5 || o.length == 5) {
            channel.sendMessage("Cat's game").queue();
            dead = true;
        }
        if(x.length >= 3) {
            for (int i = 0; i < x.length; i++) {
                for (int j = 0; j < x.length; j++) {
                    for (int k = 0; k < x.length; k++) {
                        if (i != j && i != k && j != k) {
                            if(x[i] + x[j] + x[k] == 15 && !dead) {
                                dead = true;
                                channel.sendMessage("X wins").queue();
                            }
                        }
                    }
                }
            }
        }
        if(o.length >= 3) {
            for (int i = 0; i < o.length; i++) {
                for (int j = 0; j < o.length; j++) {
                    for (int k = 0; k < o.length; k++) {
                        if (i != j && i != k && j != k) {
                            if(o[i] + o[j] + o[k] == 15 && !dead) {
                                dead = true;
                                channel.sendMessage("O wins").queue();
                            }
                        }
                    }
                }
            }
        }
    }
    public void debug() {
        for(int i = 0; i < o.length; i++) {
            System.out.println("O" + i + ": " + o[i]);
        }
        for(int i = 0; i < x.length; i++) {
            System.out.println("X" + i + ": " + x[i]);
        }
    }
}
