package com.srgood.reasons.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface Argument {
    public static Argument string(String name) {
        return new Argument() {
            @Override
            public boolean isLegal(String string) {
                return true;
            }

            @Override
            public Argument next(String string) {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public boolean isOptional() {
                return false;
            }
        };
    }

    public static Argument number(String name) {
        return new Argument() {
            @Override
            public boolean isLegal(String string) {
                return string.matches("\\d+");
            }

            @Override
            public Argument next(String string) {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public boolean isOptional() {
                return false;
            }
        };
    }

    boolean isLegal(String string);

    Argument next(String string);

    String name();

    boolean isOptional();

    default boolean isSplit() {return false;}

    default Set<String> splitOptions() {return new HashSet<>();}

    default String format() {
        if (isSplit()) {
            return splitOptions().stream().sorted().collect(Collectors.joining(" | ", "<", ">")) + " <...>";
        } else if (isRepeated()) {
            return (isOptional() ? "{" + name() + "}" : "<" + name() + ">") + "{...}";
        }
        else {
            Argument next = next("");
            if (isRepeated()) {
                return (isOptional() ? "{" + name() + "}" : "<" + name() + ">") + " {...}";
            }
            return (isOptional() ? "{" + name() + "}" : "<" + name() + ">") + (next != null ? " " + next.format() : "");
        }
    }

    default boolean isRepeated() {return false;}
}
