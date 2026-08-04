package com.r4men.java_chess;

import com.r4men.java_chess.type.Move;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MoveValidatorTest {

    @Test
    void isMoveValid() {
        // Regular capture
        Board board = new Board("4k3/4p3/8/3p4/4P3/8/3P4/4K3 w - - 0 1");
        Move move = MoveParser.fromUci(board, "e4d5");
        assert move != null;
        assertTrue(MoveValidator.isMoveValid(board, move));

        // Self capture
        board = new Board("4k3/4p3/3p4/3P4/4P3/8/8/4K3 w - - 0 1");
        board.makeMove("e4d5");
        assertEquals("4k3/4p3/3p4/3P4/4P3/8/8/4K3 w - - 0 1", board.toFen());
    }
}