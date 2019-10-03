package chessBoard;

public class Rook extends Piece {

    @Override
    public boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture) {

        //Rook only moves in straight line.
        return isStraight(fromX, fromY, toX, toY);

    }

    @Override
    public String getPiece() {
        return("Rook");
    }

}
