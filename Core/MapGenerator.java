package byog.Core;


import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.io.Serializable;
import java.util.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import static byog.Core.GameGenerator.food;
import static byog.Core.GameGenerator.monster;

/**
 * MapGenerator class creates our randomly generated world every time we start a new game.
 */

public class MapGenerator implements Serializable {
    /**
     * Size of our map is initialized with a set WIDTH & HEIGHT
     * The Dungeons in the map each have a max/min width and height that is randomly determined every new game.
     *
     * To avoid overflowing the map we set a max amount of Dungeons(maxDungeons). This was determined
     * using the formula (WIDTH/maxDungeonWidth)*(Height/maxDungeonHeight)
     *
     * The player's & enemies attributes are stored in a List
     */
    static final int WIDTH = 80;
    static final int HEIGHT = 40;
    private static final int maxDungeonWidth = 8;
    private static final int minDungeonWidth = 4;
    private static final int maxDungeonHeight = 8;
    private static final int minDungeonHeight = 4;
    private static final int maxDungeons = 50;
    private static final double minDungeonRatio = 0.3;
    private static final double maxDungeonRatio = 0.7;
    static List<Player> player;
    static List<Enemy> enemy;
    static int tokenAmount;

    /**
     *Calls all appropriate methods to randomly fill up and generate our game map.
     *
     * @param p the list storing players attributes (xPos,yPos)
     * @param e the list storing enemies attributes (xPos,yPos)
     * @return The randomly generated world after calling appropriate functions.
     */

    public static TETile[][] generateMap(List<Player> p,List<Enemy> e){
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        List<Dungeon> a = addDungeons(world);
        connectDungeons(world,a);
        addPlayer(a,p);
        addTokens(world,a);
        addEnemy(world,a,e);
        return world;
    }

    /**
     * Method randomly places dungeons of randomly generated sizes within its set parameters.
     *
     * @param world the 2D tile based world we need to place our dungeons inside.
     * @return
     */

    private static List<Dungeon> addDungeons(TETile[][] world){
        Random rand = new Random();
        int totalRooms = RandomUtils.uniform(rand,(int) (maxDungeons * minDungeonRatio),
                (int) (maxDungeons * maxDungeonRatio));
        List<Dungeon> dungeons = new ArrayList<>(totalRooms);
        Dungeon dungeon;
        int height, width, x,y;
        for(int i = 0; i < totalRooms;i++) {
            width = RandomUtils.uniform(rand, minDungeonWidth, maxDungeonWidth);
            height = RandomUtils.uniform(rand, minDungeonHeight, maxDungeonHeight);
            x = rand.nextInt(WIDTH - maxDungeonWidth);
            y = rand.nextInt(HEIGHT - maxDungeonHeight);
            dungeon = new Dungeon(x,y,width,height);
            if (dungeon.doesOverlap(world,width,height)){
                //If we find that dungeons overlap then we do not count it as dungeon and loop again.
                i -= 1;
                continue;
            }
            dungeon.addDungeon(world);
            dungeons.add(dungeon);
        }
        return dungeons;

    }

    /**
     *Using the centers coordinates of our dungeons as the targets, hallways starting in first dungeon randomly generate
     * in random directions. Method ends when all dungeons are connected.
     *
     * @param world the 2D tile based world our dungeons are occupying.
     * @param dungeons List containing every dungeon generated
     */

