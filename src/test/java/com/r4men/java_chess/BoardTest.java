package com.r4men.java_chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {
    Board initial = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    Board kiwipete = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
    Board pos3 = new Board("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ");
    Board pos4 = new Board("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");


    @Test
    void enPassant() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        board.makeMove("e2e4");
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1", board.toFen());

        board.makeMove("d7d5");
        assertEquals("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2", board.toFen());

        board.makeMove("e4e5");
        assertEquals("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2", board.toFen());

        board.makeMove("f7f5");
        assertEquals("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3", board.toFen());
    }

    // Initial Position
//    @Test
    void initial() {
        assertEquals(1, initial.perft(0));
        assertEquals(20, initial.perft(1));
        assertEquals(400, initial.perft(2));
        assertEquals(8902, initial.perft(3));
        assertEquals(197281, initial.perft(4));
        assertEquals(4865609, initial.perft(5));
        assertEquals(119060324, initial.perft(6));

        assertEquals(1, initial.fastPerft(0));
        assertEquals(20, initial.fastPerft(1));
        assertEquals(400, initial.fastPerft(2));
        assertEquals(8902, initial.fastPerft(3));
        assertEquals(197281, initial.fastPerft(4));
        assertEquals(4865609, initial.fastPerft(5));
        assertEquals(119060324, initial.fastPerft(6));
    }

    // Position 2 (Kiwipete)
//    @Test
    void kiwipete() {
        assertEquals(48, kiwipete.perft(1));
        assertEquals(2039, kiwipete.perft(2));
        assertEquals(97862, kiwipete.perft(3));
        assertEquals(4085603, kiwipete.perft(4));
        assertEquals(193690690, kiwipete.perft(5));

        assertEquals(48, kiwipete.fastPerft(1));
        assertEquals(2039, kiwipete.fastPerft(2));
        assertEquals(97862, kiwipete.fastPerft(3));
        assertEquals(4085603, kiwipete.fastPerft(4));
        assertEquals(193690690, kiwipete.fastPerft(5));
    }

    //    @Test
    void pos3() {
        assertEquals(14, pos3.perft(1));
        assertEquals(191, pos3.perft(2));
        assertEquals(2812, pos3.perft(3));
        assertEquals(43238, pos3.perft(4));
        assertEquals(674624, pos3.perft(5));
        assertEquals(11030083, pos3.perft(6));
        assertEquals(178633661, pos3.perft(7));
    }

    //    @Test
    void pos4() {
        assertEquals(6, pos4.perft(1));
        assertEquals(264, pos4.perft(2));
        assertEquals(9467, pos4.perft(3));
        assertEquals(422333, pos4.perft(4));
        assertEquals(15833292, pos4.perft(5));
        assertEquals(706045033, pos4.perft(6));
    }
}