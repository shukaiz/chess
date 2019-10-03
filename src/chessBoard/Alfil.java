package chessBoard;

public class Alfil extends Piece {

    @Override
    public boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture) {

        //Alfil is a fairy chess piece that jumps two squares diagonally.
        return (isDiagonal(fromX, fromY, toX, toY)
                && (Math.abs(toX - fromX) == 2 || Math.abs(toY - fromY) == 2));

    }

    @Override
    public String getPiece() {
        return("Alfil");
    }

}