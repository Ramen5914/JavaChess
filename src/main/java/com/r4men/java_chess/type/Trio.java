package com.r4men.java_chess.type;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public record Trio<A, B, C>(A first, B second, C third) implements Serializable {
    @Override
    public @NotNull String toString() {
        return String.format("(%s, %s, %s)", first, second, third);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Trio(Object first1, Object second1, Object third1)) {
            if (!Objects.equals(first, first1)) return false;
            if (!Objects.equals(second, second1)) return false;
            return Objects.equals(third, third1);
        }
        return false;
    }
}
