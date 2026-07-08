package com.r4men.java_chess.type;

import java.io.Serializable;
import java.util.Objects;

public class Triple<A, B, C> implements Serializable {
    private A first;
    private B second;
    private C third;

    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    public C getThird() {
        return third;
    }

    public void setThird(C third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", first, second, third);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (first != null ? first.hashCode() : 0);
        hash = 31 * hash + (second != null ? second.hashCode() : 0);
        hash = 31 * hash + (third != null ? third.hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Triple triple) {
            if (!Objects.equals(first, triple.first)) return false;
            if (!Objects.equals(second, triple.second)) return false;
            return Objects.equals(third, triple.third);
        }
        return false;
    }
}
