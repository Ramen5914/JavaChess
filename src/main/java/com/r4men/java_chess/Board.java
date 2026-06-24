package com.r4men.stockfish_j;

import java.util.ArrayList;
import java.util.List;

public class Board {
    //      a   b   c   d   e   f   g   h
    // 8 | 56, 57, 58, 59, 60, 61, 62, 63
    // 7 | 48, 49, 50, 51, 52, 53, 54, 55
    // 6 | 40, 41, 42, 43, 44, 45, 46, 47
    // 5 | 32, 33, 34, 35, 36, 37, 38, 39
    // 4 | 24, 25, 26, 27, 28, 29, 30, 31
    // 3 | 16, 17, 18, 19, 20, 21, 22, 32
    // 2 | 08, 09, 10, 11, 12, 13, 14, 15
    // 1 | 00, 01, 02, 03, 04, 05, 06, 07

    //      X  A  B  C  D  E  F  G  H  X
    // X | 6E 6F 70 71 72 73 74 75 76 77 | X
    // X | 64 65 66 67 68 69 6A 6B 6C 6D | X
    // 8 | 5A 5B 5C 5D 5E 5F 60 61 62 63 | 8
    // 7 | 50 51 52 53 54 55 56 57 58 59 | 7
    // 6 | 46 47 48 49 4A 4B 4C 4D 4E 4F | 6
    // 5 | 3C 3D 3E 3F 40 41 42 43 44 45 | 5
    // 4 | 32 33 34 35 36 37 38 39 3A 3B | 4
    // 3 | 28 29 2A 2B 2C 2D 2E 2F 30 31 | 3
    // 2 | 1E 1F 20 21 22 23 24 25 26 27 | 2
    // 1 | 14 15 16 17 18 19 1A 1B 1C 1D | 1
    // X | 0A 0B 0C 0D 0E 0F 10 11 12 13 | X
    // X | 00 01 02 03 04 05 06 07 08 09 | X
    //      X  A  B  C  D  E  F  G  H  X

    List<Piece> pieces;
    long[][] bitBoards;
    long allOcc;

    int enPassantSquare;
    Piece.Color playerToMove;
    int halfmoveClock;
    int fullmoveNumber;
    int castlingRights;

    // Ply-indexed state arrays for efficient undo (max 1024 plies)
    private static final int MAX_PLIES = 4096*2*2*2;
    private int[] enPassantHistory;
    private int[] castlingRightsHistory;
    private int[] halfmoveClockHistory;
    private Piece[] capturedPieceHistory;
    private int ply;

    public Board() {
        pieces = new ArrayList<>(64);
        for (int i = 0; i < 64; i++) {
            pieces.add(Piece.EMPTY);
        }

        bitBoards = new long[2][7];
        allOcc = 0L;

        enPassantSquare = -1;
        playerToMove = Piece.Color.WHITE;
        halfmoveClock = 0;
        fullmoveNumber = 1;

        //                 qkQK
        castlingRights = 0b0000;

        // Initialize history arrays
        enPassantHistory = new int[MAX_PLIES];
        castlingRightsHistory = new int[MAX_PLIES];
        halfmoveClockHistory = new int[MAX_PLIES];
        capturedPieceHistory = new Piece[MAX_PLIES];
        ply = 0;
    }

    public Board(String fen) {
        this();

        boolean isValid = fen.matches("^\\s*((?:[rnbqkpRNBQKP1-8]{1,8}/){7}[rnbqkpRNBQKP1-8]{1,8})\\s[wWbB]\\s(-|[KQkq]{1,4})\\s(-|[a-h][36])\\s(\\d+)\\s(\\d+)\\s*");

        if (isValid) {
            String[] parts = fen.split(" ");
            String[] pieceRows = parts[0].split("/");
            String playerToMove = parts[1];
            String castlingRights = parts[2];
            String enPassantSquare = parts[3];
            halfmoveClock = Integer.parseInt(parts[4]);
            fullmoveNumber = Integer.parseInt(parts[5]);

            for (int y = 0; y < 8; y++) {
                int file = 0;

                for (int i = 0; i < pieceRows[y].length(); i++) {
                    char c = pieceRows[y].charAt(i);
                    int index = 64 - (8 * (y + 1)) + file;

                    if (Character.isDigit(c)) {
                        int emptySquares = Character.getNumericValue(c);
                        for (int j = 0; j < emptySquares; j++) {
                            setSquare(index + j, Piece.EMPTY);
                        }
                        file += emptySquares;
                    } else {
                        if (Character.isUpperCase(c)) {
                            setSquare(index, switch (c) {
                                case 'B' -> Piece.WHITE_BISHOP;
                                case 'K' -> Piece.WHITE_KING;
                                case 'N' -> Piece.WHITE_KNIGHT;
                                case 'P' -> Piece.WHITE_PAWN;
                                case 'Q' -> Piece.WHITE_QUEEN;
                                case 'R' -> Piece.WHITE_ROOK;
                                default -> throw new IllegalArgumentException("Invalid piece character: " + c);
                            });
                        } else {
                            setSquare(index, switch (c) {
                                case 'b' -> Piece.BLACK_BISHOP;
                                case 'k' -> Piece.BLACK_KING;
                                case 'n' -> Piece.BLACK_KNIGHT;
                                case 'p' -> Piece.BLACK_PAWN;
                                case 'q' -> Piece.BLACK_QUEEN;
                                case 'r' -> Piece.BLACK_ROOK;
                                default -> throw new IllegalArgumentException("Invalid piece character: " + c);
                            });
                        }

                        file++;
                    }
                }
            }

            this.playerToMove = switch (playerToMove.toLowerCase()) {
                case "w" -> Piece.Color.WHITE;
                case "b" -> Piece.Color.BLACK;
                default -> throw new IllegalArgumentException("Invalid active color: " + playerToMove);
            };

            this.castlingRights = 0b0000;
            for (char c : castlingRights.toCharArray()) {
                switch (c) {
                    case 'K' -> this.castlingRights |= 0b0001;
                    case 'Q' -> this.castlingRights |= 0b0010;
                    case 'k' -> this.castlingRights |= 0b0100;
                    case 'q' -> this.castlingRights |= 0b1000;
                    default -> {
                    }
                }
            }

            if (enPassantSquare.equals("-")) {
                this.enPassantSquare = -1;
            } else {
                char file = enPassantSquare.charAt(0);
                char rank = enPassantSquare.charAt(1);
                this.enPassantSquare = (rank - 1) * 8 + ((int) file - 97);
            }
        } else {
            throw new IllegalArgumentException("Invalid FEN string: " + fen);
        }
    }

