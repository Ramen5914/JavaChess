package com.r4men.java_chess;

import com.r4men.java_chess.type.Move;

public final class AttackDetector {
    private long[] attackRays = {
            
    };

    public static boolean isSquareAttacked(Board board, int s10x12, Piece.Color attackingColor) {
        long occ = board.getBitBoard(attackingColor, Piece.PieceType.OCC);

        for (int s8x8 = 0; s8x8 < 64; s8x8++) {
            if (((occ >>> s8x8) & 1L) == 0L) continue;

            int from10x12 = Util.convert8x8to10x12(s8x8);
            Piece piece = board.getPieceAt10x12(from10x12);

            if (canAttack(board, from10x12, s10x12, piece)) {
                return true;
            }
        }

        return false;
    }

    static boolean canAttack(Board board, int from10x12, int to10x12, Piece piece) {
        if (from10x12 == to10x12) return false;

        Direction.D10X12 direction = Util.getD10X12FromSquares(from10x12, to10x12);

        assert (direction != null);

        return switch (piece.getPieceType()) {
            case PAWN -> isValidPawnAttack(from10x12, to10x12, piece);
            case KNIGHT -> isValidKnightAttack(board, from10x12, to10x12);
            case BISHOP -> isValidBishopAttack(board, from10x12, to10x12);
            case ROOK -> isValidRookAttack(board, from10x12, to10x12);
            case QUEEN -> isValidQueenAttack(board, from10x12, to10x12);
            case KING -> isValidKingAttack(board, from10x12, to10x12);
            default -> false;
        };
    }

    private static boolean isValidPawnAttack(int from10x12, int to10x12, Piece piece) {
        int startSquare = from10x12 + (piece.isWhite() ? Direction.D10X12.N.toInt() : Direction.D10X12.S.toInt());

        return to10x12 == startSquare + Direction.D10X12.E.toInt() || to10x12 == startSquare + Direction.D10X12.W.toInt();
    }

    // TODO
    private static boolean isValidKnightAttack(Board board, int from10x12, int to10x12) {
        return false;
    }

    // TODO
    private static boolean isValidBishopAttack(Board board, int from10x12, int to10x12) {
        return false;
    }

    // TODO
    private static boolean isValidRookAttack(Board board, int from10x12, int to10x12) {
        return false;
    }

    // TODO
    private static boolean isValidQueenAttack(Board board, int from10x12, int to10x122) {
        return isValidBishopAttack(board, from10x12, to10x122) || isValidRookAttack(board, from10x12, to10x122);
    }

    // TODO
    private static boolean isValidKingAttack(Board board, int from10x12, int to10x12) {
        int rankDiff = Util.getRankDiff10x12(from10x12, to10x12);
        int fileDiff = Util.getFileDiff10x12(from10x12, to10x12);

        return Math.abs(rankDiff) <= 1 && Math.abs(fileDiff) <= 1 && (rankDiff != 0 || fileDiff != 0);
    }


//    private boolean isValidKnightMove(Move move, Piece destinationPiece, int from10x12, int to10x12) {
//        int rankDiff = Util.getRankDistance10x12(from10x12, to10x12);
//        int fileDiff = Util.getFileDistance10x12(from10x12, to10x12);
//
//        return rankDiff > 0 && fileDiff > 0 && rankDiff + fileDiff == 3;
//    }

    // TODO make this not use makeMove or undoMove, only check for check here
    static boolean isInCheck(Board board, Move move) {
        MoveApplier.makeMove(board, move, true);
        boolean inCheck = AttackDetector.isKingInCheck(board, board.getPlayerToMove().opposite());
        MoveApplier.undoMove(board, move);
        return inCheck;
    }

    public static boolean isKingInCheck(Board board, Piece.Color color) {
        int kingSquare8x8 = Long.numberOfTrailingZeros(board.getBitBoard(color, Piece.PieceType.KING));

        if (kingSquare8x8 == 64) {
            throw new IllegalStateException("No king found on the board for " + color);
        }

        return isSquareAttacked(board, Util.convert8x8to10x12(kingSquare8x8), color.opposite());
    }
}
