package byog.Core;

/**
 * Stores the x & y position of our token.
 * All tokens in the game must be collected to win.
 */

public class Token extends GamePiece {

    /**
     * Constructs our Token object.
     * @param xPos x position of token
     * @param yPos y position of token
     */

    public Token(int xPos, int yPos) {
        super(xPos,yPos);
    }
}