    public void makeMove(Move move) {
        int from = move.getFrom();
        int to = move.getTo();

        Piece piece = pieces.get(from);

        // Save the current board state to history indexed by ply
        enPassantHistory[ply] = enPassantSquare;
        castlingRightsHistory[ply] = castlingRights;
        halfmoveClockHistory[ply] = halfmoveClock;
        // For en passant captures the captured pawn is not on the destination square
        if (move.isEnPassantCapture()) {
            capturedPieceHistory[ply] = pieces.get(to + (playerToMove.isWhite() ? -8 : 8));
        } else {
            capturedPieceHistory[ply] = pieces.get(to);
        }

        if (isMoveValid(move)) {
            emptySquare(from);
            emptySquare(to);

            enPassantSquare = -1;

            if (move.isQuiet() || move.isDoublePawnPush()) {
                setSquare(to, piece);

                if (move.isDoublePawnPush()) {
                    markEnPassantSquare(move);
                }

            } else if (move.isCapture()) {
                if (move.isPromotion()) {
                    Piece.Color color = piece.getColor();

                    if (move.isQueenPromotion()) {
                        if (color.isWhite()) {
                            setSquare(to, Piece.WHITE_QUEEN);
                        } else {
                            setSquare(to, Piece.BLACK_QUEEN);
                        }
                    } else if (move.isRookPromotion()) {
                        if (color.isWhite()) {
                            setSquare(to, Piece.WHITE_ROOK);
                        } else {
                            setSquare(to, Piece.BLACK_ROOK);
                        }
                    }
                } else {
                    setSquare(to, piece);

                    if (move.isEnPassantCapture()) {
                        removeEnPassantPawn(move);
                    }
                }
            } else if (move.isKingCastle()) {
                // For kingside castling, the move is encoded with 'to' == rook's original square
                // King moves to the appropriate square and rook is moved from its original square
                if (playerToMove.isWhite()) {
                    // King from e1 to g1
                    setSquare(6, piece);
                    emptySquare(from);
                    // Rook moves to f1 (which is 'to')
                    setSquare(5, Piece.WHITE_ROOK);
                    emptySquare(7);
                } else {
                    // King from e8 to g8
                    setSquare(62, piece);
                    emptySquare(from);
                    // Rook moves to f8 (which is 'to')
                    setSquare(61, Piece.BLACK_ROOK);
                    emptySquare(63);
                }
            } else if (move.isQueenCastle()) {
                // For queenside castling, the move is encoded with 'to' == rook's original square
                // King moves to the appropriate square and rook is moved from its original square
                if (playerToMove.isWhite()) {
                    // King from e1 to c1
                    setSquare(2, piece);
                    emptySquare(from);
                    // Rook moves to d1 (which is 'to')
                    setSquare(3, Piece.WHITE_ROOK);
                    emptySquare(0);
                } else {
                    // King from e8 to c8
                    setSquare(58, piece);
                    emptySquare(from);
                    // Rook moves to d8 (which is 'to')
                    setSquare(59, Piece.BLACK_ROOK);
                    emptySquare(56);
                }
            }

            switch (playerToMove) {
                case WHITE: {
                    if ((castlingRights & 0b0011) != 0) {
                        // Remove castling rights if king moves
                        if (piece.isKing()) {
                            castlingRights &= ~0b0011; // Remove KQ
                        }
                        // Remove castling rights if rook moves from initial position
                        if (piece.getPieceType() == Piece.PieceType.ROOK) {
                            if (from == 0) { // a1
                                castlingRights &= ~0b0010; // Remove Q (queenside)
                            }
                            if (from == 7) { // h1
                                castlingRights &= ~0b0001; // Remove K (kingside)
                            }
                        }
                        // Remove opponent's castling rights if rook is captured
                        if (move.isCapture()) {
                            if (to == 56) { // a8
                                castlingRights &= ~0b1000; // Remove q (black queenside)
                            }
                            if (to == 63) { // h8
                                castlingRights &= ~0b0100; // Remove k (black kingside)
                            }
                        }
                    }

                    break;
                }
                case BLACK: {
                    if ((castlingRights & 0b1100) != 0) {
                        // Remove castling rights if king moves
                        if (piece.getPieceType() == Piece.PieceType.KING) {
                            castlingRights &= ~0b0100; // Remove k (kingside)
                            castlingRights &= ~0b1000; // Remove q (queenside)
                        }
                        // Remove castling rights if rook moves from initial position
                        if (piece.getPieceType() == Piece.PieceType.ROOK) {
                            if (from == 56) { // a8
                                castlingRights &= ~0b1000; // Remove q (queenside)
                            }
                            if (from == 63) { // h8
                                castlingRights &= ~0b0100; // Remove k (kingside)
                            }
                        }
                        // Remove opponent's castling rights if rook is captured
                        if (move.isCapture()) {
                            if (to == 0) { // a1
                                castlingRights &= ~0b0010; // Remove Q (white queenside)
                            }
                            if (to == 7) { // h1
                                castlingRights &= ~0b0001; // Remove K (white kingside)
                            }
                        }
                    }

                    break;
                }
            }

            if (piece.isPawn() || move.isCapture()) {
                halfmoveClock = 0;
            } else {
                halfmoveClock++;
            }

            flipPlayerToMove();
            ply++;
        }
    }

