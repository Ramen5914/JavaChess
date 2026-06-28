package com.r4men.java_chess;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Move(int move, int enPassant, int castlingRights, int halfmoveClock, @Nullable Piece capturedPiece) {
    public enum Flag {
        QUIET_MOVE_FLAG(0b0000),
        DOUBLE_PAWN_PUSH_FLAG(0b0001),
        KING_CASTLE_FLAG(0b0010),
        QUEEN_CASTLE_FLAG(0b0011),
        CAPTURES_FLAG(0b0100),
        EN_PASSANT_CAPTURE_FLAG(0b0101),
        KNIGHT_PROMOTION_FLAG(0b1000),
        BISHOP_PROMOTION_FLAG(0b1001),
        ROOK_PROMOTION_FLAG(0b1010),
        QUEEN_PROMOTION_FLAG(0b1011),
        KNIGHT_PROMOTION_CAPTURE_FLAG(0b1100),
        BISHOP_PROMOTION_CAPTURE_FLAG(0b1101),
        ROOK_PROMOTION_CAPTURE_FLAG(0b1110),
        QUEEN_PROMOTION_CAPTURE_FLAG(0b1111);

        private final int flag;

        Flag(int flag) {
            this.flag = flag;
        }

        public int toInt() {
            return flag;
        }
    }

    public Move(int from, int to, Flag flag, int enPassant, int castlingRights, int halfmoveClock, Piece capturedPiece) {
        if (from >= 64 || from < 0) {
            throw new IllegalArgumentException("'from' square must be in the range [0, 64). Received: " + from);
        }

        if (to >= 64 || to < 0) {
            throw new IllegalArgumentException("'to' square must be in the range [0, 64). Received: " + to);
        }

        if (enPassant >= 64 || enPassant < 0) {
            throw new IllegalArgumentException("'enPassant' square must be in the range [0, 64). Received: " + enPassant);
        }

        this(
                ((flag.toInt() & 0b1111) << 12) | ((from & 0b111111) << 6) | (to & 0b111111),
                enPassant,
                castlingRights,
                halfmoveClock,
                capturedPiece
        );
    }

    @Override
    public @NotNull String toString() {
        return String.format("%d -> %d (%d)", getFrom(), getTo(), getFlags());
    }

    public int getFrom() {
        return (move >> 6) & 0x3f;
    }

    public int getTo() {
        return move & 0x3f;
    }

    public int getFlags() {
        return (move >> 12) & 0x0f;
    }

    public boolean isQuiet() {
        return move >> 12 == Flag.QUIET_MOVE_FLAG.toInt();
    }

    public boolean isDoublePawnPush() {
        return move >> 12 == Flag.DOUBLE_PAWN_PUSH_FLAG.toInt();
    }

    public boolean isKingCastle() {
        return move >> 12 == Flag.KING_CASTLE_FLAG.toInt();
    }

    public boolean isQueenCastle() {
        return move >> 12 == Flag.QUEEN_CASTLE_FLAG.toInt();
    }

    public boolean isCapture() {
        return ((move >> 12) & Flag.CAPTURES_FLAG.toInt()) == Flag.CAPTURES_FLAG.toInt();
    }

    public boolean isEnPassantCapture() {
        return move >> 12 == Flag.EN_PASSANT_CAPTURE_FLAG.toInt();
    }

    public boolean isPromotion() {
        return ((move >> 12) & 0b1000) == 0b1000;
    }

    public boolean isKnightPromotion() {
        return ((move >> 12) & (Flag.KNIGHT_PROMOTION_FLAG.toInt() | 0b1011)) == Flag.KNIGHT_PROMOTION_FLAG.toInt();
    }

    public boolean isBishopPromotion() {
        return ((move >> 12) & (Flag.BISHOP_PROMOTION_FLAG.toInt() | 0b1011)) == Flag.BISHOP_PROMOTION_FLAG.toInt();
    }

    public boolean isRookPromotion() {
        return ((move >> 12) & (Flag.ROOK_PROMOTION_FLAG.toInt() | 0b1011)) == Flag.ROOK_PROMOTION_FLAG.toInt();
    }

    public boolean isQueenPromotion() {
        return ((move >> 12) & (Flag.QUEEN_PROMOTION_FLAG.toInt() | 0b1011)) == Flag.QUEEN_PROMOTION_FLAG.toInt();
    }

    public boolean isKnightPromotionCapture() {
        return move >> 12 == Flag.KNIGHT_PROMOTION_CAPTURE_FLAG.toInt();
    }

    public boolean isBishopPromotionCapture() {
        return move >> 12 == Flag.BISHOP_PROMOTION_CAPTURE_FLAG.toInt();
    }

    public boolean isRookPromotionCapture() {
        return move >> 12 == Flag.ROOK_PROMOTION_CAPTURE_FLAG.toInt();
    }

    public boolean isQueenPromotionCapture() {
        return move >> 12 == Flag.QUEEN_PROMOTION_CAPTURE_FLAG.toInt();
    }

    public int getButterflyIndex() {
        return move & 0xffff;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Move a = (Move) obj;

        return (move & 0xffff) == (a.move & 0xffff);
    }

    @Override
    public int hashCode() {
        return move & 0xffff;
    }
}
