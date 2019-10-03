package chessGame;
import chessBoard.*;
import hsa_ufa.Console;
import java.awt.*;

public class Game {

    //Defines default length and height. (MINIMUM = 8)
    private static int length = 8;
    private static int height = 8;

    //Construct the board and players. Last two are for tracking last movement to undo.
    private static Piece[][] board = new Piece[length][height];
    private static Player bottom = new Player(false, board);
    private static Player top = new Player(true, board);
    private static Piece movedPiece = null;
    private static Piece capturedPiece = null;

    //Defines the player currently in control. Set to false because white has first-move.
    private static boolean blackMove = false;

    //Track if the click is to select piece or move.
    private static boolean firstClicked = false;

    //Find where move initiates. Last four are to track origin and dest. of last movement.
    private static int fromX = -1;
    private static int fromY = -1;
    private static int movedX = -1;
    private static int movedY = -1;
    private static int capturedX = -1;
    private static int capturedY = -1;

    //Defines the font and color for chess board.
    private static Color lightBrown = new Color(249, 206, 158);
    private static Color darkBrown = new Color(209, 139, 71);
    private static Color lightGreen = new Color(204, 208, 110);
    private static Color darkGreen = new Color(168, 161, 63);
    private static Color yellow = new Color(247, 244, 114);

    //Load chess pieces sprites.
    private static Image blackBishop = Toolkit.getDefaultToolkit().getImage("sprites/blackBishop.png");
    private static Image blackKing = Toolkit.getDefaultToolkit().getImage("sprites/blackKing.png");
    private static Image blackKnight = Toolkit.getDefaultToolkit().getImage("sprites/blackKnight.png");
    private static Image blackPawn = Toolkit.getDefaultToolkit().getImage("sprites/blackPawn.png");
    private static Image blackQueen = Toolkit.getDefaultToolkit().getImage("sprites/blackQueen.png");
    private static Image blackRook = Toolkit.getDefaultToolkit().getImage("sprites/blackRook.png");
    private static Image whiteBishop = Toolkit.getDefaultToolkit().getImage("sprites/whiteBishop.png");
    private static Image whiteKing = Toolkit.getDefaultToolkit().getImage("sprites/whiteKing.png");
    private static Image whiteKnight = Toolkit.getDefaultToolkit().getImage("sprites/whiteKnight.png");
    private static Image whitePawn = Toolkit.getDefaultToolkit().getImage("sprites/whitePawn.png");
    private static Image whiteQueen = Toolkit.getDefaultToolkit().getImage("sprites/whiteQueen.png");
    private static Image whiteRook = Toolkit.getDefaultToolkit().getImage("sprites/whiteRook.png");

    //Currently this is only for demonstration. Next phase is to add game loop in here.
    public static void main(String[] args) throws InterruptedException {

        //Place pieces by default on empty board. Do not use this if using customized pieces.
        initBoard(board);

        //Initialize GUI related items with size depending on board length and height.
        Console c = new Console(length * 80 + 320, height * 80, 15, "Chess");
        Font sansSerif = new Font("SansSerif", Font.PLAIN, 15);
        c.enableMouse();
        c.enableMouseMotion();
        c.setFont(sansSerif);

        //Defines mouse location for control.
        int mouseX;
        int mouseY;

        //Main game loop.
        while (!c.isKeyDown('Q')) {

            //Controls frame rate of the game.
            Thread.sleep(5);

            //Restarts the game (reconstructs objects and reset booleans).
            if (c.isKeyDown('R')) {

                board = new Piece[length][height];
                bottom = new Player(false, board);
                top = new Player(true, board);
                initBoard(board);
                blackMove = false;
                firstClicked = false;
                fromX = -1;
                fromY = -1;
                movedPiece = null;
                capturedPiece = null;
                movedX = -1;
                movedY = -1;
                capturedX = -1;
                capturedY = -1;

                //This prevents listener from repeating restart actions.
                Thread.sleep(100);

            }

            //Undo the very last movement.
            if (c.isKeyDown('U')) {

                //Add to game log if attempts to undo before first move.
                if (movedPiece == null) {

                    if (blackMove) {
                        top.writeMoves("Undo not allowed");
                    } else {
                        bottom.writeMoves("Undo not allowed");
                    }

                } else {

                    //Undo the last move by place each piece back.
                    board[movedX][movedY] = movedPiece;
                    board[capturedX][capturedY] = capturedPiece;

                    //Return control to another player after undo.
                    if (blackMove) {
                        bottom.writeMoves("Undid last move");
                        blackMove = false;
                        firstClicked = false;
                    } else {
                        top.writeMoves("Undid last move");
                        blackMove = true;
                        firstClicked = false;
                    }

                    //Reset undo actions.
                    movedPiece = null;
                    capturedPiece = null;
                    movedX = -1;
                    movedY = -1;
                    capturedX = -1;
                    capturedY = -1;

                }

                //This prevents listener from repeating undo actions.
                Thread.sleep(100);

            }

            //Find square which mouse is in. (This is same as Piece[][])
            mouseX = c.getMouseX() / 80;
            mouseY = height - (c.getMouseY() / 80) - 1;

            //Get mouse click action.
            if (mouseX < length && c.getMouseClick() == 1) {

                //First click decides originating location.
                if (!firstClicked) {

                    if (board[mouseX][mouseY] == null) {
                        continue;
                    }
                    fromX = mouseX;
                    fromY = mouseY;
                    firstClicked = true;

                } else {

                    //Another click on the same square cancels the action.
                    if (mouseX == fromX && mouseY == fromY) {
                        firstClicked = false;
                        continue;
                    }

                    //Second click is the destination. Record piece and location info of last movement.
                    if (blackMove) {
                        Piece tempMovedPiece = board[fromX][fromY];
                        Piece tempCapturedPiece = board[mouseX][mouseY];
                        if (top.move(board, fromX, fromY, mouseX, mouseY)) {
                            blackMove = false;
                            movedPiece = tempMovedPiece;
                            capturedPiece = tempCapturedPiece;
                            movedX = fromX;
                            movedY = fromY;
                            capturedX = mouseX;
                            capturedY = mouseY;
                        }
                    } else {
                        Piece tempMovedPiece = board[fromX][fromY];
                        Piece tempCapturedPiece = board[mouseX][mouseY];
                        if (bottom.move(board, fromX, fromY, mouseX, mouseY)) {
                            blackMove = true;
                            movedPiece = tempMovedPiece;
                            capturedPiece = tempCapturedPiece;
                            movedX = fromX;
                            movedY = fromY;
                            capturedX = mouseX;
                            capturedY = mouseY;
                        }
                    }

                    firstClicked = false;

                }
            }

            //Renders the board.
            render(board, c, mouseX, mouseY);

        }

        //Close the console when game finishes.
        c.close();

    }