    private void setSquare(int square, Piece piece) {
        Piece previousPiece = pieces.get(square);
        long bit = 1L << square;

        if (piece.isEmpty()) {
            if (!previousPiece.isEmpty()) {
                pieces.set(square, Piece.EMPTY);

                int color = previousPiece.getColor().ordinal();

                bitBoards[color][previousPiece.getPieceType().ordinal()] &= ~bit;
                bitBoards[color][Piece.PieceType.OCC.ordinal()] &= ~bit;
                allOcc &= ~bit;
            }
        } else {
            pieces.set(square, piece);

            bitBoards[piece.getColor().ordinal()][piece.getPieceType().ordinal()] |= bit;
            bitBoards[piece.getColor().ordinal()][Piece.PieceType.OCC.ordinal()] |= bit;
            allOcc |= bit;
        }
    }

    private void emptySquare(int square) {
        setSquare(square, Piece.EMPTY);
    }

    private void flipPlayerToMove() {
        playerToMove = playerToMove.opposite();

        if (playerToMove.isWhite()) {
            fullmoveNumber++;
        }
    }

    private void removeEnPassantPawn(Move move) {
        int to = move.getTo();

        if (pieces.get(to).isWhite()) {
            emptySquare(to - 8);
        } else {
            emptySquare(to + 8);
        }
    }

    private void markEnPassantSquare(Move move) {
        int to = move.getTo();

        if (pieces.get(to).isWhite()) {
            enPassantSquare = to - 8;
        } else {
            enPassantSquare = to + 8;
        }
    }

    private boolean wouldBeInCheckAfterMove(Move move) {
        // Temporarily apply the move
        int from = move.getFrom();
        int to = move.getTo();

        Piece piece = pieces.get(from);
        // For en passant captures the captured pawn is not on the destination square
        Piece capturedPiece = move.isEnPassantCapture()
                ? pieces.get(to + (piece.isWhite() ? -8 : 8))
                : pieces.get(to);

        // Apply the move temporarily
        emptySquare(from);

        // If en passant, also remove the captured pawn
        if (move.isEnPassantCapture()) {
            setSquare(to, piece);
            if (piece.isWhite()) {
                emptySquare(to - 8);
            } else {
                emptySquare(to + 8);
            }
        } else if (move.isKingCastle()) {
            // For kingside castling, the move is encoded with 'to' == rook's original square
            // Move king to final position and rook from its original square to its destination
            if (playerToMove.isWhite()) {
                setSquare(6, piece); // King to g1
                emptySquare(7); // h1
                setSquare(5, Piece.WHITE_ROOK); // f1
            } else {
                setSquare(62, piece); // King to g8
                emptySquare(63); // h8
                setSquare(61, Piece.BLACK_ROOK); // f8
            }
        } else if (move.isQueenCastle()) {
            // For queenside castling, the move is encoded with 'to' == rook's original square
            // Move king to final position and rook from its original square to its destination
            if (playerToMove.isWhite()) {
                setSquare(2, piece); // King to c1
                emptySquare(0); // a1
                setSquare(3, Piece.WHITE_ROOK); // d1
            } else {
                setSquare(58, piece); // King to c8
                emptySquare(56); // a8
                setSquare(59, Piece.BLACK_ROOK); // d8
            }
        } else {
            setSquare(to, piece);
        }

        // Check if king is in check
        boolean inCheck = isKingInCheck(playerToMove);

        // Undo the move
        if (move.isEnPassantCapture()) {
            emptySquare(to);
            setSquare(from, piece);
            setSquare(to + (piece.isWhite() ? -8 : 8), capturedPiece);
        } else if (move.isKingCastle()) {
            // Restore kingside castling
            if (playerToMove.isWhite()) {
                emptySquare(6); // g1
                emptySquare(5); // f1
                setSquare(from, piece); // King back to e1
                setSquare(7, Piece.WHITE_ROOK); // Rook back to h1
            } else {
                emptySquare(62); // g8
                emptySquare(61); // f8
                setSquare(from, piece); // King back to e8
                setSquare(63, Piece.BLACK_ROOK); // Rook back to h8
            }
        } else if (move.isQueenCastle()) {
            // Restore queenside castling
            if (playerToMove.isWhite()) {
                emptySquare(2); // c1
                emptySquare(3); // d1
                setSquare(from, piece); // King back to e1
                setSquare(0, Piece.WHITE_ROOK); // Rook back to a1
            } else {
                emptySquare(58); // c8
                emptySquare(59); // d8
                setSquare(from, piece); // King back to e8
                setSquare(56, Piece.BLACK_ROOK); // Rook back to a8
            }
        } else {
            emptySquare(to);
            setSquare(from, piece);
            if (!capturedPiece.isEmpty()) {
                setSquare(to, capturedPiece);
            }
        }

        return inCheck;
    }

    private boolean isKingInCheck(Piece.Color color) {
        // Find the king
        int kingSquare = -1;
        for (int i = 0; i < 64; i++) {
            Piece p = pieces.get(i);
            if (p.isKing() && p.getColor() == color) {
                kingSquare = i;
                break;
            }
        }

        if (kingSquare == -1) {
            return false; // King not found (shouldn't happen in valid game)
        }

        // Check if the king's square is attacked by opponent
        Piece.Color opponent = color.opposite();
        return isSquareAttackedBy(kingSquare, opponent);
    }

