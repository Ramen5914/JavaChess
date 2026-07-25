package com.r4men.java_chess;

import com.r4men.java_chess.type.Move;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorTest {
    @Test
    void generateKingMoves() {
        Board board = new Board("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2");

        List<Move> moveList = new ArrayList<>(256);
        int count = MoveGenerator.generateKingMoves(moveList, board, 0x19, board.getPieceAt10x12(0x19));

        System.out.println(moveList);

        assertEquals(1, count);

        board = new Board("rnbq1bnr/ppppkppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR w - - 2 3");
        moveList = new ArrayList<>(256);
        count = MoveGenerator.generateKingMoves(moveList, board, 0x19+10, board.getPieceAt10x12(0x19+10));

        System.out.println(moveList);

        assertEquals(4, count);

        board = new Board("rnb1kbnr/p6p/1pppq1p1/4pp1Q/2B1PB2/2NP1P1N/PPP3PP/R3K2R b KQkq - 0 10");
        moveList = new ArrayList<>(256);
        count = MoveGenerator.generateKingMoves(moveList, board, 0x19, board.getPieceAt10x12(0x19));

        System.out.println(moveList);

        assertEquals(7, count);
    }
}