    //Paint the empty board GUI.
    public static void render(Piece[][] board, Console c, int mouseX, int mouseY) {

        //Execute all rendering at same time to avoid flickering.
        synchronized (c) {

            //Draws the empty chess board GUI. (Note: This uses a different coordinate than pieces)
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < height; j++) {

                    //Highlight the square which have been clicked on.
                    if (firstClicked) {
                        c.setColor(yellow);
                        c.fillRect(fromX * 80, (height - fromY - 1) * 80, 80, 80);
                        if (fromX == 7 && fromY == 0) {
                            c.fillRect(560, 560, 79, 79);
                        }
                    }

                    //Highlight the square which mouse is currently in.
                    if ((c.getMouseX() > 0) && (c.getMouseY() > 0) && (i == mouseX) && (j == (height - mouseY - 1))) {

                        //Different color if different player is moving.
                        if (blackMove) {
                            c.setColor(darkGreen);
                        } else {
                            c.setColor(lightGreen);
                        }

                        c.fillRect(i * 80, j * 80, 80, 80);

                        continue;
                    }

                    //Staggers the board spaces with two different colors.
                    if ((i + j) % 2 == 0) {
                        c.setColor(lightBrown);
                    } else {
                        c.setColor(darkBrown);
                    }

                    //Draws the empty space GUI on board.
                    c.fillRect(i * 80, j * 80, 80, 80);

                }
            }

            //Draws vertical coordinates on the board.
            for (int i = 0; i < height; i++) {
                if (i % 2 == 0) {
                    c.setColor(darkBrown);
                } else {
                    c.setColor(lightBrown);
                }

                c.drawString(Integer.toString(height - i), 2, i * 80 + 15);

            }

            //Draws horizontal coordinates on the board.
            for (int i = 0; i < length; i++) {

                //Color staggering might be different depending on height of board.
                if ((i + height) % 2 == 0) {
                    c.setColor(lightBrown);
                } else {
                    c.setColor(darkBrown);
                }

                c.drawString(Character.toString((char) ('a' + i)), i * 80 + 68, height * 80 - 5);

            }

