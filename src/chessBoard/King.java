package chessBoard;

public class King extends Piece {

    @Override
    public boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture) {

        //King moves one square in any direction.
        return (Math.abs(toX - fromX) == 1 ||
                Math.abs(toY - fromY) == 1);

    }

    @Override
    public String getPiece() {
        return("King");
    }

}