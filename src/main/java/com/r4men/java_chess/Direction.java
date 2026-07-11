package com.r4men.java_chess;

public interface Direction {
    int toInt();

    Direction opposite();

    boolean isDiagonal();
    boolean isOrthogonal();

    enum D10X12 implements Direction {
        N(10), NE(11), E(1), SE(-9),
        S(-10), SW(-11), W(-1), NW(9);

        private final int value;

        D10X12(int value) {
            this.value = value;
        }

        @Override
        public int toInt() {
            return value;
        }

        @Override
        public D10X12 opposite() {
            for (D10X12 direction : D10X12.values()) {
                if (direction.value == -this.value) {
                    return direction;
                }
            }

            throw new RuntimeException("Opposite direction not found for " + this);
        }

        @Override
        public boolean isDiagonal() {
            return this == NE || this == SE || this == SW || this == NW;
        }

        @Override
        public boolean isOrthogonal() {
            return this == N || this == E || this == S || this == W;
        }
    }

    enum D8X8 implements Direction {
        N(8), NE(9), E(1), SE(-7),
        S(-8), SW(-9), W(-1), NW(7);

        private final int value;

        D8X8(int value) {
            this.value = value;
        }

        @Override
        public int toInt() {
            return value;
        }

        @Override
        public D8X8 opposite() {
            for (D8X8 direction : D8X8.values()) {
                if (direction.value == -this.value) {
                    return direction;
                }
            }

            throw new RuntimeException("Opposite direction not found for " + this);
        }

        @Override
        public boolean isDiagonal() {
            return this == NE || this == SE || this == SW || this == NW;
        }

        @Override
        public boolean isOrthogonal() {
            return this == N || this == E || this == S || this == W;
        }
    }
}
