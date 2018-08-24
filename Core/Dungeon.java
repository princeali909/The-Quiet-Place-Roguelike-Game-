package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/**
 * Stores the height and width of our randomly generated dungeons across the map.
 * Stores the x and y position of the bottom left corner of our dungeons.
 */


public class Dungeon {

    int roomWidth;
    int roomHeight;
    int x;
    int y;

    /**
     * Constructs our Dungeon object
     *
     * @param x x position of lower left corner of dungeon
     * @param y y position of lower left corner of dungeon
     * @param roomWidth width of dungeon
     * @param roomHeight height of dungeon
     */

    public Dungeon(int x, int y,int roomWidth,int roomHeight){
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.x = x;
        this.y = y;
    }

    /**
     * Generated dungeon surrounded by a 'Wall' tile space.
     *
     * @param world 2D tile based world to place our dungeon
     */

    public void addDungeon(TETile[][] world){
        Dungeon r = this;
        //Create random sized dungeons with wall tiles.
        for (int x = r.x; x < r.x + r.roomWidth; x += 1) {
            for (int y = r.y; y < r.y + r.roomHeight; y += 1) {
                world[x][y] = Tileset.WALL;
            }
        }

        //Fill in with floor tiles so that the wall tiles only make up the exterior of the dungeon.
        for (int x = r.x + 1;x < r.x +  r.roomWidth - 1;x++){
            for(int y = r.y + 1; y < r.y + r.roomHeight - 1; y++){
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Checks to see as dungeons are randomly generating, if they overlap by checking if various
     * positions in the dungeon occupy a 'Nothing' tile space.
     *
     * @param world 2D tile based world needed to check if our dungeons overlap
     * @param width width of our dungeon
     * @param height height of our dungeon
     * @return {@code true} if the coordinates {@code width} and {@code height} do not occupy a 'Nothing' tile space;
     *               {@code false} otherwise
     */

    public boolean doesOverlap(TETile[][] world,int width,int height){
        Dungeon p = this;
        TETile[][] a = world;
        if(a[p.x][p.y] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x + width + 1][p.y] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x][p.y + height + 1] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x + width + 1][p.y + height + 1] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x][p.y] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x + width][p.y] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x][p.y + height] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x + width][p.y + height] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x + (width / 2)][p.y] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x][p.y + (height / 2)] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x + (width / 3)][p.y] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x][p.y + (height / 3)] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x + width][p.y + (height / 2)] != Tileset.NOTHING){
            return true;
        }
        if(a[p.x + (width /2)][p.y + height] != Tileset.NOTHING){
            return true;
        }
        else {
            return false;
        }
    }


}