    private boolean isSquareAttackedBy(int square, Piece.Color attackingColor) {
        // Check if any piece of the attacking color can attack this square
        for (int i = 0; i < 64; i++) {
            Piece attacker = pieces.get(i);

            if (attacker.isEmpty() || attacker.getColor() != attackingColor) {
                continue;
            }

            // Check if this piece can attack the target square
            if (canPieceAttackSquare(i, square, attacker)) {
                return true;
            }
        }

        return false;
    }

    private boolean isSquareUnderAttack(int square, Piece.Color defendingColor) {
        // Check if a square is under attack by the opponent of the defending color
        return isSquareAttackedBy(square, defendingColor.opposite());
    }

    private boolean canPieceAttackSquare(int from, int to, Piece piece) {
        if (from == to) {
            return false;
        }

        int fromRank = from / 8;
        int fromFile = from % 8;
        int toRank = to / 8;
        int toFile = to % 8;
        int rankDiff = toRank - fromRank;
        int fileDiff = toFile - fromFile;

        switch (piece.getPieceType()) {
            case PAWN:
                // Pawns attack diagonally one square forward
                int direction = piece.isWhite() ? 1 : -1;
                return rankDiff == direction && Math.abs(fileDiff) == 1;

            case KNIGHT:
                // Knight moves in L-shape
                int absDiff = Math.abs(rankDiff) + Math.abs(fileDiff);
                return absDiff == 3 && Math.abs(rankDiff) != 0 && Math.abs(fileDiff) != 0;

            case BISHOP:
                // Bishop moves diagonally
                return Math.abs(rankDiff) == Math.abs(fileDiff) && rankDiff != 0 && isPathClear(from, to);

            case ROOK:
                // Rook moves horizontally or vertically
                return ((rankDiff == 0 && fileDiff != 0) || (rankDiff != 0 && fileDiff == 0)) && isPathClear(from, to);

            case QUEEN:
                // Queen moves like rook or bishop
                boolean isRookMove = (rankDiff == 0 && fileDiff != 0) || (rankDiff != 0 && fileDiff == 0);
                boolean isBishopMove = Math.abs(rankDiff) == Math.abs(fileDiff) && rankDiff != 0;
                return (isRookMove || isBishopMove) && isPathClear(from, to);

            case KING:
                // King moves one square in any direction
                return Math.abs(rankDiff) <= 1 && Math.abs(fileDiff) <= 1 && (rankDiff != 0 || fileDiff != 0);

            default:
                return false;
        }
    }

    private boolean isMoveValid(Move move) {
        int from = move.getFrom();
        int to = move.getTo();

        // Source and destination must be different
        if (from == to) {
            return false;
        }

        Piece piece = pieces.get(from);
        Piece targetPiece = pieces.get(to);

        // Source square must have a piece belonging to the current player
        if (piece.isEmpty() || piece.getColor() != playerToMove) {
            return false;
        }

        // Destination must not have own piece (unless it's a special move, but captures handle this)
        if (!targetPiece.isEmpty() && targetPiece.getColor() == playerToMove) {
            return false;
        }

        // Validate capture flag consistency
        boolean isCapture = move.isCapture();
        boolean hasTargetPiece = !targetPiece.isEmpty();
        boolean isEnPassant = move.isEnPassantCapture();

        if (isCapture != (hasTargetPiece || isEnPassant)) {
            return false;
        }

        // Validate based on piece type
        if (!isValidMovementForPiece(move, piece)) {
            return false;
        }

        // Check that the move doesn't leave the king in check
        return !wouldBeInCheckAfterMove(move);
    }

    private boolean isValidMovementForPiece(Move move, Piece piece) {
        int from = move.getFrom();
        int to = move.getTo();
        int fromRank = from / 8;
        int fromFile = from % 8;
        int toRank = to / 8;
        int toFile = to % 8;
        int rankDiff = toRank - fromRank;
        int fileDiff = toFile - fromFile;

        switch (piece.getPieceType()) {
            case PAWN:
                return isValidPawnMove(move, from, to, rankDiff, fileDiff);

            case KNIGHT:
                return isValidKnightMove(rankDiff, fileDiff);

            case BISHOP:
                return isValidBishopMove(rankDiff, fileDiff) && isPathClear(from, to);

            case ROOK:
                return isValidRookMove(rankDiff, fileDiff) && isPathClear(from, to);

            case QUEEN:
                return isValidQueenMove(rankDiff, fileDiff) && isPathClear(from, to);

            case KING:
                return isValidKingMove(move, rankDiff, fileDiff);

            default:
                return false;
        }
    }

    private boolean isValidPawnMove(Move move, int from, int to, int rankDiff, int fileDiff) {
        Piece piece = pieces.get(from);
        Piece targetPiece = pieces.get(to);
        int toRank = to / 8;
        int direction = piece.isWhite() ? 1 : -1;

        if (move.isDoublePawnPush()) {
            // Double pawn push from starting position
            int startRank = piece.isWhite() ? 1 : 6;
            if (from / 8 != startRank || rankDiff != 2 * direction || fileDiff != 0) {
                return false;
            }
            // Path must be clear
            int midSquare = from + 8 * direction;
            return pieces.get(midSquare).isEmpty() && targetPiece.isEmpty();
        }

        if (move.isEnPassantCapture()) {
            // En passant capture
            if (rankDiff != direction || Math.abs(fileDiff) != 1) {
                return false;
            }
            if (enPassantSquare != to) {
                return false;
            }
            return true;
        }

        if (move.isPromotion()) {
            // Pawn promotion (only on last rank)
            int promotionRank = piece.isWhite() ? 7 : 0;
            if (toRank != promotionRank) {
                return false;
            }
            if (move.isCapture()) {
                return rankDiff == direction && Math.abs(fileDiff) == 1 && !targetPiece.isEmpty();
            } else {
                return rankDiff == direction && fileDiff == 0 && targetPiece.isEmpty();
            }
        }

        if (move.isCapture()) {
            // Regular pawn capture (non-promotion)
            return rankDiff == direction && Math.abs(fileDiff) == 1 && !targetPiece.isEmpty();
        }

        // Regular pawn move (quiet, non-promotion)
        if (fileDiff != 0) {
            return false; // Pawns can only move diagonally when capturing
        }
        if (rankDiff != direction) {
            return false; // Pawns move one square forward
        }
        return targetPiece.isEmpty();
    }

