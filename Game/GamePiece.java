package byog.Core;

/**
 * Our super class that will store any game piece that runs only on an xPos & yPos.
 */

public abstract class GamePiece {
    public int xPos;
    public int yPos;

    /**
     * Constructs our game piece.
     * @param xPos x position of piece
     * @param yPos y position of piece
     */

    public GamePiece(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
