package chessGame;

import java.util.ArrayList;
import chessBoard.*;

public class Player {

    //Records moves in an array list.
    private ArrayList<String> movesList = new ArrayList<String>();

    //Records coordinate of Kings to detect checkmate.
    private static int blackKingX = 4;
    private static int whiteKingX = 4;
    public static int blackKingY;
    public static int whiteKingY;

    //Saves the color of the player.
    private boolean isBlack;

    //Constructs Player and saves the color and location of opponent king.
    public Player(boolean isBlack, Piece[][] board) {

        this.isBlack = isBlack;

        //Store the location of King.
        whiteKingY = 0;
        blackKingY = board[0].length - 1;

    }

    //Getter for movesList which records moves.
    public ArrayList<String> getMoves() {
        return movesList;
    }

    //Setter for movesList which records moves.
    public void writeMoves(String move) {
        movesList.add(move);
    }

    public boolean move(Piece[][] board, int fromX, int fromY, int toX, int toY) {

        /*
        Cannot move if nothing is at that location,
        moving piece in another color.
         */
        if (board[fromX][fromY] == null || board[fromX][fromY].isBlack != this.isBlack) {

            movesList.add("Move not recognized.");
            return false;

        }

        /*
        Cannot move if initiating from or trying to move to outside of board.
         */
        if (toX >= board.length || fromY >= board[0].length || toY >= board[0].length || toX < 0 || toY < 0) {

            movesList.add("Movement out of bound.");
            return false;
        }

        //Boolean to identify if this move involves capturing another piece.
        boolean capture = true;

        //Set not to capture if moving to an empty space.
        if (board[toX][toY] == null) {

            capture = false;

        } else if (board[toX][toY].isBlack == this.isBlack) {

            //Cannot capture piece with same color
            movesList.add("You cannot capture your own piece");
            return false;

        }

        //Check if movement follows the game rule.
        if (!board[fromX][fromY].validMove(fromX, fromY, toX, toY, capture)) {
            movesList.add("Move not valid.");
            return false;
        }

        //Check if the movement is blocked.
        if (board[fromX][fromY].getPiece().equals("Rook") || board[fromX][fromY].getPiece().equals("Bishop")
                || board[fromX][fromY].getPiece().equals("Queen") || board[fromX][fromY].getPiece().equals("Pawn")) {
            if (isBlocked(board, fromX, fromY, toX, toY)) {
                movesList.add("Movement blocked.");
                return false;
            }
        }

        //Execute and record movement.
        if (capture) {
            movesList.add(board[fromX][fromY].getPiece() + " at " + (char) (97 + fromX)
                    + (fromY + 1) + " captured " + board[toX][toY].getPiece() + " at "
                    + (char) (97 + toX) + (toY + 1));
            Piece origPiece = board[fromX][fromY];
            board[toX][toY] = null;
            board[toX][toY] = origPiece;
            board[fromX][fromY] = null;
        } else {
            movesList.add(board[fromX][fromY].getPiece() + " moved from " + (char) (97 + fromX)
                    + (fromY + 1) + " to " + (char) (97 + toX) + (toY + 1));
            Piece destPiece = board[fromX][fromY];
            board[toX][toY] = destPiece;
            board[fromX][fromY] = null;
        }

        //Update the location of King if it moves.
        if (board[toX][toY].getPiece() == "King") {
            if (isBlack) {
                blackKingX = toX;
                blackKingY = toY;
            } else {
                whiteKingX = toX;
                whiteKingY = toY;
            }
        }

        if (isChecked(board)) {
            movesList.add("Checkmate! " + ((isBlack) ? ("Black") : ("White")) + " wins.");
            //return;
        }

        return true;

    }

    //This function checks if the movement is blocked by another piece.
    private boolean isBlocked(Piece[][] board, int fromX, int fromY, int toX, int toY) {

        //Determine to distance of this move.
        int moves = Math.max(Math.abs(toX - fromX), Math.abs(toY - fromY));

        //Determine the direction of this movement.
        int dx = Integer.compare(toX, fromX);
        int dy = Integer.compare(toY, fromY);

        for (int i = 1; i < moves; i++) {

            if (board[fromX + i * dx][fromY + i * dy] == null) {

                //Continue to iterate if in between is empty.
                continue;

            } else if (board[fromX + i * dx][fromY + i * dy] != board[toX][toY]) {

                //Detects block if there is another piece in between.
                return true;

            }
        }

        return false;

    }

    /*
    Check if a check mate has occurred by going through every positions
    and see if a valid move could be made to opponent King.
     */
    private boolean isChecked(Piece[][] board) {

        //First iterate through the board.
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {


                if (board[i][j] == null) {

                    //Do nothing if it is empty positions.

                } else if (board[i][j].isBlack != this.isBlack) {

                    //Do nothing if pieces belong to opponent.

                } else if (!this.isBlack && board[i][j].validMove(i, j, blackKingX, blackKingY, true)
                        && !isBlocked(board, i, j, blackKingX, blackKingY)) {

                    //Check if any of our own piece can move to opponent King.
                    return true;

                } else if (this.isBlack && board[i][j].validMove(i, j, whiteKingX, whiteKingY, true)
                        && !isBlocked(board, i, j, whiteKingX, whiteKingY)) {

                    //Two ifs are required because locations of two Kings are stored separately.
                    return true;

                }

            }
        }

        return false;

    }

}
