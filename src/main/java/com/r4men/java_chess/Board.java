package com.r4men.java_chess;

import com.r4men.java_chess.type.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Board {
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

    List<Piece> board10x12;
    long[][] bitBoards;
    long allOcc;

    int enPassantSquare10x12;
    Piece.Color playerToMove;
    int halfmoveClock;
    int fullmoveNumber;
    int castlingRights;

    public Board() {
        board10x12 = new ArrayList<>(120);
        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 10; x++) {
                if (x == 0 || x == 9 || y < 2 || y > 9) {
                    board10x12.add(Piece.OFF_BOARD);
                } else {
                    board10x12.add(Piece.EMPTY);
                }
            }
        }

        bitBoards = new long[2][7];
        allOcc = 0L;

        enPassantSquare10x12 = -1;
        playerToMove = Piece.Color.WHITE;
        halfmoveClock = 0;
        fullmoveNumber = 1;

        //                 qkQK
        castlingRights = 0b0000;
    }

    public Board(String fen) {
        this();

        boolean isValid = fen.matches("^\\s*((?:[rnbqkpRNBQKP1-8]{1,8}/){7}[rnbqkpRNBQKP1-8]{1,8})\\s[wWbB]\\s(-|[KQkq]{1,4})\\s(-|[a-h][36])\\s(\\d+)\\s(\\d+)\\s*");

        if (isValid) {
            String[] fenParts = fen.split(" ");
            String[] fenPositionRows = fenParts[0].split("/");
            String playerToMove = fenParts[1];
            String castlingRights = fenParts[2];
            String enPassantSquare = fenParts[3];
            halfmoveClock = Integer.parseInt(fenParts[4]);
            fullmoveNumber = Integer.parseInt(fenParts[5]);

            StringBuilder full = new StringBuilder();
            for (String fenRow : fenPositionRows) {
                StringBuilder row = new StringBuilder();
                for (char c : fenRow.toCharArray()) {
                    if (Character.isDigit(c)) {
                        int emptySquares = Character.getNumericValue(c);
                        row.repeat(' ', emptySquares);
                    } else {
                        row.append(c);
                    }
                }
                if (row.length() != 8) {
                    throw new IllegalArgumentException("Invalid fen string: " + fenRow);
                }

                full.append(row);
            }

            int i = 0;
            for (int y = 7; y >= 0; y--) {
                for (int x = 0; x < 8; x++) {
                    char c = full.charAt(i);

                    setSquare10x12(21 + (10 * y) + x, Piece.fromChar(c));

                    i++;
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
                this.enPassantSquare10x12 = -1;
            } else {
                this.enPassantSquare10x12 = Util.convertSquareTo10x12(enPassantSquare);
            }

            boolean hasWhiteKing = Util.hasOnlyOnePiece(bitBoards[Piece.Color.WHITE.ordinal()][Piece.PieceType.KING.ordinal()]);
            boolean hasBlackKing = Util.hasOnlyOnePiece(bitBoards[Piece.Color.BLACK.ordinal()][Piece.PieceType.KING.ordinal()]);

            if (!(hasWhiteKing && hasBlackKing)) {
                throw new IllegalArgumentException("Invalid FEN string: " + fen);
            }
        } else {
            throw new IllegalArgumentException("Invalid FEN string: " + fen);
        }
    }

    public void makeMove(String move) {
        if (move.matches("^([a-hA-H][1-8])([a-hA-H][1-8])([qrbn])?$")) {
            Move m = createMoveFromString(move);
            makeMove(m);
        } else {
            throw new IllegalArgumentException("Invalid move string: " + move);
        }
    }

    private Move createMoveFromString(String move) {
        Triple<Integer, Integer, Piece.PieceType> parsed = Util.convertUciTo8x8Move(move);

        int from8x8 = parsed.getFirst();
        int to8x8 = parsed.getSecond();

        if (from8x8 == to8x8) {
            return null;
        } else {
            int from10x12 = Util.convert8x8to10x12(from8x8);
            int to10x12 = Util.convert8x8to10x12(to8x8);
            Piece.PieceType promotionPiece = parsed.getThird();

            Piece fromPiece = board10x12.get(from10x12);
            Piece toPiece = board10x12.get(to10x12);

            Move.Flag flag = Move.Flag.QUIET_MOVE_FLAG;
            Piece capturedPiece = Piece.EMPTY;

            if (fromPiece == Piece.EMPTY) {
                return null;
            } else if (fromPiece.getColor() == toPiece.getColor()) {
                if (fromPiece.isKing() && toPiece.isRook()) {
                    if (from8x8 - to8x8 == 4) {
                        flag = Move.Flag.QUEEN_CASTLE_FLAG;
                    } else if (from8x8 - to8x8 == -3) {
                        flag = Move.Flag.KING_CASTLE_FLAG;
                    } else {
                        return null;
                    }
                }
            } else {
                if (fromPiece.isPawn()) {
                    if (Math.abs(to8x8 - from8x8) == Direction.D8X8.N.toInt() * 2) {
                        flag = Move.Flag.DOUBLE_PAWN_PUSH_FLAG;
                    } else if (to10x12 == enPassantSquare10x12) {
                        capturedPiece = board10x12.get(to10x12);
                        flag = Move.Flag.EN_PASSANT_CAPTURE_FLAG;
                    } else if (fromPiece.getColor().opposite() == toPiece.getColor()) {
                        capturedPiece = board10x12.get(to10x12);

                        if (promotionPiece != null) {
                            flag = switch (promotionPiece) {
                                case KNIGHT -> Move.Flag.KNIGHT_PROMOTION_CAPTURE_FLAG;
                                case BISHOP -> Move.Flag.BISHOP_PROMOTION_CAPTURE_FLAG;
                                case ROOK -> Move.Flag.ROOK_PROMOTION_CAPTURE_FLAG;
                                case QUEEN -> Move.Flag.QUEEN_PROMOTION_CAPTURE_FLAG;
                                default -> throw new IllegalArgumentException("Invalid promotion piece: " + promotionPiece);
                            };
                        } else {
                            flag = Move.Flag.CAPTURES_FLAG;
                        }
                    } else if (promotionPiece != null) {
                        flag = switch (promotionPiece) {
                            case KNIGHT -> Move.Flag.KNIGHT_PROMOTION_FLAG;
                            case BISHOP -> Move.Flag.BISHOP_PROMOTION_FLAG;
                            case ROOK -> Move.Flag.ROOK_PROMOTION_FLAG;
                            case QUEEN -> Move.Flag.QUEEN_PROMOTION_FLAG;
                            default -> throw new IllegalArgumentException("Invalid promotion piece: " + promotionPiece);
                        };
                    }
                } else {
                    if (fromPiece.getColor().opposite() == toPiece.getColor()) {
                        flag = Move.Flag.CAPTURES_FLAG;
                    }
                }
            }

            return new Move(from8x8, to8x8, flag, enPassantSquare10x12, castlingRights, halfmoveClock, capturedPiece);
        }
    }

    public void makeMove(@Nullable Move move) {
        makeMove(move, false);
    }

    public void makeMove(@Nullable Move move, boolean ignoreSelfCheck) {
        if (move != null) {
            int from8x8 = move.getFrom8x8();
            int to8x8 = move.getTo8x8();

            int from10x12 = move.getFrom10x12();
            int to10x12 = move.getTo10x12();

            Piece piece = board10x12.get(from10x12);

            if (isMoveValid(move, ignoreSelfCheck)) {
                if (piece.isPawn() || move.isCapture()) {
                    halfmoveClock = 0;
                } else {
                    halfmoveClock++;
                }
                flipPlayerToMove();

                emptySquare10x12(from10x12);
                emptySquare10x12(to10x12);

                if (move.isQuiet() || move.isDoublePawnPush()) {
                    setSquare10x12(to10x12, piece);

                    if (move.isDoublePawnPush()) {
                        markEnPassantSquare(move);
                    }
                } else if (move.isCapture()) {
                    if (move.isPromotion()) {
                        Piece.Color color = piece.getColor();

                        // TODO finish promotion logic
                        if (move.isQueenPromotion()) {
                            if (color.isWhite()) {
//                                setSquare10x12(to, Piece.WHITE_QUEEN);
                            } else {
//                                setSquare10x12(to, Piece.BLACK_QUEEN);
                            }
                        } else if (move.isRookPromotion()) {
                            if (color.isWhite()) {
//                                setSquare10x12(to, Piece.WHITE_ROOK);
                            } else {
//                                setSquare10x12(to, Piece.BLACK_ROOK);
                            }
                        }
                    } else {
                        setSquare10x12(to10x12, piece);

                        if (move.isEnPassantCapture()) {
                            removeEnPassantPawn(move);
                        }
                    }
                } else if (move.isKingCastle()) {
                    // For kingside castling, the move is encoded with 'to' == rook's original square
                    // King moves to the appropriate square and rook is moved from its original square
                    if (playerToMove.isWhite()) {
                        // King from e1 to g1
                        setSquare10x12(6, piece);
//                        emptySquare10x12(from);
                        // Rook moves to f1 (which is 'to')
                        setSquare10x12(5, Piece.WHITE_ROOK);
                        emptySquare10x12(7);
                    } else {
                        // King from e8 to g8
                        setSquare10x12(62, piece);
//                        emptySquare10x12(from);
                        // Rook moves to f8 (which is 'to')
                        setSquare10x12(61, Piece.BLACK_ROOK);
                        emptySquare10x12(63);
                    }
                } else if (move.isQueenCastle()) {
                    // For queenside castling, the move is encoded with 'to' == rook's original square
                    // King moves to the appropriate square and rook is moved from its original square
                    if (playerToMove.isWhite()) {
                        // King from e1 to c1
                        setSquare10x12(2, piece);
//                        emptySquare10x12(from);
                        // Rook moves to d1 (which is 'to')
                        setSquare10x12(3, Piece.WHITE_ROOK);
                        emptySquare10x12(0);
                    } else {
                        // King from e8 to c8
                        setSquare10x12(58, piece);
//                        emptySquare10x12(from);
                        // Rook moves to d8 (which is 'to')
                        setSquare10x12(59, Piece.BLACK_ROOK);
                        emptySquare10x12(56);
                    }
                }

                if (!move.isDoublePawnPush()) {
                    enPassantSquare10x12 = -1;
                }
            }
        }
    }

    // TODO make real javadoc
    /**
     * blah blah blah
     * <p>
     * Before this function is called, the player to move has already been swapped, because the move has already been made
     *
     * @param move the move that was just made
     */
    private void markEnPassantSquare(@NotNull Move move) {
        int to = move.getTo10x12();

        int leftIndex = move.getTo10x12() + Direction.D10X12.W.toInt();
        Piece left = board10x12.get(leftIndex);

        int rightIndex = move.getTo10x12() + Direction.D10X12.E.toInt();
        Piece right = board10x12.get(rightIndex);

        enPassantSquare10x12 = switch (playerToMove) {
            case WHITE -> {
                // It's WHITE's turn, so BLACK just moved SOUTH. En passant square is one rank NORTH of destination.
                yield to + Direction.D10X12.N.toInt();
            }
            case BLACK -> {
                // It's BLACK's turn, so WHITE just moved NORTH. En passant square is one rank SOUTH of destination.
                yield to + Direction.D10X12.S.toInt();
            }
            default -> throw new IllegalStateException("PlayerToMove is neither WHITE nor BLACK: " + playerToMove);
        };
    }

    public void undoMove(Move move) {
        int from10x12 = move.getFrom10x12();
        int to10x12 = move.getTo10x12();
        Piece piece = board10x12.get(to10x12);

        // p means previous
        Piece.Color pPlayerToMove = playerToMove.opposite();
        Piece pPiece = move.capturedPiece();

        if (move.isCapture()) {
            setSquare10x12(to10x12, pPiece);
            setSquare10x12(from10x12, piece);

            if (move.isEnPassantCapture()) {
                switch (pPlayerToMove) {
                    case WHITE -> setSquare10x12(to10x12 - 10, Piece.BLACK_PAWN);
                    case BLACK -> setSquare10x12(to10x12 + 10, Piece.WHITE_PAWN);
                    default -> throw new IllegalStateException("Previous playerToMove is neither WHITE nor BLACK: " + pPlayerToMove);
                }
            }
        } else if (move.isCastle()) {
            switch (pPlayerToMove) {
                case WHITE -> {
                    setSquare10x12(0x19, Piece.WHITE_KING);

                    if (move.isKingCastle()) {
                        emptySquare10x12(0x1A);
                        emptySquare10x12(0x1B);
                        setSquare10x12(0x1C, Piece.WHITE_ROOK);
                    } else {
                        setSquare10x12(0x15, Piece.WHITE_ROOK);
                        emptySquare10x12(0x16);
                        emptySquare10x12(0x17);
                        emptySquare10x12(0x18);
                    }
                }
                case BLACK -> {
                    setSquare10x12(0x5F, Piece.BLACK_KING);

                    if (move.isKingCastle()) {
                        emptySquare10x12(0x60);
                        emptySquare10x12(0x61);
                        setSquare10x12(0x62, Piece.BLACK_ROOK);
                    } else {
                        setSquare10x12(0x5B, Piece.BLACK_ROOK);
                        emptySquare10x12(0x5C);
                        emptySquare10x12(0x5D);
                        emptySquare10x12(0x5E);
                    }
                }
                default -> throw new IllegalStateException("Previous playerToMove is neither WHITE nor BLACK: " + pPlayerToMove);
            }
        } else {
            emptySquare10x12(to10x12);
            setSquare10x12(from10x12, piece);
        }

        if (move.isPromotion()) {
            setSquare10x12(from10x12, switch (pPlayerToMove) {
                case WHITE -> Piece.WHITE_PAWN;
                case BLACK -> Piece.BLACK_PAWN;
                default -> throw new IllegalStateException("Previous playerToMove is neither WHITE nor BLACK: " + pPlayerToMove);
            });
        }

        playerToMove = pPlayerToMove;
        halfmoveClock = move.halfmoveClock();
        castlingRights = move.castlingRights();
        enPassantSquare10x12 = move.enPassantSquare10x12();;
        if (playerToMove.isBlack()) {
            fullmoveNumber--;
        }
    }

    private void setSquare10x12(int square10x12, Piece piece) {
        int square8x8 = Util.convert10x12to8x8(square10x12);

        Piece previousPiece = board10x12.get(square10x12);

        long bit8x8 = 1L << square8x8;

        if (piece.isEmpty()) {
            if (!previousPiece.isEmpty()) {
                board10x12.set(square10x12, Piece.EMPTY);

                int color = previousPiece.getColor().ordinal();

                bitBoards[color][previousPiece.getPieceType().ordinal()] &= ~bit8x8;
                bitBoards[color][Piece.PieceType.OCC.ordinal()] &= ~bit8x8;
                allOcc &= ~bit8x8;
            }
        } else {
            board10x12.set(square10x12, piece);

            bitBoards[piece.getColor().ordinal()][piece.getPieceType().ordinal()] |= bit8x8;
            bitBoards[piece.getColor().ordinal()][Piece.PieceType.OCC.ordinal()] |= bit8x8;
            allOcc |= bit8x8;
        }
    }

    private void emptySquare10x12(int square) {
        setSquare10x12(square, Piece.EMPTY);
    }

    private void flipPlayerToMove() {
        playerToMove = playerToMove.opposite();

        if (playerToMove.isWhite()) {
            fullmoveNumber++;
        }
    }

    private void removeEnPassantPawn(Move move) {
        int to10x12 = move.getTo10x12();

        if (playerToMove.isWhite()) {
            int removeSquare = to10x12 + Direction.D10X12.N.toInt();
            emptySquare10x12(removeSquare);
        } else {
            int removeSquare = to10x12 + Direction.D10X12.S.toInt();
            emptySquare10x12(removeSquare);
        }
    }

    private boolean wouldBeInCheckAfterMove(Move move) {
        makeMove(move, true);
        boolean toReturn = isKingInCheck(playerToMove.opposite());
        undoMove(move);

        return toReturn;
    }

    // TODO redo this function
    private boolean isKingInCheck(Piece.Color color) {
        // Find the king
        int kingSquare = -1;
        for (int i = 0; i < 64; i++) {
            Piece p = board10x12.get(i);
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
        return isSquareAttackedBy10x12(kingSquare, opponent);
    }

    // TODO finish logic in this method
    private boolean isSquareAttackedBy10x12(int s10x12, Piece.Color attackingColor) {
        long occBB = bitBoards[attackingColor.ordinal()][Piece.PieceType.OCC.ordinal()];

        for (int s8x8 = 0; s8x8 < 64; s8x8++) {
            if (((occBB >>> s8x8) & 0b1) == 1) {
                int from10x12 = Util.convert8x8to10x12(s8x8);
                Piece piece = board10x12.get(from10x12);

                if (canPieceAttackSquare10x12(from10x12, s10x12, piece)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isSquareUnderAttack10x12(int s10x12, Piece.Color defendingColor) {
        return isSquareAttackedBy10x12(s10x12, defendingColor.opposite());
    }

    private boolean canPieceAttackSquare10x12(int from10x12, int to10x12, Piece piece) {
        if (from10x12 == to10x12) {
            return false;
        }

        int fromRank = from10x12 / 10;
        int fromFile = from10x12 % 10;
        int toRank = to10x12 / 10;
        int toFile = to10x12 % 10;
        int rankDiff = toRank - fromRank;
        int fileDiff = toFile - fromFile;

        Direction.D10X12 directionD = Util.getD10X12FromSquares(from10x12, to10x12);

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
                return Math.abs(rankDiff) == Math.abs(fileDiff) && rankDiff != 0 && isPathClear10x12(from10x12, to10x12, directionD);
            case ROOK:
                // Rook moves horizontally or vertically
                return ((rankDiff == 0 && fileDiff != 0) || (rankDiff != 0 && fileDiff == 0)) && isPathClear10x12(from10x12, to10x12, directionD);
            case QUEEN:
                // Queen moves like rook or bishop
                boolean isRookMove = (rankDiff == 0 && fileDiff != 0) || (rankDiff != 0 && fileDiff == 0);
                boolean isBishopMove = Math.abs(rankDiff) == Math.abs(fileDiff) && rankDiff != 0;
                return (isRookMove || isBishopMove) && isPathClear10x12(from10x12, to10x12, directionD);
            case KING:
                // King moves one square in any direction
                return Math.abs(rankDiff) <= 1 && Math.abs(fileDiff) <= 1 && (rankDiff != 0 || fileDiff != 0);
            default:
                return false;
        }
    }

    private boolean isMoveValid(Move move) {
        return isMoveValid(move, false);
    }

    private boolean isMoveValid(Move move, boolean ignoreSelfCheck) {
        int from8x8 = move.getFrom8x8();
        int to8x8 = move.getTo8x8();

        int from10x12 = move.getFrom10x12();
        int to10x12 = move.getTo10x12();

        Piece movedPiece = board10x12.get(from10x12);
        Piece destinationPiece = board10x12.get(to10x12);

        // From and to squares must be different
        // Moved piece belongs to the player whose turn it is to move
        // Destination square is not off the board
        if (from10x12 == to10x12 || !movedPiece.matchesColor(playerToMove) || destinationPiece.isOffBoard()) {
            return false;
        }

        if (ignoreSelfCheck) {
            return isValidMovementForPiece(movedPiece, move);
        } else {
            return isValidMovementForPiece(movedPiece, move) && !wouldBeInCheckAfterMove(move);
        }
    }

    private boolean isValidMovementForPiece(Piece movedPiece, Move move) {
        int from10x12 = move.getFrom10x12();
        int to10x12 = move.getTo10x12();

        Piece destinationPiece = board10x12.get(to10x12);

        if (move.isCapture() && destinationPiece.isEmpty() && !move.isEnPassantCapture()) {
            return false;
        } else if (!movedPiece.isPawn() && move.isPromotion()) {
            return false;
        } else if (!movedPiece.isKing() && destinationPiece.getColor() == playerToMove) {
            return false;
        }

//        int fromRank = from / 8;
//        int fromFile = from % 8;
//        int toRank = to / 8;
//        int toFile = to % 8;
//        int rankDiff = toRank - fromRank;
//        int fileDiff = toFile - fromFile;

        return switch (movedPiece.getPieceType()) {
            case PAWN -> isValidPawnMove(move, destinationPiece, from10x12, to10x12);
//            case KNIGHT -> isValidKnightMove(move, destinationPiece, from10x12, to10x12);
            case BISHOP -> isValidBishopMove(move, destinationPiece, from10x12, to10x12);
            case ROOK -> isValidRookMove(move, destinationPiece, from10x12, to10x12);
            case QUEEN -> isValidQueenMove(move, destinationPiece, from10x12, to10x12);
            case KING -> isValidKingMove(move, destinationPiece, from10x12, to10x12);
            default -> false;
        };
    }

    private boolean isValidPawnMove(Move move, Piece destinationPiece, int from10x12, int to10x12) {
        Direction.D10X12 direction = playerToMove.isWhite() ? Direction.D10X12.N : Direction.D10X12.S;

        if (move.isDoublePawnPush()) {
            int validStartRank = playerToMove.isWhite() ? 2 : 7;
            int fromRank = from10x12 / 10 - 1;

            if (fromRank == validStartRank && to10x12 == from10x12 + direction.toInt() * 2) {
                return isPathClear10x12(from10x12, to10x12, direction);
            } else {
                return false;
            }
        } else if (move.isEnPassantCapture()) {
            // En passant: pawn must be one rank forward and one file diagonal to the en passant square
            int oneForward = from10x12 + direction.toInt();
            return to10x12 == oneForward + Direction.D10X12.E.toInt() && enPassantSquare10x12 == to10x12
                    || to10x12 == oneForward + Direction.D10X12.W.toInt() && enPassantSquare10x12 == to10x12;
        }

        if (move.isPromotion()) {
            int toRank = to10x12 / 10 - 1;
            int promotionRank = playerToMove.isWhite() ? 8 : 1;

            // Pawn promotion (only on last rank)
            if (toRank != promotionRank) {
                return false;
            }
            // Check if it's a valid diagonal capture or forward move for promotion
            if (move.isCapture()) {
                return to10x12 == from10x12 + direction.toInt() + Direction.D10X12.E.toInt() && !destinationPiece.isEmpty()
                        || to10x12 == from10x12 + direction.toInt() + Direction.D10X12.W.toInt() && !destinationPiece.isEmpty();
            } else {
                return to10x12 == from10x12 + direction.toInt() && destinationPiece.isEmpty();
            }
        }

        if (move.isCapture()) {
            // Regular pawn capture (non-promotion)
            return (to10x12 == from10x12 + direction.toInt() + Direction.D10X12.E.toInt() || to10x12 == from10x12 + direction.toInt() + Direction.D10X12.W.toInt())
                    && !destinationPiece.isEmpty();
        }

        // Regular pawn move (quiet, non-promotion)
        // Pawn can only move forward one square on a quiet move
        return to10x12 == from10x12 + direction.toInt() && destinationPiece.isEmpty();
    }

    private boolean isValidKnightMove(int rankDiff, int fileDiff) {
        int absDiff = Math.abs(rankDiff) + Math.abs(fileDiff);
        if (absDiff != 3) {
            return false;
        }
        return Math.abs(rankDiff) != 0 && Math.abs(fileDiff) != 0;
    }

    private boolean isValidBishopMove(Move move, Piece destinationPiece, int from10x12, int to10x12) {
        Direction.D10X12 direction = Util.getD10X12FromSquares(from10x12, to10x12);

        if (direction == Direction.D10X12.N || direction == Direction.D10X12.E || direction == Direction.D10X12.S || direction == Direction.D10X12.W) {
            return false;
        }

        if (isPathClear10x12(from10x12, to10x12, direction)) {
            if (move.isCapture() && destinationPiece.getColor() == playerToMove.opposite()) {
                return true;
            } else {
                return destinationPiece.isEmpty();
            }
        } else {
            return false;
        }
    }

    private boolean isValidRookMove(int rankDiff, int fileDiff) {
        return (rankDiff == 0 && fileDiff != 0) || (rankDiff != 0 && fileDiff == 0);
    }

    private boolean isValidQueenMove(Move move, Piece destinationPiece, int from10x12, int to10x12) {
        return false;
    }

    private boolean isValidKingMove(Move move, int rankDiff, int fileDiff) {
        int from = move.getFrom8x8();
        int to = move.getTo8x8();

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

    private boolean isPathClear10x12(int from, int to, Direction.D10X12 direction) {
        if (Util.isRayFrom10x12(from, to, direction)) {
            int squareToCheck = from + direction.toInt();
            while (squareToCheck != to) {
                if (!board10x12.get(squareToCheck).isEmpty()) {
                    return false;
                }
                squareToCheck += direction.toInt();
            }

            return true;
        } else {
            throw new IllegalArgumentException("Squares " + from + " and " + to + " are not aligned in direction " + direction);
        }
    }

    private boolean isKingsideCastleLegal() {
        if (playerToMove.isWhite()) {
            if ((castlingRights & 0b0001) == 0) {
                return false; // No kingside castling rights
            }
            // Squares e1, f1, g1 must be unoccupied
            if (!(board10x12.get(4).isKing() && board10x12.get(5).isEmpty() && board10x12.get(6).isEmpty() &&
                   board10x12.get(7).isRook())) {
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
            if (!(board10x12.get(60).isKing() && board10x12.get(61).isEmpty() && board10x12.get(62).isEmpty() &&
                   board10x12.get(63).isRook())) {
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
            if (!(board10x12.get(0).isRook() && board10x12.get(1).isEmpty() && board10x12.get(2).isEmpty() &&
                   board10x12.get(3).isEmpty() && board10x12.get(4).isKing())) {
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
            if (!(board10x12.get(56).isRook() && board10x12.get(57).isEmpty() && board10x12.get(58).isEmpty() &&
                   board10x12.get(59).isEmpty() && board10x12.get(60).isKing())) {
                return false;
            }
            // King must not be in check, and cannot move through check
            // Check e8 (from), d8 (through), c8 (to)
            return !isSquareUnderAttack(60, playerToMove) &&
                   !isSquareUnderAttack(59, playerToMove) &&
                   !isSquareUnderAttack(58, playerToMove);
        }
    }

    public int generateLegalMoves(Move[] moveList) {
        // Initialize all Move objects in the array
        for (int i = 0; i < moveList.length; i++) {
            if (moveList[i] == null) {
//                moveList[i] = new Move(0, 0, 0);
            }
        }

        int moveCount = 0;

        // Iterate through all squares
        for (int from = 0; from < 64; from++) {
            Piece piece = board10x12.get(from);

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
        Piece piece = board10x12.get(from);
        int toRank = from / 8;
        int toFile = from % 8;
        int direction = piece.isWhite() ? 1 : -1;
        int promotionRank = piece.isWhite() ? 7 : 0;

        // Forward move
        int forwardSquare = from + 8 * direction;
        if (forwardSquare >= 0 && forwardSquare < 64 && board10x12.get(forwardSquare).isEmpty()) {
            if (toRank + direction == promotionRank) {
                // Promotion moves
                for (Move.Flag promFlag : new Move.Flag[]{
                    Move.Flag.KNIGHT_PROMOTION_FLAG,
                    Move.Flag.BISHOP_PROMOTION_FLAG,
                    Move.Flag.ROOK_PROMOTION_FLAG,
                    Move.Flag.QUEEN_PROMOTION_FLAG
                }) {
                    Move move = new Move(from, forwardSquare, promFlag, enPassantSquare10x12, castlingRights, halfmoveClock, null);
                    if (isMoveValid(move)) {
//                        moveList[moveCount++].copyFrom(move);
                    }
                }
            } else {
                // Regular forward move
                Move move = new Move(from, forwardSquare, Move.Flag.QUIET_MOVE_FLAG, enPassantSquare10x12, castlingRights, halfmoveClock, null);
                if (isMoveValid(move)) {
//                    moveList[moveCount++].copyFrom(move);
                }
            }

            // Double pawn push
            int startRank = piece.isWhite() ? 1 : 6;
            if (toRank == startRank) {
                int doubleSquare = from + 16 * direction;
                if (board10x12.get(doubleSquare).isEmpty()) {
                    Move move = new Move(from, doubleSquare, Move.Flag.DOUBLE_PAWN_PUSH_FLAG, enPassantSquare10x12, castlingRights, halfmoveClock, null);
                    if (isMoveValid(move)) {
//                        moveList[moveCount++].copyFrom(move);
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
                    Piece target = board10x12.get(captureSquare);

                    if (toRank + direction == promotionRank) {
                        // Promotion captures
                        for (Move.Flag promFlag : new Move.Flag[]{
                            Move.Flag.KNIGHT_PROMOTION_CAPTURE_FLAG,
                            Move.Flag.BISHOP_PROMOTION_CAPTURE_FLAG,
                            Move.Flag.ROOK_PROMOTION_CAPTURE_FLAG,
                            Move.Flag.QUEEN_PROMOTION_CAPTURE_FLAG
                        }) {
                            Move move = new Move(from, captureSquare, promFlag, enPassantSquare10x12, castlingRights, halfmoveClock, target);
                            if (isMoveValid(move)) {
//                                moveList[moveCount++].copyFrom(move);
                            }
                        }
                    } else if (!target.isEmpty() && target.getColor() != playerToMove) {
                        // Regular capture
                        Move move = new Move(from, captureSquare, Move.Flag.CAPTURES_FLAG, enPassantSquare10x12, castlingRights, halfmoveClock, target);
                        if (isMoveValid(move)) {
//                            moveList[moveCount++].copyFrom(move);
                        }
                    } else if (enPassantSquare10x12 == captureSquare) {
                        // En passant
                        Move move = new Move(from, captureSquare, Move.Flag.EN_PASSANT_CAPTURE_FLAG, enPassantSquare10x12, castlingRights, halfmoveClock, target);
                        if (isMoveValid(move)) {
//                            moveList[moveCount++].copyFrom(move);
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
                    Piece target = board10x12.get(to);
                    if (target.isEmpty()) {
//                        Move move = new Move(from, to, Move.Flag.QUIET_MOVE_FLAG);
//                        if (isMoveValid(move)) {
//                            moveList[moveCount++].copyFrom(move);
//                        }
                    } else if (target.getColor() != playerToMove) {
//                        Move move = new Move(from, to, Move.Flag.CAPTURES_FLAG);
//                        if (isMoveValid(move)) {
//                            moveList[moveCount++].copyFrom(move);
//                        }
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
                Piece target = board10x12.get(to);

                if (target.isEmpty()) {
//                    Move move = new Move(from, to, Move.Flag.QUIET_MOVE_FLAG);
//                    if (isMoveValid(move)) {
//                        moveList[moveCount++].copyFrom(move);
//                    }
                } else if (target.getColor() != playerToMove) {
//                    Move move = new Move(from, to, Move.Flag.CAPTURES_FLAG);
//                    if (isMoveValid(move)) {
//                        moveList[moveCount++].copyFrom(move);
//                    }
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
                    Piece target = board10x12.get(to);
                    if (target.isEmpty()) {
//                        Move move = new Move(from, to, Move.Flag.QUIET_MOVE_FLAG);
//                        if (isMoveValid(move)) {
//                            moveList[moveCount++].copyFrom(move);
//                        }
                    } else if (target.getColor() != playerToMove) {
//                        Move move = new Move(from, to, Move.Flag.CAPTURES_FLAG);
//                        if (isMoveValid(move)) {
//                            moveList[moveCount++].copyFrom(move);
//                        }
                    }
                }
            }
        }

        // Castling moves
        if (playerToMove.isWhite()) {
            // Kingside castling - represent move as king from e1 (4) to rook's original square h1 (7)
            if (from == 4) {
//                Move move = new Move(from, 7, Move.Flag.KING_CASTLE_FLAG);
//                if (isMoveValid(move)) {
//                    moveList[moveCount++].copyFrom(move);
//                }
            }
            // Queenside castling - represent move as king from e1 (4) to rook's original square a1 (0)
            if (from == 4) {
//                Move move = new Move(from, 0, Move.Flag.QUEEN_CASTLE_FLAG);
//                if (isMoveValid(move)) {
//                    moveList[moveCount++].copyFrom(move);
//                }
            }
        } else {
            // Black kingside castling - king from e8 (60) to rook's original square h8 (63)
            if (from == 60) {
//                Move move = new Move(from, 63, Move.Flag.KING_CASTLE_FLAG);
//                if (isMoveValid(move)) {
//                    moveList[moveCount++].copyFrom(move);
//                }
            }
            // Black queenside castling - king from e8 (60) to rook's original square a8 (56)
            if (from == 60) {
//                Move move = new Move(from, 56, Move.Flag.QUEEN_CASTLE_FLAG);
//                if (isMoveValid(move)) {
//                    moveList[moveCount++].copyFrom(move);
//                }
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

        sb.append("FEN:\n").append(toFen()).append("\n\n");
        sb.append(get10x12BoardString()).append("\n");
        sb.append(get8x8BoardString()).append("\n");

        for (int color = 0; color < 2; color++) {
            for (int piece = 0; piece < 7; piece++) {
                sb.append(bitboardString(color, piece));

                if (piece != 6) {
                    sb.append("\n\n");
                }
            }

            if (color != 1) {
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }

    public String get10x12BoardString() {
        StringBuilder sb = new StringBuilder();

        sb.append("10x12 Board:\n");
        sb.append("    X   A   B   C   D   E   F   G   H   X\n");
        sb.append("  +---+---+---+---+---+---+---+---+---+---+\n");
        for (int y = 11; y >= 0; y--) {
            if (y > 9 || y < 2) {
                sb.append("X | ");
            } else {
                sb.append(y - 1).append(" | ");
            }

            for (int x = 0; x < 10; x++) {
                sb.append(board10x12.get(y*10 + x)).append(" | ");
            }

            if (y > 9 || y < 2) {
                sb.append("X").append("\n");
            } else {
                sb.append(y - 1).append("\n");
            }

            sb.append("  +---+---+---+---+---+---+---+---+---+---+").append("\n");
        }
        sb.append("    X   A   B   C   D   E   F   G   H   X\n");

        return sb.toString();
    }

    public String get8x8BoardString() {
        StringBuilder sb = new StringBuilder();

        sb.append("8x8 Board:\n");
        sb.append("    A   B   C   D   E   F   G   H\n");
        sb.append("  +---+---+---+---+---+---+---+---+\n");
        for (int y = 7; y >= 0; y--) {
            sb.append(y + 1).append(" | ");

            for (int x = 0; x < 8; x++) {
                sb.append(board10x12.get(10 * (y + 2) + (x + 1))).append(" | ");
            }

            sb.append(y + 1).append("\n");

            sb.append("  +---+---+---+---+---+---+---+---+").append("\n");
        }
        sb.append("    A   B   C   D   E   F   G   H\n");

        return sb.toString();
    }

    public String toFen() {
        StringBuilder sb = new StringBuilder();

        for (int y = 7; y >= 0; y--) {
            int consecutiveSpaces = 0;
            for (int x = 0; x < 8; x++) {
                int index = 10 * (y + 2) + (x + 1);

                Piece piece = board10x12.get(index);

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

            if (y != 0) {
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

        if (enPassantSquare10x12 == -1) {
            sb.append("- ");
        } else {
            int s8x8 = Util.convert10x12to8x8(enPassantSquare10x12);
            int file = s8x8 % 8;
            int rank = s8x8 / 8 + 1;

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

    public String bitboardString(int color, int piece) {
        return bitboardString(Piece.Color.values()[color], Piece.PieceType.values()[piece]);
    }

    public String bitboardString(Piece.Color color, Piece.PieceType piece) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s %s bitboard:\n", color.toString().toLowerCase(), piece.toString().toLowerCase()));

        long bb = bitBoards[color.ordinal()][piece.ordinal()];
        for (int y = 7; y >= 0; y--) {
            long select =  0b11111111L << (y * 8);
            long row = (bb & select) >>> (y * 8);

            String corrected = new StringBuilder(String.format("%8s", Long.toBinaryString(row)).replace(' ', '0')).reverse().toString();

            sb.append(corrected);

            if (y != 0) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
