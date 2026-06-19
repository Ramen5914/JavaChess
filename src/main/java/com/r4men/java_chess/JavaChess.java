package com.r4men.java_chess;

import picocli.CommandLine;
import picocli.CommandLine.*;

@Command
public class JavaChess implements Runnable {
    static final String engineName = "JavaChess";
    static final String engineVersion = "0.0.1";

    Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    @Command
    public void uciOK() {
        System.out.println("Hello!");
    }

    @Option(names = "--uci")
    String test;

//    @Option(names = "--interactive")
//    String value;

    public void run() {
        System.out.printf("%s %s by Ramen5914 (Omar Rahman)%n", engineName, engineVersion);
        while (true) {
            String input = System.console().readLine();

            switch (input) {
                case "quit": {
                    System.exit(0);
                    break;
                }
                case "uci": {
                    System.out.printf("id name %s %s%n", engineName, engineVersion);
                    System.out.println("id author Omar Rahman");
                    System.out.println();

                    System.out.println(test);
                    System.out.println(test);
                    System.out.println(test);
                    System.out.println("uciok");
                    break;
                }
                case "isready": {
                    System.out.println("readyok");
                    break;
                }
                case "d": {
                    System.out.println();
                    board.printAsciiArt();
                    System.out.println();
                    System.out.printf("Fen: %s%n", board.toFEN());
                    break;
                }
                case "help": {
                    System.out.println();
                    System.out.printf("%s is a very basic engine (if you can even call it that) that was made to manage chess games for my minecraft mod.%n", engineName);
                    System.out.println();
                    break;
                }
                default: {
                    System.out.printf("Unknown command: '%s'. Type help for more information.%n", input);
                    break;
                }
            }
        }
    }

    static void main(String[] args) {
        new CommandLine(new JavaChess()).execute(args);
    }
}
