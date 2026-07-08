package com.r4men.java_chess;

import java.util.Scanner;

public class JavaChess {
    void main(String[] args) {
        Board board = new Board("2b2b1r/3k2pp/1Np5/8/2p5/3PB3/PPP4P/R3K2R b KQ - 1 20");

        board.makeMove("d7c7");
        board.makeMove("b6c4");
        board.makeMove("c8a6");
        board.makeMove("e1a1");

        System.out.println(board.ascii8x8());
        System.out.println(board.toFen());
    }
}
