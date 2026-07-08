package com.r4men.java_chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveTest {
    Move move1;
    Move move2;
    Move move3;
    Move move4;
    Move move5;
    Move move6;
    Move move7;
    Move move8;
    Move move9;
    Move move10;
    Move move11;
    Move move12;
    Move move13;
    Move move14;

    @BeforeEach
    void beforeEach() {
        move1 = new Move(0, 63, Move.Flag.QUIET_MOVE_FLAG, -1, 0b1111, 0, null);
        move2 = new Move(8, 24, Move.Flag.DOUBLE_PAWN_PUSH_FLAG, -1, 0b1111, 0, null);
        move3 = new Move(4, 7, Move.Flag.KING_CASTLE_FLAG, -1, 0b1111, 0, null);
        move4 = new Move(4, 0, Move.Flag.QUEEN_CASTLE_FLAG, -1, 0b1111, 0, null);
        move5 = new Move(28, 35, Move.Flag.CAPTURES_FLAG, -1, 0b1111, 0, null);
        move6 = new Move(29, 20, Move.Flag.EN_PASSANT_CAPTURE_FLAG, -1, 0b1111, 0, null);
        move7 = new Move(48, 56, Move.Flag.KNIGHT_PROMOTION_FLAG, -1, 0b1111, 0, null);
        move8 = new Move(49, 57, Move.Flag.BISHOP_PROMOTION_FLAG, -1, 0b1111, 0, null);
        move9 = new Move(50, 58, Move.Flag.ROOK_PROMOTION_FLAG, -1, 0b1111, 0, null);
        move10 = new Move(51, 59, Move.Flag.QUEEN_PROMOTION_FLAG, -1, 0b1111, 0, null);
        move11 = new Move(51, 60, Move.Flag.KNIGHT_PROMOTION_CAPTURE_FLAG, -1, 0b1111, 0, null);
        move12 = new Move(52, 61, Move.Flag.BISHOP_PROMOTION_CAPTURE_FLAG, -1, 0b1111, 0, null);
        move13 = new Move(53, 62, Move.Flag.ROOK_PROMOTION_CAPTURE_FLAG, -1, 0b1111, 0, null);
        move14 = new Move(54, 63, Move.Flag.QUEEN_PROMOTION_CAPTURE_FLAG, -1, 0b1111, 0, null);
    }

    @Test
    void getFrom8x8() {
        assertEquals(0, move1.getFrom8x8());
        assertEquals(8, move2.getFrom8x8());
        assertEquals(4, move3.getFrom8x8());
        assertEquals(4, move4.getFrom8x8());
        assertEquals(28, move5.getFrom8x8());
        assertEquals(29, move6.getFrom8x8());
        assertEquals(48, move7.getFrom8x8());
        assertEquals(49, move8.getFrom8x8());
        assertEquals(50, move9.getFrom8x8());
        assertEquals(51, move10.getFrom8x8());
        assertEquals(51, move11.getFrom8x8());
        assertEquals(52, move12.getFrom8x8());
        assertEquals(53, move13.getFrom8x8());
        assertEquals(54, move14.getFrom8x8());
    }

    @Test
    void getTo8x8() {
        assertEquals(63, move1.getTo8x8());
        assertEquals(24, move2.getTo8x8());
        assertEquals(7, move3.getTo8x8());
        assertEquals(0, move4.getTo8x8());
        assertEquals(35, move5.getTo8x8());
        assertEquals(20, move6.getTo8x8());
        assertEquals(56, move7.getTo8x8());
        assertEquals(57, move8.getTo8x8());
        assertEquals(58, move9.getTo8x8());
        assertEquals(59, move10.getTo8x8());
        assertEquals(60, move11.getTo8x8());
        assertEquals(61, move12.getTo8x8());
        assertEquals(62, move13.getTo8x8());
        assertEquals(63, move14.getTo8x8());
    }

    @Test
    void getFlags() {
        assertEquals(Move.Flag.QUIET_MOVE_FLAG.toInt(), move1.getFlags());
        assertEquals(Move.Flag.DOUBLE_PAWN_PUSH_FLAG.toInt(), move2.getFlags());
        assertEquals(Move.Flag.KING_CASTLE_FLAG.toInt(), move3.getFlags());
        assertEquals(Move.Flag.QUEEN_CASTLE_FLAG.toInt(), move4.getFlags());
        assertEquals(Move.Flag.CAPTURES_FLAG.toInt(), move5.getFlags());
        assertEquals(Move.Flag.EN_PASSANT_CAPTURE_FLAG.toInt(), move6.getFlags());
        assertEquals(Move.Flag.KNIGHT_PROMOTION_FLAG.toInt(), move7.getFlags());
        assertEquals(Move.Flag.BISHOP_PROMOTION_FLAG.toInt(), move8.getFlags());
        assertEquals(Move.Flag.ROOK_PROMOTION_FLAG.toInt(), move9.getFlags());
        assertEquals(Move.Flag.QUEEN_PROMOTION_FLAG.toInt(), move10.getFlags());
        assertEquals(Move.Flag.KNIGHT_PROMOTION_CAPTURE_FLAG.toInt(), move11.getFlags());
        assertEquals(Move.Flag.BISHOP_PROMOTION_CAPTURE_FLAG.toInt(), move12.getFlags());
        assertEquals(Move.Flag.ROOK_PROMOTION_CAPTURE_FLAG.toInt(), move13.getFlags());
        assertEquals(Move.Flag.QUEEN_PROMOTION_CAPTURE_FLAG.toInt(), move14.getFlags());
    }

    @Test
    void isQuiet() {
        assertTrue(move1.isQuiet());
        assertFalse(move2.isQuiet());
        assertFalse(move3.isQuiet());
        assertFalse(move4.isQuiet());
        assertFalse(move5.isQuiet());
        assertFalse(move6.isQuiet());
        assertFalse(move7.isQuiet());
        assertFalse(move8.isQuiet());
        assertFalse(move9.isQuiet());
        assertFalse(move10.isQuiet());
        assertFalse(move11.isQuiet());
        assertFalse(move12.isQuiet());
        assertFalse(move13.isQuiet());
        assertFalse(move14.isQuiet());
    }

    @Test
    void isDoublePawnPush() {
        assertFalse(move1.isDoublePawnPush());
        assertTrue(move2.isDoublePawnPush());
        assertFalse(move3.isDoublePawnPush());
        assertFalse(move4.isDoublePawnPush());
        assertFalse(move5.isDoublePawnPush());
        assertFalse(move6.isDoublePawnPush());
        assertFalse(move7.isDoublePawnPush());
        assertFalse(move8.isDoublePawnPush());
        assertFalse(move9.isDoublePawnPush());
        assertFalse(move10.isDoublePawnPush());
        assertFalse(move11.isDoublePawnPush());
        assertFalse(move12.isDoublePawnPush());
        assertFalse(move13.isDoublePawnPush());
        assertFalse(move14.isDoublePawnPush());
    }

    @Test
    void isKingCastle() {
        assertFalse(move1.isKingCastle());
        assertFalse(move2.isKingCastle());
        assertTrue(move3.isKingCastle());
        assertFalse(move4.isKingCastle());
        assertFalse(move5.isKingCastle());
        assertFalse(move6.isKingCastle());
        assertFalse(move7.isKingCastle());
        assertFalse(move8.isKingCastle());
        assertFalse(move9.isKingCastle());
        assertFalse(move10.isKingCastle());
        assertFalse(move11.isKingCastle());
        assertFalse(move12.isKingCastle());
        assertFalse(move13.isKingCastle());
        assertFalse(move14.isKingCastle());
    }

    @Test
    void isQueenCastle() {
        assertFalse(move1.isQueenCastle());
        assertFalse(move2.isQueenCastle());
        assertFalse(move3.isQueenCastle());
        assertTrue(move4.isQueenCastle());
        assertFalse(move5.isQueenCastle());
        assertFalse(move6.isQueenCastle());
        assertFalse(move7.isQueenCastle());
        assertFalse(move8.isQueenCastle());
        assertFalse(move9.isQueenCastle());
        assertFalse(move10.isQueenCastle());
        assertFalse(move11.isQueenCastle());
        assertFalse(move12.isQueenCastle());
        assertFalse(move13.isQueenCastle());
        assertFalse(move14.isQueenCastle());
    }

    @Test
    void isCapture() {
        assertFalse(move1.isCapture());
        assertFalse(move2.isCapture());
        assertFalse(move3.isCapture());
        assertFalse(move4.isCapture());
        assertTrue(move5.isCapture());
        assertTrue(move6.isCapture());
        assertFalse(move7.isCapture());
        assertFalse(move8.isCapture());
        assertFalse(move9.isCapture());
        assertFalse(move10.isCapture());
        assertTrue(move11.isCapture());
        assertTrue(move12.isCapture());
        assertTrue(move13.isCapture());
        assertTrue(move14.isCapture());
    }

    @Test
    void isEnPassantCapture() {
        assertFalse(move1.isEnPassantCapture());
        assertFalse(move2.isEnPassantCapture());
        assertFalse(move3.isEnPassantCapture());
        assertFalse(move4.isEnPassantCapture());
        assertFalse(move5.isEnPassantCapture());
        assertTrue(move6.isEnPassantCapture());
        assertFalse(move7.isEnPassantCapture());
        assertFalse(move8.isEnPassantCapture());
        assertFalse(move9.isEnPassantCapture());
        assertFalse(move10.isEnPassantCapture());
        assertFalse(move11.isEnPassantCapture());
        assertFalse(move12.isEnPassantCapture());
        assertFalse(move13.isEnPassantCapture());
        assertFalse(move14.isEnPassantCapture());
    }

    @Test
    void isPromotion() {
        assertFalse(move1.isPromotion());
        assertFalse(move2.isPromotion());
        assertFalse(move3.isPromotion());
        assertFalse(move4.isPromotion());
        assertFalse(move5.isPromotion());
        assertFalse(move6.isPromotion());
        assertTrue(move7.isPromotion());
        assertTrue(move8.isPromotion());
        assertTrue(move9.isPromotion());
        assertTrue(move10.isPromotion());
        assertTrue(move11.isPromotion());
        assertTrue(move12.isPromotion());
        assertTrue(move13.isPromotion());
        assertTrue(move14.isPromotion());
    }

    @Test
    void isKnightPromotion() {
        assertFalse(move1.isKnightPromotion());
        assertFalse(move2.isKnightPromotion());
        assertFalse(move3.isKnightPromotion());
        assertFalse(move4.isKnightPromotion());
        assertFalse(move5.isKnightPromotion());
        assertFalse(move6.isKnightPromotion());
        assertTrue(move7.isKnightPromotion());
        assertFalse(move8.isKnightPromotion());
        assertFalse(move9.isKnightPromotion());
        assertFalse(move10.isKnightPromotion());
        assertTrue(move11.isKnightPromotion());
        assertFalse(move12.isKnightPromotion());
        assertFalse(move13.isKnightPromotion());
        assertFalse(move14.isKnightPromotion());
    }

    @Test
    void isBishopPromotion() {
        assertFalse(move1.isBishopPromotion());
        assertFalse(move2.isBishopPromotion());
        assertFalse(move3.isBishopPromotion());
        assertFalse(move4.isBishopPromotion());
        assertFalse(move5.isBishopPromotion());
        assertFalse(move6.isBishopPromotion());
        assertFalse(move7.isBishopPromotion());
        assertTrue(move8.isBishopPromotion());
        assertFalse(move9.isBishopPromotion());
        assertFalse(move10.isBishopPromotion());
        assertFalse(move11.isBishopPromotion());
        assertTrue(move12.isBishopPromotion());
        assertFalse(move13.isBishopPromotion());
        assertFalse(move14.isBishopPromotion());
    }

    @Test
    void isRookPromotion() {
        assertFalse(move1.isRookPromotion());
        assertFalse(move2.isRookPromotion());
        assertFalse(move3.isRookPromotion());
        assertFalse(move4.isRookPromotion());
        assertFalse(move5.isRookPromotion());
        assertFalse(move6.isRookPromotion());
        assertFalse(move7.isRookPromotion());
        assertFalse(move8.isRookPromotion());
        assertTrue(move9.isRookPromotion());
        assertFalse(move10.isRookPromotion());
        assertFalse(move11.isRookPromotion());
        assertFalse(move12.isRookPromotion());
        assertTrue(move13.isRookPromotion());
        assertFalse(move14.isRookPromotion());
    }

    @Test
    void isQueenPromotion() {
        assertFalse(move1.isQueenPromotion());
        assertFalse(move2.isQueenPromotion());
        assertFalse(move3.isQueenPromotion());
        assertFalse(move4.isQueenPromotion());
        assertFalse(move5.isQueenPromotion());
        assertFalse(move6.isQueenPromotion());
        assertFalse(move7.isQueenPromotion());
        assertFalse(move8.isQueenPromotion());
        assertFalse(move9.isQueenPromotion());
        assertTrue(move10.isQueenPromotion());
        assertFalse(move11.isQueenPromotion());
        assertFalse(move12.isQueenPromotion());
        assertFalse(move13.isQueenPromotion());
        assertTrue(move14.isQueenPromotion());
    }

    @Test
    void isKnightPromotionCapture() {
        assertFalse(move1.isKnightPromotionCapture());
        assertFalse(move2.isKnightPromotionCapture());
        assertFalse(move3.isKnightPromotionCapture());
        assertFalse(move4.isKnightPromotionCapture());
        assertFalse(move5.isKnightPromotionCapture());
        assertFalse(move6.isKnightPromotionCapture());
        assertFalse(move7.isKnightPromotionCapture());
        assertFalse(move8.isKnightPromotionCapture());
        assertFalse(move9.isKnightPromotionCapture());
        assertFalse(move10.isKnightPromotionCapture());
        assertTrue(move11.isKnightPromotionCapture());
        assertFalse(move12.isKnightPromotionCapture());
        assertFalse(move13.isKnightPromotionCapture());
        assertFalse(move14.isKnightPromotionCapture());
    }

    @Test
    void isBishopPromotionCapture() {
        assertFalse(move1.isBishopPromotionCapture());
        assertFalse(move2.isBishopPromotionCapture());
        assertFalse(move3.isBishopPromotionCapture());
        assertFalse(move4.isBishopPromotionCapture());
        assertFalse(move5.isBishopPromotionCapture());
        assertFalse(move6.isBishopPromotionCapture());
        assertFalse(move7.isBishopPromotionCapture());
        assertFalse(move8.isBishopPromotionCapture());
        assertFalse(move9.isBishopPromotionCapture());
        assertFalse(move10.isBishopPromotionCapture());
        assertFalse(move11.isBishopPromotionCapture());
        assertTrue(move12.isBishopPromotionCapture());
        assertFalse(move13.isBishopPromotionCapture());
        assertFalse(move14.isBishopPromotionCapture());
    }

    @Test
    void isRookPromotionCapture() {
        assertFalse(move1.isRookPromotionCapture());
        assertFalse(move2.isRookPromotionCapture());
        assertFalse(move3.isRookPromotionCapture());
        assertFalse(move4.isRookPromotionCapture());
        assertFalse(move5.isRookPromotionCapture());
        assertFalse(move6.isRookPromotionCapture());
        assertFalse(move7.isRookPromotionCapture());
        assertFalse(move8.isRookPromotionCapture());
        assertFalse(move9.isRookPromotionCapture());
        assertFalse(move10.isRookPromotionCapture());
        assertFalse(move11.isRookPromotionCapture());
        assertFalse(move12.isRookPromotionCapture());
        assertTrue(move13.isRookPromotionCapture());
        assertFalse(move14.isRookPromotionCapture());
    }

    @Test
    void isQueenPromotionCapture() {
        assertFalse(move1.isQueenPromotionCapture());
        assertFalse(move2.isQueenPromotionCapture());
        assertFalse(move3.isQueenPromotionCapture());
        assertFalse(move4.isQueenPromotionCapture());
        assertFalse(move5.isQueenPromotionCapture());
        assertFalse(move6.isQueenPromotionCapture());
        assertFalse(move7.isQueenPromotionCapture());
        assertFalse(move8.isQueenPromotionCapture());
        assertFalse(move9.isQueenPromotionCapture());
        assertFalse(move10.isQueenPromotionCapture());
        assertFalse(move11.isQueenPromotionCapture());
        assertFalse(move12.isQueenPromotionCapture());
        assertFalse(move13.isQueenPromotionCapture());
        assertTrue(move14.isQueenPromotionCapture());
    }
}