package com.r4men.java_chess;

import com.r4men.java_chess.type.Trio;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void isDiagonalFrom10x12() {
        for (int i = 0; i < 120; i++) {
//            switch (i) {
//                case 0x20, 0x2B, 0x36, 0x41, 0x4C, 0x57, 0x62 -> assertTrue(Util.isDiagonalFrom10x12(0x15, i, Direction.NE_10X12), Integer.toHexString(i));
//                default -> assertFalse(Util.isDiagonalFrom10x12(0x15, i, Direction.NE_10X12), Integer.toHexString(i));
//            }
//
//            switch (i) {
//                case 0x40, 0x35, 0x2A, 0x1F -> assertTrue(Util.isDiagonalFrom10x12(0x4B, i, Direction.SW_10X12), Integer.toHexString(i));
//                default -> assertFalse(Util.isDiagonalFrom10x12(0x4B, i, Direction.SW_10X12), Integer.toHexString(i));
//            }

            switch (i) {
                case 0x62 -> assertTrue(Util.isRayFrom10x12(0x57, i, Direction.D10X12.NE), Integer.toHexString(i));
                default -> assertFalse(Util.isRayFrom10x12(0x57, i, Direction.D10X12.NE), Integer.toHexString(i));
            }
        }
    }

    @Test
    void convertUciTo8x8Move() {
        Set<Character> validFiles = Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        Set<Character> validRanks = Set.of('1', '2', '3', '4', '5', '6', '7', '8');
        Set<Character> validPromo = Set.of('q', 'r', 'b', 'n', ' ');

        for (char f1 : validFiles) {
            for (char r1 : validRanks) {
                for (char f2 : validFiles) {
                    for (char r2 : validRanks) {
                        for (char p : validPromo) {
                            String uciMove = "" + f1 + r1 + f2 + r2 + (p == ' ' ? "" : p);

                            int from = (Character.getNumericValue(r1) - 1) * 8 + (f1 - 'a');
                            int to = (Character.getNumericValue(r2) - 1) * 8 + (f2 - 'a');

                            Trio<Integer, Integer, Piece.@Nullable PieceType> output = Util.convertUciTo8x8Move(uciMove);

                            assertEquals(from, output.first(), "Failed for uciMove: " + uciMove);
                            assertEquals(to, output.second(), "Failed for uciMove: " + uciMove);

                            if (p == ' ') {
                                assertNull(output.third(), "Failed for uciMove: " + uciMove);
                            } else {
                                Piece.PieceType expected = switch (p) {
                                    case 'q' -> Piece.PieceType.QUEEN;
                                    case 'r' -> Piece.PieceType.ROOK;
                                    case 'b' -> Piece.PieceType.BISHOP;
                                    case 'n' -> Piece.PieceType.KNIGHT;
                                    default -> throw new IllegalStateException("Unexpected value: " + p);
                                };

                                assertEquals(expected, output.third(), "Failed for uciMove: " + uciMove);
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    void getD10X12FromSquares() {
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 1));
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 2));
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 3));
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 4));
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 5));
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 6));
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 7));
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 8));
        assertEquals(Direction.D10X12.E, Util.getD10X12FromSquares(0, 9));

        assertEquals(Direction.D10X12.N, Util.getD10X12FromSquares(0, 10));
        assertEquals(Direction.D10X12.NE, Util.getD10X12FromSquares(0, 11));
        assertEquals(Direction.D10X12.NE, Util.getD10X12FromSquares(0, 12));
        assertEquals(Direction.D10X12.NE, Util.getD10X12FromSquares(0, 13));

        assertEquals(Direction.D10X12.SW, Util.getD10X12FromSquares(13, 0));
    }
}
