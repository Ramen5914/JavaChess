package com.r4men.java_chess;

import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(
        name = "JavaChess",
        subcommands = { PositionCommand.class }
)
public class JavaChess implements Runnable {
    static boolean firstStart = true;

    static final String engineName = "JavaChess";
    static final String engineVersion = "0.0.1";

    public Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    @Command()
    public void quit() {
        System.exit(0);
    }

    @Command()
    public void uci() {
        System.out.printf("id name %s %s%n", engineName, engineVersion);
        System.out.println("id author Omar Rahman");
        System.out.println();

        System.out.println("uciok");
    }

    @Command(name = "ucinewgame")
    public void uciNewGame() {

    }

    @Command(name = "isready")
    public void isReady() {
        System.out.println("readyok");
    }

    @Command()
    public void help() {
        System.out.println();
        System.out.printf("%s is a very basic engine (if you can even call it that) that was made to manage chess games for my minecraft mod.%n", engineName);
        System.out.println();
    }

    @Command()
    public void d() {
        System.out.println();
        board.printAsciiArt();
        System.out.println();
        System.out.printf("Fen: %s%n", board.toFEN());
    }

    public void run() {
        while (true) {
            String input = System.console().readLine();

            main(input.split(" "));
        }
    }

    void main(String[] args) {
        if (firstStart) {
            System.out.printf("%s %s by Ramen5914 (Omar Rahman)%n", engineName, engineVersion);
            firstStart = false;
        }

        new CommandLine(new JavaChess()).execute(args);
    }
}
