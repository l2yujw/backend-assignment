package com.example.oms.core.guard;

public final class Guard {

    private Guard() {}

    public static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }

        return value;
    }

    public static String notBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }

        return value;
    }

    public static int minValue(int value, int min, String name) {
        if (value < min) {
            throw new IllegalArgumentException(name + " must be >= " + min);
        }

        return value;
    }
}
