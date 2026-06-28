package com.r4men.java_chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UtilTest {
    @Test
    void convert10x12to8x8() {
        assertThrows(IllegalArgumentException.class, () -> Util.convert10x12to8x8(0));
        assertEquals(0, Util.convert10x12to8x8(0x15));
        assertEquals(8, Util.convert10x12to8x8(0x1F));
        assertEquals(16, Util.convert10x12to8x8(0x29));
        assertEquals(63, Util.convert10x12to8x8(0x62));
    }

    @Test
    void convert8x8to10x12() {
        assertEquals(0x15, Util.convert8x8to10x12(0));
        assertEquals(0x16, Util.convert8x8to10x12(1));
        assertEquals(0x17, Util.convert8x8to10x12(2));
        assertEquals(0x18, Util.convert8x8to10x12(3));
        assertEquals(0x19, Util.convert8x8to10x12(4));
        assertEquals(0x1A, Util.convert8x8to10x12(5));
        assertEquals(0x1B, Util.convert8x8to10x12(6));
        assertEquals(0x1C, Util.convert8x8to10x12(7));
        assertEquals(0x1F, Util.convert8x8to10x12(8));
        assertEquals(0x62, Util.convert8x8to10x12(63));
    }
}
