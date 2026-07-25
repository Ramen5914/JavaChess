package com.r4men.java_chess.type;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public record Pair<A, B>(A first, B second) implements Serializable {
    @Override
    public @NotNull String toString() {
        return String.format("(%s, %s)", first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair(Object first1, Object second1)) {
            if (!Objects.equals(first, first1)) return false;
            return Objects.equals(second, second1);
        }
        return false;
    }
}