    private static void connectDungeons(TETile[][] world, List<Dungeon> dungeons) {
        Random rand = new Random();
        int deltaX, deltaY;
        int currentX, currentY;
        boolean movingInX;
        int hallwayLength;
        Dungeon currentDungeon, goalDungeon;
        Iterator<Dungeon> seer = dungeons.iterator();
        currentDungeon = seer.next();
        while(seer.hasNext()){
            goalDungeon = seer.next();
            currentX = currentDungeon.x + (currentDungeon.roomWidth / 2);
            currentY = currentDungeon.y + (currentDungeon.roomHeight / 2);
            deltaX = (goalDungeon.x + (goalDungeon.roomWidth / 2)) - currentX;
            deltaY = (goalDungeon.y + (goalDungeon.roomHeight / 2)) - currentY;
            while (!(deltaX == 0 && deltaY == 0)) { //while we have distance to cover
                movingInX = rand.nextInt(1) == 1; //randomly decide if we are moving in the x direction or y direction
                if (movingInX && deltaX == 0) { //after randomly deciding we check if delta x is 0 so that movingInX is actually false
                    movingInX = false;
                }
                else if (!movingInX && deltaY == 0){ //checking if movingInX is false and deltaY is 0 then movingInX should actually be true
                    movingInX = true;
                }
                if(movingInX == true){
                    hallwayLength = rand.nextInt(Math.abs(deltaX))+1;
                }
                else{
                    hallwayLength = rand.nextInt(Math.abs(deltaY))+1;
                }
                for (int i = 0; i < hallwayLength; i++){
                    if(movingInX){
                        if (deltaX > 0) {
                            horizontalHallway(world,currentX,currentY - 1);
                            currentX += 1;
                        }
                        else{
                            horizontalHallway(world,currentX,currentY - 1);
                            currentX -= 1;
                        }
                    }
                    else{
                        if(deltaY > 0) {
                            verticalHallway(world,currentX - 1,currentY);
                            currentY += 1;
                        }
                        else{
                            verticalHallway(world,currentX - 1,currentY);
                            currentY -= 1;
                        }
                    }

                }
                if (movingInX){
                    if(deltaX > 0) {
                        corner(world,currentX,currentY - 1);
                        deltaX -= hallwayLength;
                    }
                    else{
                        corner(world,currentX,currentY - 1);
                        deltaX += hallwayLength;
                    }
                }
                else{
                    if(deltaY > 0) {
                        corner(world,currentX - 1,currentY);
                        deltaY -= hallwayLength;
                    }
                    else {
                        corner(world,currentX - 1,currentY);
                        deltaY += hallwayLength;
                    }
                }
            }
            currentDungeon = goalDungeon;
        }

    }


    /**
     * Method that draws our vertical hallways.
     *
     * @param world 2D tile based world to occupy our hallways
     * @param xPos x position of bottom left corner of hallway
     * @param yPos y position of bottom left corner of hallway
     */

