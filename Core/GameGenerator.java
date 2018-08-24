package byog.Core;


import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static byog.Core.MapGenerator.*;

/**
 * Game generator gives us our interactibility with the map generated.
 * Lays our the rules of the game, what the player and enemies can and cannot do.
 * Generates our main menu when you boot up the game.
 * Displays messages when you win,lose,start or quit a game.
 */

public class GameGenerator{
    private boolean mainMenu;
    private static TETile farmer = new TETile('a',Color.black,Color.white,"player","C:\\Users\\ali\\Desktop\\CS61b\\player.png");
    private static TETile damagedWall = new TETile('b',Color.black,Color.white,"player","C:\\Users\\ali\\Desktop\\CS61b\\rock1.png");
    private static TETile brokenWall = new TETile('b',Color.black,Color.white,"player","C:\\Users\\ali\\Desktop\\CS61b\\rock2.png");
    static TETile food = new TETile('b',Color.black,Color.white,"player","C:\\Users\\ali\\Desktop\\CS61b\\food.png");
    static TETile monster = new TETile('b',Color.black,Color.white,"player","C:\\Users\\ali\\Desktop\\CS61b\\enemy3.png");



    /**
     * main method that boots up game.
     * Displays main menu, after player makes his/her decision, it loads game for you to play.
     */


    public void startGame() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        mainMenu = true;