    private boolean isValidKnightMove(int rankDiff, int fileDiff) {
        int absDiff = Math.abs(rankDiff) + Math.abs(fileDiff);
        if (absDiff != 3) {
            return false;
        }
        return Math.abs(rankDiff) != 0 && Math.abs(fileDiff) != 0;
    }

    private boolean isValidBishopMove(int rankDiff, int fileDiff) {
        return Math.abs(rankDiff) == Math.abs(fileDiff) && rankDiff != 0;
    }

    private boolean isValidRookMove(int rankDiff, int fileDiff) {
        return (rankDiff == 0 && fileDiff != 0) || (rankDiff != 0 && fileDiff == 0);
    }

    private boolean isValidQueenMove(int rankDiff, int fileDiff) {
        return isValidRookMove(rankDiff, fileDiff) || isValidBishopMove(rankDiff, fileDiff);
    }

    private boolean isValidKingMove(Move move, int rankDiff, int fileDiff) {
        int from = move.getFrom();
        int to = move.getTo();

        if (move.isKingCastle()) {
            // Kingside castling - 'to' is the rook's original square
            // White: king from e1 (4) and rook at h1 (7)
            // Black: king from e8 (60) and rook at h8 (63)
            if (playerToMove.isWhite()) {
                return from == 4 && to == 7 && isKingsideCastleLegal();
            } else {
                return from == 60 && to == 63 && isKingsideCastleLegal();
            }
        }

        if (move.isQueenCastle()) {
            // Queenside castling - 'to' is the rook's original square
            // White: king from e1 (4) and rook at a1 (0)
            // Black: king from e8 (60) and rook at a8 (56)
            if (playerToMove.isWhite()) {
                return from == 4 && to == 0 && isQueensideCastleLegal();
            } else {
                return from == 60 && to == 56 && isQueensideCastleLegal();
            }
        }

        // Regular king move
        return Math.abs(rankDiff) <= 1 && Math.abs(fileDiff) <= 1 &&
               (rankDiff != 0 || fileDiff != 0);
    }

    private boolean isPathClear(int from, int to) {
        int fromRank = from / 8;
        int fromFile = from % 8;
        int toRank = to / 8;
        int toFile = to % 8;

        int rankDiff = Integer.compare(toRank, fromRank);
        int fileDiff = Integer.compare(toFile, fromFile);

        int currentRank = fromRank + rankDiff;
        int currentFile = fromFile + fileDiff;

        while (currentRank != toRank || currentFile != toFile) {
            int square = currentRank * 8 + currentFile;
            if (!pieces.get(square).isEmpty()) {
                return false;
            }
            currentRank += rankDiff;
            currentFile += fileDiff;
        }

        return true;
    }

    private boolean isKingsideCastleLegal() {
        if (playerToMove.isWhite()) {
            if ((castlingRights & 0b0001) == 0) {
                return false; // No kingside castling rights
            }
            // Squares e1, f1, g1 must be unoccupied
            if (!(pieces.get(4).isKing() && pieces.get(5).isEmpty() && pieces.get(6).isEmpty() &&
                   pieces.get(7).isRook())) {
                return false;
            }
            // King must not be in check, and cannot move through check
            // Check e1 (from), f1 (through), g1 (to)
            return !isSquareUnderAttack(4, playerToMove) &&
                   !isSquareUnderAttack(5, playerToMove) &&
                   !isSquareUnderAttack(6, playerToMove);
        } else {
            if ((castlingRights & 0b0100) == 0) {
                return false; // No kingside castling rights
            }
            // Squares e8, f8, g8 must be unoccupied
            if (!(pieces.get(60).isKing() && pieces.get(61).isEmpty() && pieces.get(62).isEmpty() &&
                   pieces.get(63).isRook())) {
                return false;
            }
            // King must not be in check, and cannot move through check
            // Check e8 (from), f8 (through), g8 (to)
            return !isSquareUnderAttack(60, playerToMove) &&
                   !isSquareUnderAttack(61, playerToMove) &&
                   !isSquareUnderAttack(62, playerToMove);
        }
    }

    private boolean isQueensideCastleLegal() {
        if (playerToMove.isWhite()) {
            if ((castlingRights & 0b0010) == 0) {
                return false; // No queenside castling rights
            }
            // Squares a1, b1, c1, d1, e1 must be unoccupied
            if (!(pieces.get(0).isRook() && pieces.get(1).isEmpty() && pieces.get(2).isEmpty() &&
                   pieces.get(3).isEmpty() && pieces.get(4).isKing())) {
                return false;
            }
            // King must not be in check, and cannot move through check
            // Check e1 (from), d1 (through), c1 (to)
            return !isSquareUnderAttack(4, playerToMove) &&
                   !isSquareUnderAttack(3, playerToMove) &&
                   !isSquareUnderAttack(2, playerToMove);
        } else {
            if ((castlingRights & 0b1000) == 0) {
                return false; // No queenside castling rights
            }
            // Squares a8, b8, c8, d8, e8 must be unoccupied
            if (!(pieces.get(56).isRook() && pieces.get(57).isEmpty() && pieces.get(58).isEmpty() &&
                   pieces.get(59).isEmpty() && pieces.get(60).isKing())) {
                return false;
            }
            // King must not be in check, and cannot move through check
            // Check e8 (from), d8 (through), c8 (to)
            return !isSquareUnderAttack(60, playerToMove) &&
                   !isSquareUnderAttack(59, playerToMove) &&
                   !isSquareUnderAttack(58, playerToMove);
        }
    }

