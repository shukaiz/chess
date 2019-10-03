package chessBoard;

public abstract class Piece {

    /*
    Black pieces on top, White pieces on bottom. (Pawn can only move forward)
    Pawn can advance two squares on first move.
     */
    public boolean isBlack;
    public boolean firstMove = true;

    public abstract boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture);

    public abstract String getPiece();

    public static boolean isStraight(int fromX, int fromY, int toX, int toY) {

        //Only allows either moving horizontally or vertically.
        return ((fromX == toX && fromY != toY) ||
                (fromY == toY && fromX != toX));

    }

    public static boolean isDiagonal(int fromX, int fromY, int toX, int toY) {

        //Only allows move diagonally.
        return (Math.abs(toX - fromX) == Math.abs(toY - fromY));

    }

}
