package com.r4men.stockfish_j;

public class Move {
    public static class Flags {
        public static final int QUIET_MOVE_FLAG = 0b0000;
        public static final int DOUBLE_PAWN_PUSH_FLAG = 0b0001;
        public static final int KING_CASTLE_FLAG = 0b0010;
        public static final int QUEEN_CASTLE_FLAG = 0b0011;
        public static final int CAPTURES_FLAG = 0b0100;
        public static final int EN_PASSANT_CAPTURE_FLAG = 0b0101;
        public static final int KNIGHT_PROMOTION_FLAG = 0b1000;
        public static final int BISHOP_PROMOTION_FLAG = 0b1001;
        public static final int ROOK_PROMOTION_FLAG = 0b1010;
        public static final int QUEEN_PROMOTION_FLAG = 0b1011;
        public static final int KNIGHT_PROMOTION_CAPTURE_FLAG = 0b1100;
        public static final int BISHOP_PROMOTION_CAPTURE_FLAG = 0b1101;
        public static final int ROOK_PROMOTION_CAPTURE_FLAG = 0b1110;
        public static final int QUEEN_PROMOTION_CAPTURE_FLAG = 0b1111;
    }

    protected int mMove;

    public Move(int from, int to, int flags) {
        mMove = ((flags & 0b1111) << 12) | ((from & 0b111111) << 6) | (to & 0b111111);
    }

    @Override
    public String toString() {
        return String.format("%d -> %d (%d)", getFrom(), getTo(), getFlags());

//        return String.format("%16s", Integer.toBinaryString(mMove)).replace(' ', '0');
    }

    public void copyFrom(Move a) {
        mMove = a.mMove;
    }

    public int getFrom() {
        return (mMove >> 6) & 0x3f;
    }

    public int getTo() {
        return mMove & 0x3f;
    }

    public int getFlags() {
        return (mMove >> 12) & 0x0f;
    }

    public void setTo(int to) {
        mMove &= ~0x3f;
        mMove |= to & 0x3f;
    }

    public void setFrom(int from) {
        mMove &= ~(0x3f << 6);
        mMove |= (from & 0x3f) << 6;
    }

    public void setFlags(int flags) {
        mMove &= ~(0x0f << 12);
        mMove |= (flags & 0x0f) << 12;
    }

    public boolean isQuiet() {
        return mMove >> 12 == Flags.QUIET_MOVE_FLAG;
    }

    public boolean isDoublePawnPush() {
        return mMove >> 12 == Flags.DOUBLE_PAWN_PUSH_FLAG;
    }

    public boolean isKingCastle() {
        return mMove >> 12 == Flags.KING_CASTLE_FLAG;
    }

    public boolean isQueenCastle() {
        return mMove >> 12 == Flags.QUEEN_CASTLE_FLAG;
    }

    public boolean isCapture() {
        return ((mMove >> 12) & Flags.CAPTURES_FLAG) == Flags.CAPTURES_FLAG;
    }

    public boolean isEnPassantCapture() {
        return mMove >> 12 == Flags.EN_PASSANT_CAPTURE_FLAG;
    }

    public boolean isPromotion() {
        return ((mMove >> 12) & 0b1000) == 0b1000;
    }

    public boolean isKnightPromotion() {
        return ((mMove >> 12) & (Flags.KNIGHT_PROMOTION_FLAG | 0b1011)) == Flags.KNIGHT_PROMOTION_FLAG;
    }

    public boolean isBishopPromotion() {
        return ((mMove >> 12) & (Flags.BISHOP_PROMOTION_FLAG | 0b1011)) == Flags.BISHOP_PROMOTION_FLAG;
    }

    public boolean isRookPromotion() {
        return ((mMove >> 12) & (Flags.ROOK_PROMOTION_FLAG | 0b1011)) == Flags.ROOK_PROMOTION_FLAG;
    }

    public boolean isQueenPromotion() {
        return ((mMove >> 12) & (Flags.QUEEN_PROMOTION_FLAG | 0b1011)) == Flags.QUEEN_PROMOTION_FLAG;
    }

    public boolean isKnightPromotionCapture() {
        return mMove >> 12 == Flags.KNIGHT_PROMOTION_CAPTURE_FLAG;
    }

    public boolean isBishopPromotionCapture() {
        return mMove >> 12 == Flags.BISHOP_PROMOTION_CAPTURE_FLAG;
    }

    public boolean isRookPromotionCapture() {
        return mMove >> 12 == Flags.ROOK_PROMOTION_CAPTURE_FLAG;
    }

    public boolean isQueenPromotionCapture() {
        return mMove >> 12 == Flags.QUEEN_PROMOTION_CAPTURE_FLAG;
    }

    public int getButterflyIndex() {
        return mMove & 0b111111111111;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Move a = (Move) obj;

        return (mMove & 0b111111111111) == (a.mMove & 0b111111111111);
    }

    @Override
    public int hashCode() {
        return mMove & 0b111111111111;
    }

    public short asShort() {
        return (short) (mMove);
    }
}
