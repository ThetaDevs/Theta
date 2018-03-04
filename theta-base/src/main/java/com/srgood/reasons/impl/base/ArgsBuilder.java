package com.srgood.reasons.impl.base;

import com.srgood.reasons.commands.Argument;

import java.util.*;

public class ArgsBuilder {
    private boolean inSubCommands = false;
    List<PreArgument> argumentChain = new ArrayList<>();
    Map<String, Argument> subCommands = new HashMap<>();

    boolean isOptional = false;

    public static ArgsBuilder create() {
        return new ArgsBuilder();
    }

    public ArgsBuilder addString(String name) {
        if (inSubCommands) {
            throw new IllegalStateException("Cannot add arguments after subcommands");
        }
        argumentChain.add(new StringPreArgument(name, isOptional));
        return this;
    }

    public ArgsBuilder addNumber(String name) {
        if (inSubCommands) {
            throw new IllegalStateException("Cannot add arguments after subcommands");
        }
        argumentChain.add(new NumberPreArgument(name, isOptional));
        return this;
    }

    public ArgsBuilder addSubCommand(String name, Argument arguments) {
        inSubCommands = true;
        subCommands.put(name, arguments);
        return this;
    }

    public Argument build() {
        Argument argument = null;
        if (inSubCommands) {
            argument = new SplitPreArgument(subCommands, isOptional).withNext(null);
        }

        for (int i = argumentChain.size() - 1; i >= 0; i--) {
            argument = argumentChain.get(i).withNext(argument);
        }
        return argument;
    }

    public ArgsBuilder beginOptional() {
        isOptional = true;
        return this;
    }

    public Argument repeatLast() {
        if (inSubCommands) {
            throw new IllegalStateException("Cannot add arguments after subcommands");
        }
        Argument last = argumentChain.get(argumentChain.size() - 1).withNext(null);
        Argument argument = new RepeatArgument(last);
        for (int i = argumentChain.size() -1 - 1; i >= 0; i--) {
            argument = argumentChain.get(i).withNext(argument);
        }
        return argument;
    }

    private interface PreArgument {
        public Argument withNext(Argument argument);
    }

    private class NumberPreArgument implements PreArgument {
        private final String name;
        private final boolean optional;

        public NumberPreArgument(String name, boolean optional) {
            this.name = name;
            this.optional = optional;
        }

        public Argument withNext(Argument argument) {
            return new Argument() {
                @Override
                public boolean isLegal(String string) {
                    return string.matches("\\d+");
                }

                @Override
                public Argument next(String string) {
                    return argument;
                }

                @Override
                public String name() {
                    return name;
                }

                @Override
                public boolean isOptional() {
                    return optional;
                }
            };
        }
    }

    private class StringPreArgument implements PreArgument {
        private final String name;
        private final boolean optional;

        public StringPreArgument(String name, boolean optional) {
            this.name = name;
            this.optional = optional;
        }

        public Argument withNext(Argument argument) {
            return new Argument() {
                @Override
                public boolean isLegal(String string) {
                    return string.matches(".+");
                }

                @Override
                public Argument next(String string) {
                    return argument;
                }

                @Override
                public String name() {
                    return name;
                }

                @Override
                public boolean isOptional() {
                    return optional;
                }
            };
        }
    }

    private class SplitPreArgument implements PreArgument {
        private final Map<String, Argument> argumentMap;
        private final boolean optional;

        public SplitPreArgument(Map<String, Argument> argumentMap, boolean optional) {
            this.argumentMap = Collections.unmodifiableMap(new HashMap<>(argumentMap));
            this.optional = optional;
        }

        public Argument withNext(Argument argument) {
            return new Argument() {
                @Override
                public boolean isLegal(String string) {
                    return argumentMap.keySet().contains(string);
                }

                @Override
                public Argument next(String string) {
                    return argumentMap.get(string);
                }

                @Override
                public String name() {
                    return null;
                }

                @Override
                public boolean isOptional() {
                    return optional;
                }

                @Override
                public boolean isSplit() {
                    return true;
                }

                @Override
                public Set<String> splitOptions() {
                    return argumentMap.keySet();
                }
            };
        }
    }

    private class RepeatArgument implements Argument {
        private final Argument repeated;

        public RepeatArgument(Argument repeated) {
            this.repeated = repeated;
        }

        @Override
        public boolean isLegal(String string) {
            return repeated.isLegal(string);
        }

        @Override
        public Argument next(String string) {
            return this;
        }

        @Override
        public String name() {
            return repeated.name();
        }

        @Override
        public boolean isOptional() {
            return repeated.isOptional();
        }

        @Override
        public boolean isRepeated() {
            return true;
        }
    }
}
