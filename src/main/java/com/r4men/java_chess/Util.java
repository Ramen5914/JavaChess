package com.r4men.java_chess;

import com.r4men.java_chess.type.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    //                 HEX:
    //         A  B  C  D  E  F  G  H
    //   | 6E 6F 70 71 72 73 74 75 76 77 |
    //   | 64 65 66 67 68 69 6A 6B 6C 6D |
    // 8 | 5A 5B 5C 5D 5E 5F 60 61 62 63 | 8
    // 7 | 50 51 52 53 54 55 56 57 58 59 | 7
    // 6 | 46 47 48 49 4A 4B 4C 4D 4E 4F | 6
    // 5 | 3C 3D 3E 3F 40 41 42 43 44 45 | 5
    // 4 | 32 33 34 35 36 37 38 39 3A 3B | 4
    // 3 | 28 29 2A 2B 2C 2D 2E 2F 30 31 | 3
    // 2 | 1E 1F 20 21 22 23 24 25 26 27 | 2
    // 1 | 14 15 16 17 18 19 1A 1B 1C 1D | 1
    //   | 0A 0B 0C 0D 0E 0F 10 11 12 13 |
    //   | 00 01 02 03 04 05 06 07 08 09 |
    //         A  B  C  D  E  F  G  H
    private static final boolean[] board10x12 = new boolean[120];

    static {
        for (int i = 0; i < 120; i++) {
            if (i % 10 != 0 && i % 10 != 9 && i > 0x14 && i < 0x63) {
                board10x12[i] = true;
            }
        }
    }

    public static int convert10x12to8x8(int s10x12) {
        if (s10x12 % 10 == 0 || s10x12 % 10 == 9 || s10x12 <= 0x14 || s10x12 >= 0x63) {
            throw new  IllegalArgumentException("Invalid 10x12 square (Off-Board on 8x8): " + String.format("0x%2s", Integer.toHexString(s10x12).toUpperCase()).replace(' ', '0'));
        }

        int rank = (s10x12 - 0x15) / 10;
        int file = (s10x12 - 0x15) % 10;

        return (rank * 8) + file;
    }

    /**
     * Determines whether a target square lies on a straight ray from a starting square
     * in a 10x12 mailbox board representation.
     *
     * <p>The ray may be orthogonal or diagonal, depending on the supplied direction.
     * The method steps from {@code from} toward {@code to} one square at a time until
     * it either reaches {@code to} or leaves the board.</p>
     *
     * @param from the starting square index in 10x12 representation
     * @param to the target square index in 10x12 representation
     * @param direction the ray direction to check
     *
     * @return {@code true} if {@code to} lies on the specified ray from {@code from}; {@code false} otherwise
     *
     * @throws IllegalArgumentException if either {@code from} or {@code to} are not in the range [0, 120)
     */
    public static boolean isRayFrom10x12(int from, int to, Direction.D10X12 direction) {
        if (from < 0 || from >= 120 || to < 0 || to >= 120) {
            throw new IllegalArgumentException("From or To must be between 0 and 120");
        }

        int step = direction.toInt();
        int square = from + step;

        while (square >= 0 && square < 120 && board10x12[square]) {
            if (square == to) {
                return true;
            }
            square += step;
        }

        return false;
    }

    //              INT:
    //      A  B  C  D  E  F  G  H
    // 8 | 56 57 58 59 60 61 62 63 | 8
    // 7 | 48 49 50 51 52 53 54 55 | 7
    // 6 | 40 41 42 43 44 45 46 47 | 6
    // 5 | 32 33 34 35 36 37 38 39 | 5
    // 4 | 24 25 26 27 28 29 30 31 | 4
    // 3 | 16 17 18 19 20 21 22 23 | 3
    // 2 | 08 09 10 11 12 13 14 15 | 2
    // 1 | 00 01 02 03 04 05 06 07 | 1
    //      A  B  C  D  E  F  G  H

    public static int convert8x8to10x12(int s8x8) {
        if (s8x8 < 0 || s8x8 >= 64) {
            throw new  IllegalArgumentException("Invalid 8x8 square (Off-Board on 8x8): " + s8x8);
        }

        int file = (s8x8 % 8) + 1;
        int rank = (s8x8 / 8) + 2;

        return (rank * 10) + file;
    }

    public static int convertSquareTo8x8(String square) {
        if (square.matches("^[a-hA-H][1-8]$")) {
            char[] parts = square.toLowerCase().toCharArray();

            return (Character.getNumericValue(parts[1]) - 1) * 8 + (parts[0] - 'a');
        } else {
            throw new IllegalArgumentException("Invalid square string: " + square);
        }
    }

    /**
     * Converts a UCI move string into usable integers and PieceTypes for the Move constructor
     * @param uciMove a string that matches ^([a-hA-H][1-8])([a-hA-H][1-8])([qrbn])?$ regex
     * @return a {@link Triple} containing two Integers (from8x8, to8x8) and a {@link Piece.PieceType PieceType} for promotion (null if no promotion)
     * @throws IllegalArgumentException if the uciMove string does not match the regex
     */
    public static Triple<Integer, Integer, Piece.@Nullable PieceType> convertUciTo8x8Move(String uciMove) {
        String pattern = "^([a-h][1-8])([a-h][1-8])([qrbn])?$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(uciMove.toLowerCase());

        if (m.matches()) {
            char[] fromSquare = m.group(1).toCharArray();
            char[] toSquare = m.group(2).toCharArray();
            char promotion = m.group(3) == null ? ' ' : m.group(3).charAt(0);

            return new Triple<>(
                    (Character.getNumericValue(fromSquare[1]) - 1) * 8 + (fromSquare[0] - 'a'),
                    (Character.getNumericValue(toSquare[1]) - 1) * 8 + (toSquare[0] - 'a'),
                    switch (promotion) {
                        case 'q' -> Piece.PieceType.QUEEN;
                        case 'r' -> Piece.PieceType.ROOK;
                        case 'b' -> Piece.PieceType.BISHOP;
                        case 'n' -> Piece.PieceType.KNIGHT;
                        default -> null;
                    }
            );
        } else {
            throw new IllegalArgumentException("Invalid uciMove string: " + uciMove);
        }
    }

    public static int convertSquareTo10x12(String square) {
        return convert8x8to10x12(convertSquareTo8x8(square));
    }

    /**
     * Makes sure a bitboard has exactly 1 piece on it
     * @param bb 64 bit long bitboard
     * @return true if the long has only 1 bit set, false otherwise
     */
    public static boolean hasOnlyOnePiece(long bb) {
        return Long.bitCount(bb) == 1;
    }
}
