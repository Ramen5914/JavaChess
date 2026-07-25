package com.r4men.java_chess;

import com.r4men.java_chess.type.Move;

public final class MoveValidator {
    // TODO move check test to the end of make move
    public static boolean isMoveValid(Board board, Move move) {
        int from10x12 = move.getFrom10x12();
        int to10x12 = move.getTo10x12();
        Piece movedPiece = board.getPieceAt10x12(from10x12);
        Piece destinationPiece = board.getPieceAt10x12(to10x12);

        if (from10x12 == to10x12 || !movedPiece.matchesColor(board.getPlayerToMove()) || destinationPiece.isOffBoard()) {
            return false;
        }

        return isValidMovementForPiece(board, movedPiece, move);
    }

    static boolean isValidMovementForPiece(Board board, Piece movedPiece, Move move) {
        Piece.Color color = movedPiece.getColor();

        return switch (movedPiece.getPieceType()) {
            case PAWN -> isValidPawnMove(board, move, color);
            case KNIGHT -> isValidKnightMove(board, move, color);
            case BISHOP -> isValidBishopMove(board, move, color);
            case ROOK -> isValidRookMove(board, move, color);
            case QUEEN -> isValidQueenMove(board, move, color);
            case KING -> isValidKingMove(board, move, color);
            default -> false;
        };
    }

    static boolean isValidPawnMove(Board board, Move move, Piece.Color color) {
        int from = move.getFrom10x12();
        int to = move.getTo10x12();

        Piece destinationPiece = board.getPieceAt10x12(to);
        Direction.D10X12 direction = color == Piece.Color.WHITE ? Direction.D10X12.N : Direction.D10X12.S;

        if (move.isCapture()) {
            int east = from + direction.toInt() + Direction.D10X12.E.toInt();
            int west = from + direction.toInt() + Direction.D10X12.W.toInt();

            if (!(to == east || to == west)) {
                return false;
            }

            if (move.isEnPassantCapture()) {
                return to == board.getEnPassantSquare10x12() && destinationPiece == move.capturedPiece();
            } else {
                return destinationPiece.getColor() == color.opposite();
            }
        } else {
            if (move.isDoublePawnPush()) {
                return Util.getRankDiff10x12(from, to) == 2 && Util.isPathClear10x12(board, from, to);
            }

            return Util.isRayFrom10x12(from, to, direction) && board.getPieceAt10x12(to).isEmpty();
        }
    }

    static boolean isValidKnightMove(Board board, Move move, Piece.Color color) {
        return false;
    }

    static boolean isValidBishopMove(Board board, Move move, Piece.Color color) {
        return false;
    }

    static boolean isValidRookMove(Board board, Move move, Piece.Color color) {
        return false;
    }

    static boolean isValidQueenMove(Board board, Move move, Piece.Color color) {
        return false;
    }

    static boolean isValidKingMove(Board board, Move move, Piece.Color color) {
        return false;
    }
}
