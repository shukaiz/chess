package chessBoard;

public class Ferz extends Piece {

    @Override
    public boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture) {

        //Ferz is a fairy chess piece that jump one squares diagonally.
        return (isDiagonal(fromX, fromY, toX, toY)
                && (Math.abs(toX - fromX) == 1 || Math.abs(toY - fromY) == 1));

    }

    @Override
    public String getPiece() {
        return("Ferz");
    }

}