package chessBoard;

public class Bishop extends Piece {

    @Override
    public boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture) {

        //Bishop only moves diagonally.
        return isDiagonal(fromX, fromY, toX, toY);

    }

    @Override
    public String getPiece() {
        return("Bishop");
    }

}