    public void undoMove(Move move) {
        int from = move.getFrom();
        int to = move.getTo();

        // Decrement ply and restore captured piece from history
        ply--;
        Piece capturedPiece = capturedPieceHistory[ply];

        // The side that made the move is the opposite of the current playerToMove
        Piece.Color mover = playerToMove.opposite();

        // Handle castling
        if (move.isKingCastle()) {
            // Castling moves are encoded with 'to' == rook's original square; undo by moving
            // king and rook back to their original squares based on the mover
            if (mover.isWhite()) {
                // King is at g1 (6), move back to e1 (4)
                emptySquare(6);
                setSquare(4, Piece.WHITE_KING);
                // Rook is at f1 (5), move back to h1 (7)
                emptySquare(5);
                setSquare(7, Piece.WHITE_ROOK);
            } else {
                // King is at g8 (62), move back to e8 (60)
                emptySquare(62);
                setSquare(60, Piece.BLACK_KING);
                // Rook is at f8 (61), move back to h8 (63)
                emptySquare(61);
                setSquare(63, Piece.BLACK_ROOK);
            }

        } else if (move.isQueenCastle()) {
            // Undo queenside castling
            if (mover.isWhite()) {
                // King is at c1 (2), move back to e1 (4)
                emptySquare(2);
                setSquare(4, Piece.WHITE_KING);
                // Rook is at d1 (3), move back to a1 (0)
                emptySquare(3);
                setSquare(0, Piece.WHITE_ROOK);
            } else {
                // King is at c8 (58), move back to e8 (60)
                emptySquare(58);
                setSquare(60, Piece.BLACK_KING);
                // Rook is at d8 (59), move back to a8 (56)
                emptySquare(59);
                setSquare(56, Piece.BLACK_ROOK);
            }

        } else if (move.isPromotion()) {
            // Get the moved piece from destination before overwriting
            Piece movedPiece = pieces.get(to);
            // Remove promoted piece from destination
            emptySquare(to);

            // Restore the pawn to the source square
            setSquare(from, mover == Piece.Color.WHITE ? Piece.WHITE_PAWN : Piece.BLACK_PAWN);

            // Restore captured piece if any
            if (!capturedPiece.isEmpty()) {
                setSquare(to, capturedPiece);
            }

        } else if (move.isCapture()) {
            // Get the moved piece from destination before overwriting
            Piece movedPiece = pieces.get(to);
            // Regular capture (non-promotion, non-castling)
            if (move.isEnPassantCapture()) {
                // En passant: captured pawn is not on the destination square
                emptySquare(to);
                setSquare(from, movedPiece);
                // Restore the captured pawn to its actual square
                int capSquare = mover.isWhite() ? to - 8 : to + 8;
                setSquare(capSquare, capturedPiece);
            } else {
                // Regular capture
                emptySquare(to);
                // Move the captured piece back to 'to'
                setSquare(to, capturedPiece);
                // Restore moving piece back to 'from'
                setSquare(from, movedPiece);
            }

        } else {
            // Get the moved piece from destination before overwriting
            Piece movedPiece = pieces.get(to);
            // Quiet move or double pawn push
            emptySquare(to);
            setSquare(from, movedPiece);
        }

        // Restore board state from history
        enPassantSquare = enPassantHistory[ply];
        castlingRights = castlingRightsHistory[ply];
        halfmoveClock = halfmoveClockHistory[ply];

        // Flip player to move back
        playerToMove = playerToMove.opposite();
        if (playerToMove.isBlack()) {
            fullmoveNumber--;
        }
    }

    public int generateLegalMoves(Move[] moveList) {
        // Initialize all Move objects in the array
        for (int i = 0; i < moveList.length; i++) {
            if (moveList[i] == null) {
                moveList[i] = new Move(0, 0, 0);
            }
        }

        int moveCount = 0;

        // Iterate through all squares
        for (int from = 0; from < 64; from++) {
            Piece piece = pieces.get(from);

            // Skip empty squares and opponent pieces
            if (piece.isEmpty() || piece.getColor() != playerToMove) {
                continue;
            }

            // Generate moves for this piece
            moveCount = generateMovesForPiece(from, piece, moveList, moveCount);
        }

        return moveCount;
    }

    private int generateMovesForPiece(int from, Piece piece, Move[] moveList, int moveCount) {
        switch (piece.getPieceType()) {
            case PAWN:
                moveCount = generatePawnMoves(from, moveList, moveCount);
                break;
            case KNIGHT:
                moveCount = generateKnightMoves(from, moveList, moveCount);
                break;
            case BISHOP:
                moveCount = generateBishopMoves(from, moveList, moveCount);
                break;
            case ROOK:
                moveCount = generateRookMoves(from, moveList, moveCount);
                break;
            case QUEEN:
                moveCount = generateQueenMoves(from, moveList, moveCount);
                break;
            case KING:
                moveCount = generateKingMoves(from, moveList, moveCount);
                break;
            default:
                break;
        }
        return moveCount;
    }

