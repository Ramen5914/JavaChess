package com.r4men.java_chess;

public class JavaChess {
    void main(String[] args) {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        board.makeMove("e2e4");
        board.makeMove("f7f5");
        board.makeMove("e4e5");
        board.makeMove("d7d5");

        System.out.println(board);
    }
}
