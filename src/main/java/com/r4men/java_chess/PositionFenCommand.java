package com.r4men.java_chess;

import picocli.CommandLine.*;

@Command(
        name = "fen"
)
public class PositionFenCommand implements Runnable {
    @ParentCommand
    private PositionCommand parent;

    @Parameters(
            index = "0",
            paramLabel = "<position>",
            description = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"
    )
    String fenPosition;

    @Parameters(
            index = "1",
            paramLabel = "<side-to-move>",
            description = "w"
    )
    char sideToMove;

    @Parameters(
            index = "2",
            paramLabel = "<castling-rights>",
            description = "KQkq"
    )
    String castlingRights;

    @Parameters(index = "3", paramLabel = "<en-passant-square>")
    String enPassantSquare;

    @Parameters(index = "4", paramLabel = "<halfmove-clock>")
    int halfmoveClock;

    @Parameters(index = "5", paramLabel = "<fullmove-number>")
    int fullmoveNumber;

    @Override
    public void run() {
        parent.parent.board = new Board(String.format("%s %c %s %s %d %d", fenPosition, sideToMove, castlingRights, enPassantSquare, halfmoveClock, fullmoveNumber));
    }
}