    private int generatePawnMoves(int from, Move[] moveList, int moveCount) {
        Piece piece = pieces.get(from);
        int toRank = from / 8;
        int toFile = from % 8;
        int direction = piece.isWhite() ? 1 : -1;
        int promotionRank = piece.isWhite() ? 7 : 0;

        // Forward move
        int forwardSquare = from + 8 * direction;
        if (forwardSquare >= 0 && forwardSquare < 64 && pieces.get(forwardSquare).isEmpty()) {
            if (toRank + direction == promotionRank) {
                // Promotion moves
                for (int promFlag : new int[]{
                    Move.Flags.KNIGHT_PROMOTION_FLAG,
                    Move.Flags.BISHOP_PROMOTION_FLAG,
                    Move.Flags.ROOK_PROMOTION_FLAG,
                    Move.Flags.QUEEN_PROMOTION_FLAG
                }) {
                    Move move = new Move(from, forwardSquare, promFlag);
                    if (isMoveValid(move)) {
                        moveList[moveCount++].copyFrom(move);
                    }
                }
            } else {
                // Regular forward move
                Move move = new Move(from, forwardSquare, Move.Flags.QUIET_MOVE_FLAG);
                if (isMoveValid(move)) {
                    moveList[moveCount++].copyFrom(move);
                }
            }

            // Double pawn push
            int startRank = piece.isWhite() ? 1 : 6;
            if (toRank == startRank) {
                int doubleSquare = from + 16 * direction;
                if (pieces.get(doubleSquare).isEmpty()) {
                    Move move = new Move(from, doubleSquare, Move.Flags.DOUBLE_PAWN_PUSH_FLAG);
                    if (isMoveValid(move)) {
                        moveList[moveCount++].copyFrom(move);
                    }
                }
            }
        }

        // Diagonal captures
        for (int fileDelta : new int[]{-1, 1}) {
            int captureSquare = from + 8 * direction + fileDelta;
            if (captureSquare >= 0 && captureSquare < 64) {
                int captureFile = captureSquare % 8;
                int currentFile = from % 8;
                if (Math.abs(captureFile - currentFile) == 1) {
                    Piece target = pieces.get(captureSquare);

                    if (toRank + direction == promotionRank) {
                        // Promotion captures
                        for (int promFlag : new int[]{
                            Move.Flags.KNIGHT_PROMOTION_CAPTURE_FLAG,
                            Move.Flags.BISHOP_PROMOTION_CAPTURE_FLAG,
                            Move.Flags.ROOK_PROMOTION_CAPTURE_FLAG,
                            Move.Flags.QUEEN_PROMOTION_CAPTURE_FLAG
                        }) {
                            Move move = new Move(from, captureSquare, promFlag);
                            if (isMoveValid(move)) {
                                moveList[moveCount++].copyFrom(move);
                            }
                        }
                    } else if (!target.isEmpty() && target.getColor() != playerToMove) {
                        // Regular capture
                        Move move = new Move(from, captureSquare, Move.Flags.CAPTURES_FLAG);
                        if (isMoveValid(move)) {
                            moveList[moveCount++].copyFrom(move);
                        }
                    } else if (enPassantSquare == captureSquare) {
                        // En passant
                        Move move = new Move(from, captureSquare, Move.Flags.EN_PASSANT_CAPTURE_FLAG);
                        if (isMoveValid(move)) {
                            moveList[moveCount++].copyFrom(move);
                        }
                    }
                }
            }
        }

        return moveCount;
    }

