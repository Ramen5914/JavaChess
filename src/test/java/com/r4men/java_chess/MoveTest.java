package com.r4men.java_chess;

import org.junit.jupiter.api.Assertions;
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
        move1 = new Move(0, 63, Move.Flags.QUIET_MOVE_FLAG);
        move2 = new Move(8, 24, Move.Flags.DOUBLE_PAWN_PUSH_FLAG);
        move3 = new Move(4, 7, Move.Flags.KING_CASTLE_FLAG);
        move4 = new Move(4, 0, Move.Flags.QUEEN_CASTLE_FLAG);
        move5 = new Move(28, 35, Move.Flags.CAPTURES_FLAG);
        move6 = new Move(29, 20, Move.Flags.EN_PASSANT_CAPTURE_FLAG);
        move7 = new Move(48, 56, Move.Flags.KNIGHT_PROMOTION_FLAG);
        move8 = new Move(49, 57, Move.Flags.BISHOP_PROMOTION_FLAG);
        move9 = new Move(50, 58, Move.Flags.ROOK_PROMOTION_FLAG);
        move10 = new Move(51, 59, Move.Flags.QUEEN_PROMOTION_FLAG);
        move11 = new Move(51, 60, Move.Flags.KNIGHT_PROMOTION_CAPTURE_FLAG);
        move12 = new Move(52, 61, Move.Flags.BISHOP_PROMOTION_CAPTURE_FLAG);
        move13 = new Move(53, 62, Move.Flags.ROOK_PROMOTION_CAPTURE_FLAG);
        move14 = new Move(54, 63, Move.Flags.QUEEN_PROMOTION_CAPTURE_FLAG);
    }

    @Test
    void getFrom() {
        Assertions.assertEquals(0, move1.getFrom());
        Assertions.assertEquals(8, move2.getFrom());
        Assertions.assertEquals(4, move3.getFrom());
        Assertions.assertEquals(4, move4.getFrom());
        Assertions.assertEquals(28, move5.getFrom());
        Assertions.assertEquals(29, move6.getFrom());
        Assertions.assertEquals(48, move7.getFrom());
        Assertions.assertEquals(49, move8.getFrom());
        Assertions.assertEquals(50, move9.getFrom());
        Assertions.assertEquals(51, move10.getFrom());
        Assertions.assertEquals(51, move11.getFrom());
        Assertions.assertEquals(52, move12.getFrom());
        Assertions.assertEquals(53, move13.getFrom());
        Assertions.assertEquals(54, move14.getFrom());
    }

    @Test
    void getTo() {
        Assertions.assertEquals(63, move1.getTo());
        Assertions.assertEquals(24, move2.getTo());
        Assertions.assertEquals(7, move3.getTo());
        Assertions.assertEquals(0, move4.getTo());
        Assertions.assertEquals(35, move5.getTo());
        Assertions.assertEquals(20, move6.getTo());
        Assertions.assertEquals(56, move7.getTo());
        Assertions.assertEquals(57, move8.getTo());
        Assertions.assertEquals(58, move9.getTo());
        Assertions.assertEquals(59, move10.getTo());
        Assertions.assertEquals(60, move11.getTo());
        Assertions.assertEquals(61, move12.getTo());
        Assertions.assertEquals(62, move13.getTo());
        Assertions.assertEquals(63, move14.getTo());
    }

    @Test
    void getFlags() {
        Assertions.assertEquals(Move.Flags.QUIET_MOVE_FLAG, move1.getFlags());
        Assertions.assertEquals(Move.Flags.DOUBLE_PAWN_PUSH_FLAG, move2.getFlags());
        Assertions.assertEquals(Move.Flags.KING_CASTLE_FLAG, move3.getFlags());
        Assertions.assertEquals(Move.Flags.QUEEN_CASTLE_FLAG, move4.getFlags());
        Assertions.assertEquals(Move.Flags.CAPTURES_FLAG, move5.getFlags());
        Assertions.assertEquals(Move.Flags.EN_PASSANT_CAPTURE_FLAG, move6.getFlags());
        Assertions.assertEquals(Move.Flags.KNIGHT_PROMOTION_FLAG, move7.getFlags());
        Assertions.assertEquals(Move.Flags.BISHOP_PROMOTION_FLAG, move8.getFlags());
        Assertions.assertEquals(Move.Flags.ROOK_PROMOTION_FLAG, move9.getFlags());
        Assertions.assertEquals(Move.Flags.QUEEN_PROMOTION_FLAG, move10.getFlags());
        Assertions.assertEquals(Move.Flags.KNIGHT_PROMOTION_CAPTURE_FLAG, move11.getFlags());
        Assertions.assertEquals(Move.Flags.BISHOP_PROMOTION_CAPTURE_FLAG, move12.getFlags());
        Assertions.assertEquals(Move.Flags.ROOK_PROMOTION_CAPTURE_FLAG, move13.getFlags());
        Assertions.assertEquals(Move.Flags.QUEEN_PROMOTION_CAPTURE_FLAG, move14.getFlags());
    }

    @Test
    void setTo() {
        for (int i = 0; i < 63; i++) {
            move1.setTo(i);
            Assertions.assertEquals(i, move1.getTo());
            move2.setTo(i);
            Assertions.assertEquals(i, move2.getTo());
            move3.setTo(i);
            Assertions.assertEquals(i, move3.getTo());
            move4.setTo(i);
            Assertions.assertEquals(i, move4.getTo());
            move5.setTo(i);
            Assertions.assertEquals(i, move5.getTo());
            move6.setTo(i);
            Assertions.assertEquals(i, move6.getTo());
            move7.setTo(i);
            Assertions.assertEquals(i, move7.getTo());
            move8.setTo(i);
            Assertions.assertEquals(i, move8.getTo());
            move9.setTo(i);
            Assertions.assertEquals(i, move9.getTo());
            move10.setTo(i);
            Assertions.assertEquals(i, move10.getTo());
            move11.setTo(i);
            Assertions.assertEquals(i, move11.getTo());
            move12.setTo(i);
            Assertions.assertEquals(i, move12.getTo());
            move13.setTo(i);
            Assertions.assertEquals(i, move13.getTo());
            move14.setTo(i);
            Assertions.assertEquals(i, move14.getTo());

            beforeEach();
        }
    }

    @Test
    void setFrom() {
        for (int i = 0; i < 63; i++) {
            move1.setFrom(i);
            Assertions.assertEquals(i, move1.getFrom());
            move2.setFrom(i);
            Assertions.assertEquals(i, move2.getFrom());
            move3.setFrom(i);
            Assertions.assertEquals(i, move3.getFrom());
            move4.setFrom(i);
            Assertions.assertEquals(i, move4.getFrom());
            move5.setFrom(i);
            Assertions.assertEquals(i, move5.getFrom());
            move6.setFrom(i);
            Assertions.assertEquals(i, move6.getFrom());
            move7.setFrom(i);
            Assertions.assertEquals(i, move7.getFrom());
            move8.setFrom(i);
            Assertions.assertEquals(i, move8.getFrom());
            move9.setFrom(i);
            Assertions.assertEquals(i, move9.getFrom());
            move10.setFrom(i);
            Assertions.assertEquals(i, move10.getFrom());
            move11.setFrom(i);
            Assertions.assertEquals(i, move11.getFrom());
            move12.setFrom(i);
            Assertions.assertEquals(i, move12.getFrom());
            move13.setFrom(i);
            Assertions.assertEquals(i, move13.getFrom());
            move14.setFrom(i);
            Assertions.assertEquals(i, move14.getFrom());

            beforeEach();
        }
    }

    @Test
    void setFlags() {
        for (int i = 0b0000; i < 0b1111; i++) {
            move1.setFlags(i);
            Assertions.assertEquals(i, move1.getFlags());
            move2.setFlags(i);
            Assertions.assertEquals(i, move2.getFlags());
            move3.setFlags(i);
            Assertions.assertEquals(i, move3.getFlags());
            move4.setFlags(i);
            Assertions.assertEquals(i, move4.getFlags());
            move5.setFlags(i);
            Assertions.assertEquals(i, move5.getFlags());
            move6.setFlags(i);
            Assertions.assertEquals(i, move6.getFlags());
            move7.setFlags(i);
            Assertions.assertEquals(i, move7.getFlags());
            move8.setFlags(i);
            Assertions.assertEquals(i, move8.getFlags());
            move9.setFlags(i);
            Assertions.assertEquals(i, move9.getFlags());
            move10.setFlags(i);
            Assertions.assertEquals(i, move10.getFlags());
            move11.setFlags(i);
            Assertions.assertEquals(i, move11.getFlags());
            move12.setFlags(i);
            Assertions.assertEquals(i, move12.getFlags());
            move13.setFlags(i);
            Assertions.assertEquals(i, move13.getFlags());
            move14.setFlags(i);
            Assertions.assertEquals(i, move14.getFlags());

            beforeEach();
        }
    }

    @Test
    void isQuiet() {
        Assertions.assertTrue(move1.isQuiet());
        Assertions.assertFalse(move2.isQuiet());
        Assertions.assertFalse(move3.isQuiet());
        Assertions.assertFalse(move4.isQuiet());
        Assertions.assertFalse(move5.isQuiet());
        Assertions.assertFalse(move6.isQuiet());
        Assertions.assertFalse(move7.isQuiet());
        Assertions.assertFalse(move8.isQuiet());
        Assertions.assertFalse(move9.isQuiet());
        Assertions.assertFalse(move10.isQuiet());
        Assertions.assertFalse(move11.isQuiet());
        Assertions.assertFalse(move12.isQuiet());
        Assertions.assertFalse(move13.isQuiet());
        Assertions.assertFalse(move14.isQuiet());
    }

    @Test
    void isDoublePawnPush() {
        Assertions.assertFalse(move1.isDoublePawnPush());
        Assertions.assertTrue(move2.isDoublePawnPush());
        Assertions.assertFalse(move3.isDoublePawnPush());
        Assertions.assertFalse(move4.isDoublePawnPush());
        Assertions.assertFalse(move5.isDoublePawnPush());
        Assertions.assertFalse(move6.isDoublePawnPush());
        Assertions.assertFalse(move7.isDoublePawnPush());
        Assertions.assertFalse(move8.isDoublePawnPush());
        Assertions.assertFalse(move9.isDoublePawnPush());
        Assertions.assertFalse(move10.isDoublePawnPush());
        Assertions.assertFalse(move11.isDoublePawnPush());
        Assertions.assertFalse(move12.isDoublePawnPush());
        Assertions.assertFalse(move13.isDoublePawnPush());
        Assertions.assertFalse(move14.isDoublePawnPush());
    }

    @Test
    void isKingCastle() {
        Assertions.assertFalse(move1.isKingCastle());
        Assertions.assertFalse(move2.isKingCastle());
        Assertions.assertTrue(move3.isKingCastle());
        Assertions.assertFalse(move4.isKingCastle());
        Assertions.assertFalse(move5.isKingCastle());
        Assertions.assertFalse(move6.isKingCastle());
        Assertions.assertFalse(move7.isKingCastle());
        Assertions.assertFalse(move8.isKingCastle());
        Assertions.assertFalse(move9.isKingCastle());
        Assertions.assertFalse(move10.isKingCastle());
        Assertions.assertFalse(move11.isKingCastle());
        Assertions.assertFalse(move12.isKingCastle());
        Assertions.assertFalse(move13.isKingCastle());
        Assertions.assertFalse(move14.isKingCastle());
    }

    @Test
    void isQueenCastle() {
        Assertions.assertFalse(move1.isQueenCastle());
        Assertions.assertFalse(move2.isQueenCastle());
        Assertions.assertFalse(move3.isQueenCastle());
        Assertions.assertTrue(move4.isQueenCastle());
        Assertions.assertFalse(move5.isQueenCastle());
        Assertions.assertFalse(move6.isQueenCastle());
        Assertions.assertFalse(move7.isQueenCastle());
        Assertions.assertFalse(move8.isQueenCastle());
        Assertions.assertFalse(move9.isQueenCastle());
        Assertions.assertFalse(move10.isQueenCastle());
        Assertions.assertFalse(move11.isQueenCastle());
        Assertions.assertFalse(move12.isQueenCastle());
        Assertions.assertFalse(move13.isQueenCastle());
        Assertions.assertFalse(move14.isQueenCastle());
    }

    @Test
    void isCapture() {
        Assertions.assertFalse(move1.isCapture());
        Assertions.assertFalse(move2.isCapture());
        Assertions.assertFalse(move3.isCapture());
        Assertions.assertFalse(move4.isCapture());
        Assertions.assertTrue(move5.isCapture());
        Assertions.assertTrue(move6.isCapture());
        Assertions.assertFalse(move7.isCapture());
        Assertions.assertFalse(move8.isCapture());
        Assertions.assertFalse(move9.isCapture());
        Assertions.assertFalse(move10.isCapture());
        Assertions.assertTrue(move11.isCapture());
        Assertions.assertTrue(move12.isCapture());
        Assertions.assertTrue(move13.isCapture());
        Assertions.assertTrue(move14.isCapture());
    }

    @Test
    void isEnPassantCapture() {
        Assertions.assertFalse(move1.isEnPassantCapture());
        Assertions.assertFalse(move2.isEnPassantCapture());
        Assertions.assertFalse(move3.isEnPassantCapture());
        Assertions.assertFalse(move4.isEnPassantCapture());
        Assertions.assertFalse(move5.isEnPassantCapture());
        Assertions.assertTrue(move6.isEnPassantCapture());
        Assertions.assertFalse(move7.isEnPassantCapture());
        Assertions.assertFalse(move8.isEnPassantCapture());
        Assertions.assertFalse(move9.isEnPassantCapture());
        Assertions.assertFalse(move10.isEnPassantCapture());
        Assertions.assertFalse(move11.isEnPassantCapture());
        Assertions.assertFalse(move12.isEnPassantCapture());
        Assertions.assertFalse(move13.isEnPassantCapture());
        Assertions.assertFalse(move14.isEnPassantCapture());
    }

    @Test
    void isPromotion() {
        Assertions.assertFalse(move1.isPromotion());
        Assertions.assertFalse(move2.isPromotion());
        Assertions.assertFalse(move3.isPromotion());
        Assertions.assertFalse(move4.isPromotion());
        Assertions.assertFalse(move5.isPromotion());
        Assertions.assertFalse(move6.isPromotion());
        Assertions.assertTrue(move7.isPromotion());
        Assertions.assertTrue(move8.isPromotion());
        Assertions.assertTrue(move9.isPromotion());
        Assertions.assertTrue(move10.isPromotion());
        Assertions.assertTrue(move11.isPromotion());
        Assertions.assertTrue(move12.isPromotion());
        Assertions.assertTrue(move13.isPromotion());
        Assertions.assertTrue(move14.isPromotion());
    }

    @Test
    void isKnightPromotion() {
        Assertions.assertFalse(move1.isKnightPromotion());
        Assertions.assertFalse(move2.isKnightPromotion());
        Assertions.assertFalse(move3.isKnightPromotion());
        Assertions.assertFalse(move4.isKnightPromotion());
        Assertions.assertFalse(move5.isKnightPromotion());
        Assertions.assertFalse(move6.isKnightPromotion());
        Assertions.assertTrue(move7.isKnightPromotion());
        Assertions.assertFalse(move8.isKnightPromotion());
        Assertions.assertFalse(move9.isKnightPromotion());
        Assertions.assertFalse(move10.isKnightPromotion());
        Assertions.assertTrue(move11.isKnightPromotion());
        Assertions.assertFalse(move12.isKnightPromotion());
        Assertions.assertFalse(move13.isKnightPromotion());
        Assertions.assertFalse(move14.isKnightPromotion());
    }

    @Test
    void isBishopPromotion() {
        Assertions.assertFalse(move1.isBishopPromotion());
        Assertions.assertFalse(move2.isBishopPromotion());
        Assertions.assertFalse(move3.isBishopPromotion());
        Assertions.assertFalse(move4.isBishopPromotion());
        Assertions.assertFalse(move5.isBishopPromotion());
        Assertions.assertFalse(move6.isBishopPromotion());
        Assertions.assertFalse(move7.isBishopPromotion());
        Assertions.assertTrue(move8.isBishopPromotion());
        Assertions.assertFalse(move9.isBishopPromotion());
        Assertions.assertFalse(move10.isBishopPromotion());
        Assertions.assertFalse(move11.isBishopPromotion());
        Assertions.assertTrue(move12.isBishopPromotion());
        Assertions.assertFalse(move13.isBishopPromotion());
        Assertions.assertFalse(move14.isBishopPromotion());
    }

    @Test
    void isRookPromotion() {
        Assertions.assertFalse(move1.isRookPromotion());
        Assertions.assertFalse(move2.isRookPromotion());
        Assertions.assertFalse(move3.isRookPromotion());
        Assertions.assertFalse(move4.isRookPromotion());
        Assertions.assertFalse(move5.isRookPromotion());
        Assertions.assertFalse(move6.isRookPromotion());
        Assertions.assertFalse(move7.isRookPromotion());
        Assertions.assertFalse(move8.isRookPromotion());
        Assertions.assertTrue(move9.isRookPromotion());
        Assertions.assertFalse(move10.isRookPromotion());
        Assertions.assertFalse(move11.isRookPromotion());
        Assertions.assertFalse(move12.isRookPromotion());
        Assertions.assertTrue(move13.isRookPromotion());
        Assertions.assertFalse(move14.isRookPromotion());
    }

    @Test
    void isQueenPromotion() {
        Assertions.assertFalse(move1.isQueenPromotion());
        Assertions.assertFalse(move2.isQueenPromotion());
        Assertions.assertFalse(move3.isQueenPromotion());
        Assertions.assertFalse(move4.isQueenPromotion());
        Assertions.assertFalse(move5.isQueenPromotion());
        Assertions.assertFalse(move6.isQueenPromotion());
        Assertions.assertFalse(move7.isQueenPromotion());
        Assertions.assertFalse(move8.isQueenPromotion());
        Assertions.assertFalse(move9.isQueenPromotion());
        Assertions.assertTrue(move10.isQueenPromotion());
        Assertions.assertFalse(move11.isQueenPromotion());
        Assertions.assertFalse(move12.isQueenPromotion());
        Assertions.assertFalse(move13.isQueenPromotion());
        Assertions.assertTrue(move14.isQueenPromotion());
    }

    @Test
    void isKnightPromotionCapture() {
        Assertions.assertFalse(move1.isKnightPromotionCapture());
        Assertions.assertFalse(move2.isKnightPromotionCapture());
        Assertions.assertFalse(move3.isKnightPromotionCapture());
        Assertions.assertFalse(move4.isKnightPromotionCapture());
        Assertions.assertFalse(move5.isKnightPromotionCapture());
        Assertions.assertFalse(move6.isKnightPromotionCapture());
        Assertions.assertFalse(move7.isKnightPromotionCapture());
        Assertions.assertFalse(move8.isKnightPromotionCapture());
        Assertions.assertFalse(move9.isKnightPromotionCapture());
        Assertions.assertFalse(move10.isKnightPromotionCapture());
        Assertions.assertTrue(move11.isKnightPromotionCapture());
        Assertions.assertFalse(move12.isKnightPromotionCapture());
        Assertions.assertFalse(move13.isKnightPromotionCapture());
        Assertions.assertFalse(move14.isKnightPromotionCapture());
    }

    @Test
    void isBishopPromotionCapture() {
        Assertions.assertFalse(move1.isBishopPromotionCapture());
        Assertions.assertFalse(move2.isBishopPromotionCapture());
        Assertions.assertFalse(move3.isBishopPromotionCapture());
        Assertions.assertFalse(move4.isBishopPromotionCapture());
        Assertions.assertFalse(move5.isBishopPromotionCapture());
        Assertions.assertFalse(move6.isBishopPromotionCapture());
        Assertions.assertFalse(move7.isBishopPromotionCapture());
        Assertions.assertFalse(move8.isBishopPromotionCapture());
        Assertions.assertFalse(move9.isBishopPromotionCapture());
        Assertions.assertFalse(move10.isBishopPromotionCapture());
        Assertions.assertFalse(move11.isBishopPromotionCapture());
        Assertions.assertTrue(move12.isBishopPromotionCapture());
        Assertions.assertFalse(move13.isBishopPromotionCapture());
        Assertions.assertFalse(move14.isBishopPromotionCapture());
    }

    @Test
    void isRookPromotionCapture() {
        Assertions.assertFalse(move1.isRookPromotionCapture());
        Assertions.assertFalse(move2.isRookPromotionCapture());
        Assertions.assertFalse(move3.isRookPromotionCapture());
        Assertions.assertFalse(move4.isRookPromotionCapture());
        Assertions.assertFalse(move5.isRookPromotionCapture());
        Assertions.assertFalse(move6.isRookPromotionCapture());
        Assertions.assertFalse(move7.isRookPromotionCapture());
        Assertions.assertFalse(move8.isRookPromotionCapture());
        Assertions.assertFalse(move9.isRookPromotionCapture());
        Assertions.assertFalse(move10.isRookPromotionCapture());
        Assertions.assertFalse(move11.isRookPromotionCapture());
        Assertions.assertFalse(move12.isRookPromotionCapture());
        Assertions.assertTrue(move13.isRookPromotionCapture());
        Assertions.assertFalse(move14.isRookPromotionCapture());
    }

    @Test
    void isQueenPromotionCapture() {
        Assertions.assertFalse(move1.isQueenPromotionCapture());
        Assertions.assertFalse(move2.isQueenPromotionCapture());
        Assertions.assertFalse(move3.isQueenPromotionCapture());
        Assertions.assertFalse(move4.isQueenPromotionCapture());
        Assertions.assertFalse(move5.isQueenPromotionCapture());
        Assertions.assertFalse(move6.isQueenPromotionCapture());
        Assertions.assertFalse(move7.isQueenPromotionCapture());
        Assertions.assertFalse(move8.isQueenPromotionCapture());
        Assertions.assertFalse(move9.isQueenPromotionCapture());
        Assertions.assertFalse(move10.isQueenPromotionCapture());
        Assertions.assertFalse(move11.isQueenPromotionCapture());
        Assertions.assertFalse(move12.isQueenPromotionCapture());
        Assertions.assertFalse(move13.isQueenPromotionCapture());
        Assertions.assertTrue(move14.isQueenPromotionCapture());
    }
}