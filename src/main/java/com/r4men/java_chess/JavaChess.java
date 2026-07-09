package com.r4men.java_chess;

public class JavaChess {
    void main(String[] args) {
        Board board = new Board("2b2b1r/3k2pp/1Np5/8/2p5/3PB3/PPP4P/R3K2R b KQ - 1 20");

        board.makeMove("d7c7");
        board.makeMove("b6c4");
        board.makeMove("c8a6");
        board.makeMove("e1a1");
        board.makeMove("a6c4");
        board.makeMove("e3f4");
        board.makeMove("c7b6");
        board.makeMove("d3c4");
        board.makeMove("f8c5");
        board.makeMove("h1f1");
        board.makeMove("h8f8");
        board.makeMove("c1b1");
        board.makeMove("g7g5");
        board.makeMove("f4g5");
        board.makeMove("f8e8");
        board.makeMove("d1e1");
        board.makeMove("e8g8");
        board.makeMove("g5f4");
        board.makeMove("g8g2");
        board.makeMove("c2c3");
        board.makeMove("b6a5");
        board.makeMove("e1d1");
        board.makeMove("g2e2");
        board.makeMove("d1d2");
        board.makeMove("e2e1");
        board.makeMove("f1e1");
        board.makeMove("h7h6");
        board.makeMove("d2d7");
        board.makeMove("h6h5");
        board.makeMove("e1e6");
        board.makeMove("a5b6");
        board.makeMove("b2b4");

//        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
//
//        board.makeMove("e2e4");

        System.out.println(board.ascii8x8());
        System.out.println(board.toFen());
    }
}
