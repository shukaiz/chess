package chessBoard;

public class Knight extends Piece {

    @Override
    public boolean validMove(int fromX, int fromY, int toX, int toY, boolean capture) {

        //Knight moves to any of the closest squares that are not on the same rank, file, or diagonal. (L-shaped)
        return ((Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 2) ||
                (Math.abs(toX - fromX) == 2 && Math.abs(toY - fromY) == 1));

    }

    @Override
    public String getPiece() {
        return("Knight");
    }

}