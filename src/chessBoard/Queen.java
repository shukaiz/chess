package chessBoard;

public class Queen extends Piece {

    @Override
    public boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture) {

        /*Queen combines the power of a rook and bishop and can move
        any number of squares along a rank, file, or diagonal. */
        return (isStraight(fromX, fromY, toX, toY) ||
                isDiagonal(fromX, fromY, toX, toY));

    }

    @Override
    public String getPiece() {
        return("Queen");
    }

}