    private int generateKnightMoves(int from, Move[] moveList, int moveCount) {
        int[][] knightOffsets = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] offset : knightOffsets) {
            int to = from + offset[0] * 8 + offset[1];
            if (to >= 0 && to < 64) {
                int fromFile = from % 8;
                int toFile = to % 8;
                // Check boundary wrapping
                if (Math.abs(toFile - fromFile) <= 2) {
                    Piece target = pieces.get(to);
                    if (target.isEmpty()) {
                        Move move = new Move(from, to, Move.Flags.QUIET_MOVE_FLAG);
                        if (isMoveValid(move)) {
                            moveList[moveCount++].copyFrom(move);
                        }
                    } else if (target.getColor() != playerToMove) {
                        Move move = new Move(from, to, Move.Flags.CAPTURES_FLAG);
                        if (isMoveValid(move)) {
                            moveList[moveCount++].copyFrom(move);
                        }
                    }
                }
            }
        }

        return moveCount;
    }

    private int generateBishopMoves(int from, Move[] moveList, int moveCount) {
        return generateSlidingMoves(from, new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}}, moveList, moveCount);
    }

    private int generateRookMoves(int from, Move[] moveList, int moveCount) {
        return generateSlidingMoves(from, new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}, moveList, moveCount);
    }

    private int generateQueenMoves(int from, Move[] moveList, int moveCount) {
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        return generateSlidingMoves(from, directions, moveList, moveCount);
    }

    private int generateSlidingMoves(int from, int[][] directions, Move[] moveList, int moveCount) {
        int fromRank = from / 8;
        int fromFile = from % 8;

        for (int[] direction : directions) {
            int rank = fromRank + direction[0];
            int file = fromFile + direction[1];

            while (rank >= 0 && rank < 8 && file >= 0 && file < 8) {
                int to = rank * 8 + file;
                Piece target = pieces.get(to);

                if (target.isEmpty()) {
                    Move move = new Move(from, to, Move.Flags.QUIET_MOVE_FLAG);
                    if (isMoveValid(move)) {
                        moveList[moveCount++].copyFrom(move);
                    }
                } else if (target.getColor() != playerToMove) {
                    Move move = new Move(from, to, Move.Flags.CAPTURES_FLAG);
                    if (isMoveValid(move)) {
                        moveList[moveCount++].copyFrom(move);
                    }
                    break; // Stop sliding after capture
                } else {
                    break; // Stop sliding at own piece
                }

                rank += direction[0];
                file += direction[1];
            }
        }

        return moveCount;
    }

    private int generateKingMoves(int from, Move[] moveList, int moveCount) {
        int[][] kingOffsets = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
        };

        // Regular king moves
        for (int[] offset : kingOffsets) {
            int to = from + offset[0] * 8 + offset[1];
            if (to >= 0 && to < 64) {
                int fromFile = from % 8;
                int toFile = to % 8;
                // Check boundary wrapping
                if (Math.abs(toFile - fromFile) <= 1) {
                    Piece target = pieces.get(to);
                    if (target.isEmpty()) {
                        Move move = new Move(from, to, Move.Flags.QUIET_MOVE_FLAG);
                        if (isMoveValid(move)) {
                            moveList[moveCount++].copyFrom(move);
                        }
                    } else if (target.getColor() != playerToMove) {
                        Move move = new Move(from, to, Move.Flags.CAPTURES_FLAG);
                        if (isMoveValid(move)) {
                            moveList[moveCount++].copyFrom(move);
                        }
                    }
                }
            }
        }

        // Castling moves
        if (playerToMove.isWhite()) {
            // Kingside castling - represent move as king from e1 (4) to rook's original square h1 (7)
            if (from == 4) {
                Move move = new Move(from, 7, Move.Flags.KING_CASTLE_FLAG);
                if (isMoveValid(move)) {
                    moveList[moveCount++].copyFrom(move);
                }
            }
            // Queenside castling - represent move as king from e1 (4) to rook's original square a1 (0)
            if (from == 4) {
                Move move = new Move(from, 0, Move.Flags.QUEEN_CASTLE_FLAG);
                if (isMoveValid(move)) {
                    moveList[moveCount++].copyFrom(move);
                }
            }
        } else {
            // Black kingside castling - king from e8 (60) to rook's original square h8 (63)
            if (from == 60) {
                Move move = new Move(from, 63, Move.Flags.KING_CASTLE_FLAG);
                if (isMoveValid(move)) {
                    moveList[moveCount++].copyFrom(move);
                }
            }
            // Black queenside castling - king from e8 (60) to rook's original square a8 (56)
            if (from == 60) {
                Move move = new Move(from, 56, Move.Flags.QUEEN_CASTLE_FLAG);
                if (isMoveValid(move)) {
                    moveList[moveCount++].copyFrom(move);
                }
            }
        }

        return moveCount;
    }

    public long perft(int depth) {
        Move[] moveList = new Move[256];
        int numMoves;
        long nodes = 0;

        if (depth == 0) {
            return 1L;
        }

        numMoves = generateLegalMoves(moveList);
        for (int i = 0; i < numMoves; i++) {
            makeMove(moveList[i]);
            nodes += perft(depth - 1);
            undoMove(moveList[i]);
        }

        return nodes;
    }

    public long fastPerft(int depth) {
        Move[] moveList = new Move[256];
        long nodes = 0;

        int numMoves = generateLegalMoves(moveList);

        if (depth == 0) {
            return 1L;
        }

        if (depth == 1) {
            return numMoves;
        }

        for (int i = 0; i < numMoves; i++) {
            makeMove(moveList[i]);
            nodes += fastPerft(depth - 1);
            undoMove(moveList[i]);
        }

        return nodes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int index = 64 - (8 * (y + 1)) + x;

                sb.append(pieces.get(index).getPieceChar());
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    public String toFEN() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < 8; y++) {
            int consecutiveSpaces = 0;
            for (int x = 0; x < 8; x++) {
                int index = 64 - (8 * (y + 1)) + x;

                Piece piece = pieces.get(index);

                if (piece.isEmpty()) {
                    consecutiveSpaces++;

                    if (x == 7) {
                        sb.append(consecutiveSpaces);
                    }
                } else {
                    if (consecutiveSpaces > 0) {
                        sb.append(consecutiveSpaces);
                    }
                    consecutiveSpaces = 0;
                    sb.append(piece.getPieceChar());
                }
            }
            if (y != 7) {
                sb.append('/');
            } else {
                sb.append(' ');
            }
        }

        if (playerToMove.isWhite()) {
            sb.append("w ");
        } else {
            sb.append("b ");
        }

        StringBuilder c = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (((castlingRights >> i) & 0b1) == 1) {
                c.append(switch (i) {
                    case 0 -> 'K';
                    case 1 -> 'Q';
                    case 2 -> 'k';
                    case 3 -> 'q';
                    default -> "";
                });
            }
        }

        if (!c.isEmpty()) {
            sb.append(c).append(' ');
        } else {
            sb.append("- ");
        }

        if (enPassantSquare == -1) {
            sb.append("- ");
        } else {
            int file = enPassantSquare % 8;
            int rank = enPassantSquare / 8 + 1;

            sb.append(switch (file) {
                case 0 -> "a" + rank;
                case 1 -> "b" + rank;
                case 2 -> "c" + rank;
                case 3 -> "d" + rank;
                case 4 -> "e" + rank;
                case 5 -> "f" + rank;
                case 6 -> "g" + rank;
                case 7 -> "h" + rank;
                default -> "";
            });

            sb.append(' ');
        }

        sb.append(halfmoveClock).append(' ');
        sb.append(fullmoveNumber);

        return sb.toString();
    }

    public void printAsciiArt() {
        String line = "   +---+---+---+---+---+---+---+---+";
        String files = "     a   b   c   d   e   f   g   h";


        System.out.println(files);
        System.out.println(line);
        for (int y = 0; y < 8; y++) {
            System.out.printf(" %d ", 8 - y);

            for (int x = 0; x < 8; x++) {
                int index = 64 - ((y+1) * 8) + x;

                System.out.printf("| %c ", pieces.get(index).getPieceChar());
            }

            System.out.printf("| %d%n", 8 - y);
            System.out.println(line);
        }
        System.out.println(files);
    }

    public String bitboardString(Piece.Color color, Piece.PieceType piece) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s %s:\n", color.toString().toLowerCase(), piece.toString().toLowerCase()));

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                long bit = 1L << (56 - (8 * y) + x);

                if ((bitBoards[color.ordinal()][piece.ordinal()] & bit) != 0) {
                    sb.append('1');
                } else {
                    sb.append('0');
                }
            }

            if (y != 7) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }
}