        while (mainMenu == true) {
            mainMenu();
            StdDraw.pause(1500);
            String userInput = menuChoice();
            if(userInput.equals("p") || userInput.equals("P")){
                List<Player> p = new ArrayList<>();
                List<Enemy> e = new ArrayList<>();
                enemy = e;
                player = p;
                TETile[][] world = generateMap(player,e);
                mainMenu = false;
                ter.renderFrame(world);
                while(mainMenu == false) {
                    player(world, p);


                    mainMenu = true;

                }
            }
            if(userInput.equals("l") || userInput.equals("L")){
                loreMenu();
                menuChoice();


            }
            if(userInput.equals("q") || userInput.equals("Q")){
                mainMenu = false;
                System.exit(0);
            }
        }
    }

    /**
     * Generates the main menu interface.
     */

    public void mainMenu() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        String title = "The Quiet Place";
        String New = "Play Game (P)";
        String Lore = "Lore (L)";
        String quit = "Quit Game (Q)";


        Font largeFont = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setFont(largeFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight + 5, title);
        StdDraw.text(midWidth, midHeight, New);
        StdDraw.text(midWidth, midHeight - 2, Lore);
        StdDraw.text(midWidth, midHeight - 4, quit);
        StdDraw.show();


    }

    /**
     * Menu explaining the back story behind the game.
     */

    public void loreMenu() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        String title = "You are a scavenger in a Dungeon looking for food for your family.";
        String part1 = "A blind monster who tracks their prey purely on sound (including footsteps) is in the dungeon with you.";
        String part2 = "You need to collect all the food before your leave the dungeon to feed your family.";
        String part3 = "Tip: Don't bang on the walls, make every step count!";
        String exit = "Press any key to leave and return to main menu";


        Font largeFont = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setFont(largeFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight + 2, title);
        StdDraw.text(midWidth, midHeight, part1);
        StdDraw.text(midWidth, midHeight - 2, part2);
        StdDraw.text(midWidth, midHeight - 4, part3);
        StdDraw.text(midWidth, midHeight - 8, exit);
        StdDraw.show();


    }

    /**
     * Allows player to type in the key relating to there choice in how they wish to start their game.
     *
     * @return character player pressed to decide if they want to play a new game or load old game.
     */

    public String menuChoice() {
        String input = "";
        int i = 0;

        while (i < 1) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input += String.valueOf(key);
            if(key == 'p' || key == 'P') {
                drawFrame("Collect all the Apples!");
                StdDraw.pause(2500);
                drawFrame("Avoid The Monster!");
                StdDraw.pause(2500);
            }
            if(key == 'q' || key == 'Q'){
                drawFrame("Returning to Main Menu");
                StdDraw.pause(1000);
            }
            i++;
        }
        StdDraw.pause(500);
        return input;
    }

    /**
     * This method generates the 16X16 sprite of the player and makes it so you can move the player over any space
     * except through walls.
     * If you step over a token, the token amount decreases while if you step over an enemy you lose your only life
     * and lose the game.
     *
     * @param world 2D tile based world where we place our player
     * @param players List of size 1 that gives us our players randomly generated x and y Position on the map.
     */

    public void player(TETile[][] world, List<Player> players) {
        int lives = 1;
        Enemy enemy1 = enemy.get(0);
        TERenderer ter = new TERenderer();
        Player p = players.get(0);
        world[p.xPos][p.yPos] = farmer;
        ter.renderFrame(world);


        while (tokenAmount != 0 && lives != 0) {
            //allow player to move until they obtained all tokens or until they get attacked.

            if(world[p.xPos][p.yPos] == world[enemy1.xPos][enemy1.yPos]){
                lives -= 1;
            }
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            switch (key) {
                case 'w':
                    enemy(world, enemy, 0); //Monster moves only when it hears a footstep.
                    if (world[p.xPos][p.yPos + 1] == world[enemy1.xPos][enemy1.yPos] || world[p.xPos][p.yPos + 1] == monster ) {
                        //Ends the game if you run into the monster.
                        lives -= 1;
                    }
                    if (world[p.xPos][p.yPos + 1] == Tileset.WALL) {
                        world[p.xPos][p.yPos] = farmer;
                        ter.renderFrame(world);
                        continue;
                    }
                    else if (world[p.xPos][p.yPos + 1] == food) {
                        p.yPos += 1;
                        world[p.xPos][p.yPos] = farmer;
                        world[p.xPos][p.yPos - 1] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        tokenAmount -= 1;
                        continue;
                    }  else {
                        p.yPos += 1;
                        world[p.xPos][p.yPos] = farmer;
                        world[p.xPos][p.yPos - 1] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        continue;
                    }


                case 's':
                    enemy(world, enemy, 0);
                    if (world[p.xPos][p.yPos - 1] == world[enemy1.xPos][enemy1.yPos] || world[p.xPos][p.yPos - 1] == monster) {
                        lives -= 1;
                    }
                    if (world[p.xPos][p.yPos - 1] == Tileset.WALL) {
                        world[p.xPos][p.yPos] = farmer;
                        ter.renderFrame(world);
                        continue;
                    }
                    else if (world[p.xPos][p.yPos - 1] == food) {
                        p.yPos -= 1;
                        world[p.xPos][p.yPos] = farmer;
                        world[p.xPos][p.yPos + 1] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        tokenAmount -= 1;
                        continue;
                }
                    else {
                        p.yPos -= 1;
                        world[p.xPos][p.yPos] = farmer;
                        world[p.xPos][p.yPos + 1] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        continue;
                    }

                case 'a':
                    enemy(world, enemy, 0);
                    if (world[p.xPos - 1][p.yPos] == world[enemy1.xPos][enemy1.yPos] || world[p.xPos - 1][p.yPos] == monster) {
                        lives -= 1;
                    }
                    if (world[p.xPos - 1][p.yPos] == Tileset.WALL) {
                        world[p.xPos][p.yPos] = farmer;
                        ter.renderFrame(world);
                        continue;
                    }
                    else if (world[p.xPos - 1][p.yPos] == food) {
                        p.xPos -= 1;
                        world[p.xPos][p.yPos] = farmer;
                        world[p.xPos + 1][p.yPos] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        tokenAmount -= 1;
                        continue;
                    }
                    else {
                        p.xPos -= 1;
                        world[p.xPos][p.yPos] = farmer;
                        world[p.xPos + 1][p.yPos] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        continue;
                    }


                case 'd':
                    enemy(world, enemy, 0);
                    if (world[p.xPos + 1][p.yPos] == world[enemy1.xPos][enemy1.yPos] || world[p.xPos + 1][p.yPos] == monster) {
                        lives -= 1;
                    }
                    if (world[p.xPos + 1][p.yPos] == Tileset.WALL) {
                        world[p.xPos][p.yPos] = farmer;
                        ter.renderFrame(world);
                        continue;
                    }
                    else if (world[p.xPos + 1][p.yPos] == food){
                        p.xPos += 1;
                        world[p.xPos][p.yPos] = farmer;
                        world[p.xPos - 1][p.yPos] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        tokenAmount -= 1;
                        continue;
                }

                    else {
                        p.xPos += 1;
                        world[p.xPos][p.yPos] = farmer;
                        world[p.xPos - 1][p.yPos] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        continue;
                    }

            }
            if (key == 'q'){
                drawFrame("See You Next Time!");
                StdDraw.pause(750);
                break;
            }
        }
        if(tokenAmount == 0) {
            drawFrame("You Win!");
            StdDraw.pause(2000);
            player.remove(0);
            enemy.remove(0);
        }
        else if(lives == 0){
            drawFrame("You Lost.");
            StdDraw.pause(2000);
            player.remove(0);
            enemy.remove(0);
            tokenAmount = 0;
        }
        else{
            drawFrame("Thanks for Playing!");
            StdDraw.pause(2000);
            player.remove(0);
            enemy.remove(0);
            tokenAmount = 0;
        }

    }

    /**
     * Generates 16X16 enemy sprite. Enemy moves depending on its position relative to the player.
     * If the enemy runs into a wall it will change directions until it can find its best path towards you.
     * Enemies only move if you move.
     *
     * Enemies are based off the monsters in the movie "The Quiet Place" where they only come our when they hear any
     * sound including footsteps.
     * @param world
     * @param enemies
     * @param x
     */

    public static void enemy(TETile[][] world,List<Enemy> enemies,int x){
        Player p = player.get(0);
        TERenderer ter = new TERenderer();
        Enemy e = enemies.get(x);
        world[e.xPos][e.yPos] = monster;
        ter.renderFrame(world);
        int i = 0;
        while (i < 1) {
            int direction;
            if(p.xPos - e.xPos > 0 && p.yPos - e.yPos > 0){
                if(p.xPos - e.xPos > p.yPos - e.yPos){
                    if(world[e.xPos + 1][e.yPos] == Tileset.WALL){
                        direction = 0;
                    }
                    else {
                        direction = 3;
                    }
                }
                else {
                    if(world[e.xPos][e.yPos + 1] == Tileset.WALL){
                        direction = 3;
                    }
                    else {
                        direction = 0;
                    }
                }

            }
            else if(p.xPos - e.xPos < 0 && p.yPos - e.yPos < 0) {
                if (Math.abs(p.xPos - e.xPos) < Math.abs(p.yPos - e.yPos)) {
                    if (world[e.xPos][e.yPos - 1] == Tileset.WALL) {
                        direction = 2;
                    } else {
                        direction = 1;
                    }
                }

                else {
                    if(world[e.xPos - 1][e.yPos] == Tileset.WALL){
                        direction = 1;
                    }
                    else {
                        direction = 2;
                    }
                }

            }

            else if(p.yPos - e.yPos < 0 && p.xPos - e.xPos > 0){
                if(Math.abs(p.yPos - e.yPos) > p.xPos - e.xPos){
                    if(world[e.xPos][e.yPos - 1] == Tileset.WALL || world[e.xPos][e.yPos + 1] == Tileset.WALL){
                        direction = 3;
                    }
                    else {
                        direction = 1;
                    }
                }
                else{
                    if(world[e.xPos + 1][e.yPos] == Tileset.WALL){
                        direction = 1;
                    }
                    else{
                        direction = 3;
                    }
                }

            }
            else if(p.yPos - e.yPos > 0 && p.xPos - e.xPos < 0){
                if(Math.abs(p.xPos - e.xPos) > p.yPos - e.yPos){
                    if(world[e.xPos][e.yPos + 1] == Tileset.WALL){
                        direction = 2;
                    }
                    else {
                        direction = 0;
                    }
                }
                else{
                    if(world[e.xPos][e.yPos - 1] == Tileset.WALL || world[e.xPos][e.yPos + 1] == Tileset.WALL){
                        direction = 2;
                    }
                    else{
                        direction =0;
                    }
                }
            }
            else{
                if(p.yPos - e.yPos > 0 && p.xPos - e.xPos == 0 ){
                    direction = 0;
                }
                else if(p.yPos - e.yPos < 0 && p.xPos - e.xPos == 0){
                    direction = 1;
                }
                else if(p.xPos - e.xPos > 0 && p.yPos - e.yPos == 0){
                    if(world[e.xPos - 1][e.yPos] == Tileset.WALL && world[e.xPos + 1][e.yPos] == Tileset.WALL){
                        direction = 1;
                    }
                    else {
                        direction = 3;
                    }
                }
                else{
                    if(world[e.xPos - 1][e.yPos] == Tileset.WALL){
                        direction = 1;
                    }
                    else {
                        direction = 2;
                    }
                }

            }

            switch (direction) {
                case 0:
                    if (world[e.xPos][e.yPos] == farmer) {
                        world[e.xPos][e.yPos] = world[p.xPos][p.yPos];
                        break;
                    }
                    if(world[e.xPos][e.yPos + 1] == Tileset.NOTHING){
                        verticalHallway(world,e.xPos - 1,e.yPos + 1);
                        ter.renderFrame(world);
                    }
                    if(world[e.xPos][e.yPos + 1] == Tileset.WALL && world[e.xPos][e.yPos + 2] != Tileset.NOTHING){
                        world[e.xPos][e.yPos + 1] = damagedWall;
                        i++;
                        ter.renderFrame(world);
                        break;
                    }
                    if(world[e.xPos][e.yPos + 1] == damagedWall){
                        world[e.xPos][e.yPos + 1] = brokenWall ;
                        i++;
                        ter.renderFrame(world);
                        break;
                    }
                    else if (world[e.xPos][e.yPos + 1] == Tileset.WALL || world[e.xPos][e.yPos + 1] == food ||
                            world[e.xPos][e.yPos + 1] == damagedWall) {
                        world[e.xPos][e.yPos] = monster;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }
                    else {
                        e.yPos += 1;
                        world[e.xPos][e.yPos] = monster;
                        world[e.xPos][e.yPos - 1] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }


                case 1:
                    if (world[e.xPos][e.yPos] == farmer) {
                        world[e.xPos][e.yPos] = world[p.xPos][p.yPos];
                        break;
                    }
                    if(world[e.xPos][e.yPos - 1] == Tileset.NOTHING){
                        verticalHallway(world,e.xPos - 1,e.yPos - 1);
                        ter.renderFrame(world);
                    }
                    if(world[e.xPos][e.yPos - 1] == Tileset.WALL && world[e.xPos][e.yPos - 2] != Tileset.NOTHING){
                    world[e.xPos][e.yPos - 1] = damagedWall;
                    ter.renderFrame(world);
                    i++;
                    break;
                    }
                    if(world[e.xPos][e.yPos - 1] == damagedWall){
                        world[e.xPos][e.yPos - 1] = brokenWall ;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }
                    else if (world[e.xPos][e.yPos - 1] == Tileset.WALL || world[e.xPos][e.yPos - 1] == food ||
                            world[e.xPos][e.yPos - 1] == damagedWall) {
                        world[e.xPos][e.yPos] = monster;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }
                    else {
                        e.yPos -= 1;
                        world[e.xPos][e.yPos] = monster;
                        world[e.xPos][e.yPos + 1] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }


                case 2:
                    if (world[e.xPos][e.yPos] == farmer) {
                        world[e.xPos][e.yPos] = world[p.xPos][p.yPos];
                        break;
                    }
                    if(world[e.xPos - 1][e.yPos] == Tileset.NOTHING){
                        horizontalHallway(world,e.xPos - 1,e.yPos - 1);
                        ter.renderFrame(world);
                    }
                    if(world[e.xPos - 1][e.yPos] == Tileset.WALL && world[e.xPos - 2][e.yPos] != Tileset.NOTHING){
                        world[e.xPos - 1][e.yPos] = damagedWall;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }
                    if(world[e.xPos - 1][e.yPos] == damagedWall){
                        world[e.xPos - 1][e.yPos] = brokenWall ;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }
                    else if (world[e.xPos - 1][e.yPos] == Tileset.WALL || world[e.xPos - 1][e.yPos] == food
                    || world[e.xPos - 1][e.yPos] == damagedWall ) {
                        world[e.xPos][e.yPos] = monster;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }
                    else {
                        e.xPos -= 1;
                        world[e.xPos][e.yPos] = monster;
                        world[e.xPos + 1][e.yPos] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }


                case 3:
                    if (world[e.xPos][e.yPos] == farmer) {
                        world[e.xPos][e.yPos] = world[p.xPos][p.yPos];
                        break;
                    }
                    if(world[e.xPos + 1][e.yPos] == Tileset.NOTHING){
                        horizontalHallway(world,e.xPos - 1,e.yPos - 1);
                        ter.renderFrame(world);
                    }
                    if(world[e.xPos + 1][e.yPos] == Tileset.WALL && world[e.xPos + 2][e.yPos] != Tileset.NOTHING){
                    world[e.xPos + 1][e.yPos] = damagedWall;
                    ter.renderFrame(world);
                    i++;
                    break;
                    }
                    if(world[e.xPos + 1][e.yPos] == damagedWall){
                        world[e.xPos + 1][e.yPos] = brokenWall ;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }
                    else if (world[e.xPos + 1][e.yPos] == Tileset.WALL || world[e.xPos + 1][e.yPos] == food ||
                            world[e.xPos + 1][e.yPos] == damagedWall) {
                        world[e.xPos][e.yPos] = monster;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }
                    else {
                        e.xPos += 1;
                        world[e.xPos][e.yPos] = monster;
                        world[e.xPos - 1][e.yPos] = Tileset.FLOOR;
                        ter.renderFrame(world);
                        i++;
                        break;
                    }

            }
        }

    }


    /**
     * Displays our messages on the center of the screen.
     * Is only called before and after the game.
     * @param s the message we would like to display on the screen.
     */

    public void drawFrame(String s) {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        //GUI
        if (!mainMenu) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, HEIGHT - 1, "");
            StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        }

        // Actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
    }

}