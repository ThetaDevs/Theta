package com.srgood.reasons.impl.commands.utils;

import java.util.function.Function;

@FunctionalInterface
interface FallibleFunction<T, R> {
    static <T, R> Function<T, R> exceptionProofFallibleFunction(FallibleFunction<T, R> function, R defaultValue) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                return defaultValue;
            }
        };
    }

    R apply(T t) throws Exception;
}
