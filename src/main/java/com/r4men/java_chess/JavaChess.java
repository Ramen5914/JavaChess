package com.r4men.java_chess;

public class JavaChess {
    void main(String[] args) {
        Board board = new Board("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");

        board.makeMove("e5d6");
        board.makeMove("f5f4");
        board.makeMove("g2g4");
        board.makeMove("f4g3");
        board.makeMove("d1f3");

        System.out.println(board);
    }
}