            //Draws the chess pieces GUI. (Note: This uses a different coordinate than board)
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < height; j++) {

                    //Continue and do not draw piece if a space is empty.
                    if (board[i][j] == null) {
                        continue;
                    }

                    //Draw pieces on the board GUI. Separated by pieces colors.
                    if (board[i][j].isBlack) {
                        drawPieces(board, c, i, j, blackBishop, blackKing, blackKnight, blackPawn, blackQueen, blackRook);
                    } else if (!board[i][j].isBlack) {
                        drawPieces(board, c, i, j, whiteBishop, whiteKing, whiteKnight, whitePawn, whiteQueen, whiteRook);
                    }
                }
            }

            drawMessages(c);

        }

    }

    //Renders the texts GUI on the right side.
    private static void drawMessages(Console c) {

        //Draw the text output on the right side.
        c.setColor(Color.black);
        c.drawString("Current Player: ", length * 80 + 10, 20);
        c.drawString("Black moves:", length * 80 + 10, 50);
        c.drawString("White moves:", length * 80 + 10, height * 80 / 2 + 25);
        c.drawString("Press 'Q' - Quit  'R' - Restart  'U' - Undo", length * 80 + 10, height * 80 - 20);

        //Displays which player's turn is it.
        if (blackMove) {
            c.setColor(Color.white);
            c.fillRect(length * 80 + 125, 5, 50, 20);
            c.setColor(Color.black);
            c.drawString("Black", length * 80 + 130, 20);
        } else {
            c.fillRect(length * 80 + 125, 5, 50, 20);
            c.setColor(Color.white);
            c.drawString("White", length * 80 + 130, 20);
        }

        //Draw game moves log (only output last 12 moves).
        c.setColor(Color.black);
        if (top.getMoves().size() > 12) {
            c.setColor(Color.white);
            c.fillRect(length * 80 + 10, 50, 300,250);
            c.setColor(Color.black);
            for (int i = top.getMoves().size() - 12; i < top.getMoves().size(); i++) {
                c.drawString(top.getMoves().get(i), length * 80 + 10, 70 + (i - top.getMoves().size() + 12) * 20);
            }
        } else {
            c.setColor(Color.white);
            c.fillRect(length * 80 + 10, 50, 300,250);
            c.setColor(Color.black);
            for (int i = 0; i < top.getMoves().size(); i++) {
                c.drawString(top.getMoves().get(i), length * 80 + 10, 70 + i * 20);
            }
        }

        if (bottom.getMoves().size() > 12) {
            c.setColor(Color.white);
            c.fillRect(length * 80 + 10, height * 80 / 2 + 25, 300,250);
            c.setColor(Color.black);
            for (int i = bottom.getMoves().size() - 12; i < bottom.getMoves().size(); i++) {
                c.drawString(bottom.getMoves().get(i), length * 80 + 10, height * 80 / 2 + 45 + (i - bottom.getMoves().size() + 12) * 20);
            }
        } else {
            c.setColor(Color.white);
            c.fillRect(length * 80 + 10, height * 80 / 2 + 25, 300,250);
            c.setColor(Color.black);
            for (int i = 0; i < bottom.getMoves().size(); i++) {
                c.drawString(bottom.getMoves().get(i), length * 80 + 10, height * 80 / 2 + 45 + i * 20);
            }
        }

    }

    private static void drawPieces(Piece[][] board, Console c, int i, int j, Image imgBishop, Image imgKing,
                                   Image imgKnight, Image imgPawn, Image imgQueen, Image imgRook) {

        //Draw pieces on the board GUI. Note: This uses a different coordinate than Piece[][] board.
        switch (board[i][j].getPiece()) {
            case "Bishop":
                c.drawImage(imgBishop, i * 80 + 15, height * 80 - (j + 1) * 80 + 15);
                break;
            case "King":
                c.drawImage(imgKing, i * 80 + 15, height * 80 - (j + 1) * 80 + 15);
                break;
            case "Knight":
                c.drawImage(imgKnight, i * 80 + 15, height * 80 - (j + 1) * 80 + 15);
                break;
            case "Pawn":
                c.drawImage(imgPawn, i * 80 + 15, height * 80 - (j + 1) * 80 + 15);
                break;
            case "Queen":
                c.drawImage(imgQueen, i * 80 + 15, height * 80 - (j + 1) * 80 + 15);
                break;
            case "Rook":
                c.drawImage(imgRook, i * 80 + 15, height * 80 - (j + 1) * 80 + 15);
                break;
        }

    }

    public static void initBoard(Piece[][] board) {
        //This function place pieces to initial positions.

        for (int i = 0; i < 8; i++) {

            //Fill the 2nd to bottom row with pawns and set them to white.
            board[i][1] = new Pawn();
            board[i][1].isBlack = false;

            //Fill the 2nd to top row with pawns and set them to black.
            board[i][board[i].length - 2] = new Pawn();
            board[i][board[i].length - 2].isBlack = true;

        }

        //Fill the back row with pieces other than pawns.
        setupBackRow(board, 0);
        setupBackRow(board, board[0].length - 1);

    }

    private static void setupBackRow(Piece[][] board, int rank) {
        //Place pieces other than pawns to back rows of the board.

        boolean isBlack;

        /*
        Assigns colors to pieces. Black on top; White on bottom.
        (Rank is row in chess)
         */
        if (rank == 0) {
            isBlack = false;
        } else {
            isBlack = true;
        }

        //Place pieces to back rows and assign color.
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0: case 7:
                    board[i][rank] = new Rook();
                    break;
                case 1: case 6:
                    board[i][rank] = new Knight();
                    break;
                case 2: case 5:
                    board[i][rank] = new Bishop();
                    break;
                case 3:
                    board[i][rank] = new Queen();
                    break;
                case 4:
                    board[i][rank] = new King();
                    break;
            }
            board[i][rank].isBlack = isBlack;
        }

    }

}