package com.r4men.java_chess;

public class JavaChess {
    void main(String[] args) {
//        Board board = new Board("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2");
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        board.makeMove("e2e4");

        System.out.println();
        System.out.println(board.toFen());
    }
}
