package com.r4men.java_chess;

public class JavaChess {
    void main(String[] args) {
        Board board = new Board("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2");

        board.makeMove("f7f5");

        System.out.println(board.ascii8x8());
        System.out.println(board.toFen());
    }
}
