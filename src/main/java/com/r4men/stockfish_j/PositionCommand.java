package com.r4men.stockfish_j;

import picocli.CommandLine.*;

@Command(
        name = "position",
        description = "Set the position of the chess board",
        subcommands = { PositionFenCommand.class }
)
public class PositionCommand implements Runnable {
    @ParentCommand
    public JavaChess parent;

    @Command(name = "startpos")
    public void startPosition() {

    }

    @Override
    public void run() {

    }
}
