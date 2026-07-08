package com.r4men.java_chess;

public class JavaChess {
    void main(String[] args) {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        board.makeMove("e2e4");
        board.makeMove("f7f5");
        board.makeMove("e4e5");
        board.makeMove("d7d5");
        board.makeMove("e5d6");
        board.makeMove("f5f4");
        board.makeMove("g2g4");
        board.makeMove("f4g3");
        board.makeMove("d1f3");
        board.makeMove("g3f2");
        board.makeMove("d2d4");
        board.makeMove("f3f2");
        board.makeMove("d8d6");
        board.makeMove("g1f3");

        System.out.println(board.ascii8x8());
    }
}
