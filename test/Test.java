import chessBoard.*;
import chessGame.Game;
import chessGame.Player;
import hsa_ufa.Console;

import static org.junit.Assert.*;

public class Test {

    //Length and height must between 8 to 26.
    private static int length = 8;
    private static int height = 8;
    private Piece[][] board = new Piece[length][height];

    @org.junit.jupiter.api.Test
    public void boardSize() {

        Game.initBoard(board);

        assertEquals(8, board.length);
        assertEquals(8, board[0].length);

    }

    @org.junit.jupiter.api.Test
    public void boardSetup() {

        Game.initBoard(board);

        //Test if pieces are set up correctly.
        assertEquals("Pawn", board[0][1].getPiece());
        assertEquals("Rook", board[0][0].getPiece());
        assertEquals("Knight", board[1][0].getPiece());
        assertEquals("Bishop", board[2][0].getPiece());
        assertEquals("Queen", board[3][0].getPiece());
        assertEquals("King", board[4][0].getPiece());

        //Test if pieces' colors are set up correctly.
        assertEquals(false, board[0][0].isBlack);
        assertEquals(true, board[0][7].isBlack);

        printBoard(board);

    }

    @org.junit.jupiter.api.Test
    public void fairyPieces() {

        Player bottom = new Player(false, board);

        board[1][1] = new Ferz();
        board[1][1].isBlack = false;
        board[5][5] = new Alfil();
        board[5][5].isBlack = false;

        //Test if fairy pieces are set up correctly.
        assertEquals("Ferz", board[1][1].getPiece());
        assertEquals("Alfil", board[5][5].getPiece());

        //Test Ferz functions properly.
        bottom.move(board, 1,1,1,2);
        assertNull(board[1][2]);
        bottom.move(board, 1,1,2,2);
        assertNotNull(board[2][2]);

        //Test Alfil functions properly.
        bottom.move(board, 5,5,6,6);
        assertNull(board[6][6]);
        bottom.move(board, 5,5,7,7);
        assertNotNull(board[7][7]);

    }

    @org.junit.jupiter.api.Test
    public void boardNull() {

        Game.initBoard(board);

        assertNull(board[0][2]);
        assertNull(board[1][3]);
        assertNull(board[7][5]);

    }

    @org.junit.jupiter.api.Test
    public void movePiece() {

        Game.initBoard(board);
        Player bottom = new Player(false, board);

        assertNotNull(board[3][1]);
        assertNull(board[3][3]);
        bottom.move(board, 3, 1, 3, 3);
        assertNull(board[3][1]);
        assertNotNull(board[3][3]);

    }

    @org.junit.jupiter.api.Test
    public void validMoves() {

        Game.initBoard(board);
        Player bottom = new Player(false, board);
        Player top = new Player(true, board);

        //Test pawn cannot capture piece vertically in front.
        bottom.move(board, 6, 1, 6, 3);
        bottom.move(board, 6, 3, 6, 4);
        bottom.move(board, 6, 4, 6, 5);
        bottom.move(board, 6, 5, 6, 6);
        assertEquals(true, board[6][6].isBlack);

        //Test opponent piece could not be moved.
        bottom.move(board, 0, 6, 0, 5);
        assertNull(board[0][5]);

        //Test piece could not be moved outside of bound.
        bottom.move(board, 0, 0, -1,0);
        assertNotNull(board[0][0]);

        //Test own piece could not be captured.
        bottom.move(board, 0, 0, 0,1);
        assertEquals("Pawn", board[0][1].getPiece());

        //Test if move is blocked.
        bottom.move(board, 0, 0, 0, 2);
        assertNotNull(board[0][0]);

        //Test first move of pawn.
        bottom.move(board, 0,1,0,3);
        assertNull(board[0][1]);

        //Test second move of pawn.
        bottom.move(board, 0,3,0,5);
        bottom.move(board, 0,3,0,4);
        assertNull(board[0][5]);

        //Test rook move without pawn blocking.
        bottom.move(board, 0,0,0,1);
        assertNotNull(board[0][1]);

        //Test if pawn can move diagonally when capturing.
        bottom.move(board, 0,4,0,5);
        bottom.move(board, 0,5,1,6);
        assertEquals( false, board[1][6].isBlack);

        //Test if King coordinate updates.
        bottom.move(board, 4,1,4,2);
        bottom.move(board, 4,0,4,1);
        top.move(board, 4,6,4,5);
        top.move(board, 4,7,4,6);
        assertEquals(1, Player.whiteKingY);
        assertEquals(6, Player.blackKingY);

        //Print game log.
        System.out.println(bottom.getMoves().get(bottom.getMoves().size() - 1));
        System.out.println(top.getMoves().get(top.getMoves().size() - 1));

    }

    @org.junit.jupiter.api.Test
    public void gameLog() {

        Player bottom = new Player(false, board);
        bottom.writeMoves("Test writing game log.");

        assertEquals("Test writing game log.", bottom.getMoves().get(0));

    }

    @org.junit.jupiter.api.Test
    public void checkMate() {

        Game.initBoard(board);
        Player bottom = new Player(false, board);

        bottom.move(board, 3, 1, 3, 3);
        bottom.move(board, 3, 0, 3, 2);
        bottom.move(board, 3, 2, 4, 3);
        bottom.move(board, 4, 3, 4, 4);
        bottom.move(board, 4, 4, 4, 6);

        //There should 6 output messages while only 5 moves because last one indicates checkmate.
        assertEquals(6, bottom.getMoves().size());

        //Test a special case of check mate.
        board = new Piece[length][height];

        Player top = new Player(true, board);
        board[5][4] = new King();
        board[5][4].isBlack = true;
        board[7][4] = new King();
        board[7][4].isBlack = false;
        board[5][0] = new Rook();
        board[5][0].isBlack = true;
        top.move(board, 5, 0, 7, 0);

        //There should be 2 outputs while only 1 move.
        assertEquals(2, top.getMoves().size());

    }

    @org.junit.jupiter.api.Test
    public void boardGUI() {

        Game.initBoard(board);
        Console c = new Console(length * 80, height * 80, 21, "Chess");

        Game.render(board, c, -1, -1);

    }

    private static void printBoard(Piece[][] board) {
        //This functions print the board in a table format for testing/display purposes.

        //Leave the top left cell empty.
        System.out.printf( "%15s", "");

        //Print header in letters from a to h (if length is 8).
        for (int i = 0; i < board.length; i++) {
            System.out.printf( "%15s", (char) (97 + i));
        }


        System.out.println();

        //Board origin is at bottom left so print has to start reversely.
        for (int j = board[0].length - 1; j >= 0; j--) {

            //Print the row number before printing pieces on each row.
            System.out.printf( "%15s", j + 1);

            for (int i = 0; i < board.length; i++) {

                //Check if a position is empty first to prevent NullPointerException.
                if (board[i][j] == null) {
                    System.out.printf( "%15s", "");
                } else if (board[i][j].isBlack){
                    System.out.printf( "%15s", board[i][j].getPiece() + "(Black)");
                } else {
                    System.out.printf( "%15s", board[i][j].getPiece() + "(White)");
                }

            }

            //Switch line after finish printing each row.
            System.out.println();

        }

    }

}