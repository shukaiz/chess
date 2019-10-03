package chessBoard;

public class Pawn extends Piece {

    @Override
    public boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture) {

        if (validDirection(fromX, fromY, toX, toY)) {

            if ((firstMove) && (fromX == toX) && (Math.abs(toY - fromY) <= 2)) {

                //Pawn can move vertically forward for one or two squares on first move.
                if (Math.abs(toY - fromY) == 2) {
                    firstMove = false;
                }
                return true;

            } else if ((!capture) && (!firstMove) && (fromX == toX) && (Math.abs(toY - fromY) == 1)) {

                //Pawn can only move vertically one square forward after first move (while not capturing).
                return true;

            } else if ((capture) && (Math.abs(toX - fromX) == 1) && (Math.abs(toY - fromY) == 1)) {

                //Pawn can diagonally move for one square if capturing another piece.
                return true;

            }

        }

        return false;

    }

    private boolean validDirection(int fromX, int fromY, int toX, int toY) {
        /*
        Black pieces on top, White pieces on bottom;
        Coordinate follows chess rule (row 1 on bottom).
         */
        return (isBlack) ? (toY < fromY) : (toY > fromY);
    }

    @Override
    public String getPiece() {
        return("Pawn");
    }

}