    static void verticalHallway(TETile[][] world, int xPos, int yPos) {
        for (int i = 0; i < 1;i++) {
            int size = 1;
            for (int y = yPos; y < yPos + size; y++) {
                for (int x = xPos; x < xPos + 3;x++) {
                    if(world[x][y] == Tileset.NOTHING || world[x][y] == Tileset.WALL) {
                        world[x][y] = Tileset.WALL;
                    }
                    else{
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }
            for(int x = xPos + 1; x == xPos + 1; x++){
                for (int y = yPos; y < yPos + size; y++){
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * Method that draws our horizontal hallways.
     *
     * @param world 2D tile based world to occupy our hallways.
     * @param xPos x position of bottom left corner of hallway
     * @param yPos y position of bottom left corner of hallway
     */


        static void horizontalHallway(TETile[][] world, int xPos, int yPos){
            for (int i = 0;i <1;i++) {
                int size = 1;
                for (int y = yPos; y < yPos + 3; y++) {
                    for (int x = xPos; x < xPos + size; x += 1) {
                        if(world[x][y] == Tileset.NOTHING || world[x][y] == Tileset.WALL) {
                            world[x][y] = Tileset.WALL;
                        }
                        else{
                            world[x][y] = Tileset.FLOOR;
                        }
                    }
                }
                for (int y = yPos + 1; y == yPos + 1; y++){
                    for(int x = xPos; x < xPos + size; x++){
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }

        }

    /**
     * Draws 2X3 corner to fill opening as hallways randomly change directions.
     *
     * @param world 2D tile based world to occupy our corners.
     * @param xPos x position of bottom left corner of corner
     * @param yPos y position of bottom left corner of corner
     */


    private static void corner(TETile[][] world,int xPos,int yPos){
        if(world[xPos][yPos + 1] == Tileset.WALL || world[xPos][yPos + 1] == Tileset.NOTHING) {
            world[xPos][yPos + 1] = Tileset.WALL;
        }
        else{
            world[xPos][yPos + 1] = Tileset.FLOOR;
        }
        if(world[xPos + 1][yPos + 1] == Tileset.WALL || world[xPos + 1][yPos + 1] == Tileset.NOTHING) {
            world[xPos + 1][yPos + 1] = Tileset.WALL;
        }
        else{
            world[xPos + 1][yPos + 1] = Tileset.FLOOR;
        }
        if(world[xPos][yPos] == Tileset.WALL || world[xPos][yPos] == Tileset.NOTHING) {
            world[xPos][yPos] = Tileset.WALL;
        }
        else{
            world[xPos][yPos] = Tileset.FLOOR;
        }
        if(world[xPos + 1][yPos] == Tileset.WALL || world[xPos + 1][yPos] == Tileset.NOTHING) {
            world[xPos + 1][yPos] = Tileset.WALL;
        }
        else{
            world[xPos + 1][yPos] = Tileset.FLOOR;
        }
        if(world[xPos +2][yPos] == Tileset.WALL || world[xPos +2][yPos] == Tileset.NOTHING) {
            world[xPos + 2][yPos] = Tileset.WALL;
        }
        else{
            world[xPos + 2][yPos] = Tileset.FLOOR;
        }
        if(world[xPos+2][yPos+1] == Tileset.WALL || world[xPos+2][yPos+1] == Tileset.NOTHING) {
            world[xPos + 2][yPos + 1] = Tileset.WALL;
        }
        else{
            world[xPos + 2][yPos + 1] = Tileset.FLOOR;
        }
    }

    /**
     * Finds random dungeon to place our player when we generate world.
     *
     * @param dungeons List needed to select random dungeon to place player
     * @param players List we will use to store players attributes
     */

    private static void addPlayer(List<Dungeon> dungeons,List<Player> players){
        Random rand = new Random();
        int randomDungeon = rand.nextInt(dungeons.size());

        Dungeon playerDungeon = dungeons.get(randomDungeon);
        Player player = new Player(playerDungeon.x + playerDungeon.roomWidth / 2,playerDungeon.y + playerDungeon.roomHeight / 2);
        players.add(player);
    }

    /**
     * Places our tokens in random dungeons across the map. Player needs to collect all the tokens to win.
     *
     * @param world 2D tile based world to place our tokens that the player will need to collect to win.
     * @param dungeons List needed to select random dungeons to place tokens
     */

    private static void addTokens(TETile[][] world, List<Dungeon> dungeons){
        Random rand = new Random();
        int randomDungeons = rand.nextInt(dungeons.size());
        while (randomDungeons == 0){
            randomDungeons = rand.nextInt(dungeons.size());
        }

        for (int x = 0; x < randomDungeons; x++) {
            Dungeon playerDungeon = dungeons.get(x);
            // 1 subtracted from position so tokens don't overlap with enemies in case both spawn in same dungeon.
            Token token = new Token((playerDungeon.x + playerDungeon.roomWidth / 2) - 1, (playerDungeon.y + playerDungeon.roomHeight / 2) - 1);
            int xPos = token.xPos;
            int yPos = token.yPos;
            world[xPos][yPos] = food;
            tokenAmount += 1;
        }
    }

    /**
     * Finds random dungeon to place our enemy when we generate world.
     *
     * @param dungeons List needed to select random dungeon to place player
     * @param enemies List we will use to store our enemies attributes
     */

    private static void addEnemy(TETile[][] world, List<Dungeon> dungeons,List<Enemy> enemies){
        Random rand = new Random();
        int randomEnemyCount = rand.nextInt(dungeons.size());
        Dungeon enemyDungeon = dungeons.get(randomEnemyCount);
        Enemy enemy = new Enemy(enemyDungeon.x + enemyDungeon.roomWidth / 2,enemyDungeon.y + enemyDungeon.roomHeight / 2);
        enemies.add(enemy);
        world[enemy.xPos][enemy.yPos] = monster;
    }

}