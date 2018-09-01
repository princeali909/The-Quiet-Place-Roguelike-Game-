package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.lang.reflect.WildcardType;
import java.util.*;

import static byog.Core.GameGenerator.*;
import static byog.Core.MapGenerator.*;

public class temp {

    protected static int[] distTo;
    protected static int[] edgeTo;

    public temp() {

        distTo = new int[WIDTH * HEIGHT];
        edgeTo = new int[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH * HEIGHT; i += 1) {
            distTo[i] = Integer.MAX_VALUE;
            edgeTo[i] = Integer.MAX_VALUE;
        }
    }

    /**
     * Returns x coordinate for given vertex.
     */
    public int toX(int v) {
        return v % WIDTH + 1;
    }

    /**
     * Returns y coordinate for given vertex.
     */
    public int toY(int v) {
        return v / HEIGHT + 1;
    }

    /**
     * Returns one dimensional coordinate for vertex in position x, y.
     */
    public static int xyTo1D(int x, int y) {
        return (y - 1) * HEIGHT + (x - 1);
    }

    private static int h(int enemyPos) {
        Player p = player.get(0);
        int x = p.xPos;
        int y = p.yPos;
        int oneDPlayerPos = xyTo1D(x, y);
        return distance(oneDPlayerPos, enemyPos);
    }

    private static int distance(int sourceX, int sourceY, int targetX, int targetY) {
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    private static int distance(int v, int w) {
        Enemy e = enemy.get(0);
        Player p = player.get(0);
        int sourceX = e.xPos;
        int sourceY = e.yPos;
        int targetX = p.xPos;
        int targetY = p.yPos;
        return distance(sourceX, sourceY, targetX, targetY);
    }

    public static Iterable<Integer> adj(int x, int y, TETile[][] world) {
        HashMap<Integer, Integer> neighbors = new HashMap<>();
        if (!wallExists(x, y, "North", world)) {
            neighbors.put(x, y + 1);
        }

        if (!wallExists(x, y, "East", world)) {
            neighbors.put(x + 1, y);
        }

        if (!wallExists(x, y, "South", world)) {
            neighbors.put(x, y - 1);
        }

        if (!wallExists(x, y, "West", world)) {
            neighbors.put(x - 1, y);
        }

        return (Iterable<Integer>) neighbors;
    }

    public static boolean wallExists(int x, int y, String Direction, TETile[][] world) {
        if (Direction == "North") {
            if (world[x][y + 1] == Tileset.WALL) {
                return true;
            } else {
                return false;
            }
        }
        if (Direction == "South") {
            if (world[x][y - 1] == Tileset.WALL) {
                return true;
            } else {
                return false;
            }
        }
        if (Direction == "East") {
            if (world[x + 1][y] == Tileset.WALL) {
                return true;
            } else {
                return false;
            }
        } else {
            if (world[x - 1][y] == Tileset.WALL) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static void astar(int oneDEnemyPos, TETile[][] world) {
        Enemy e = enemy.get(0);
        Player p = player.get(0);
        oneDEnemyPos = xyTo1D(e.xPos, e.yPos);
        int oneDPlayerPos = xyTo1D(p.xPos, p.yPos);
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(WIDTH * HEIGHT,
                new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        if (h((Integer) o1) < h((Integer) o2)) {
                            return -1;
                        } else if (h((Integer) o1) > h((Integer) o2)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });

        if (oneDEnemyPos == oneDPlayerPos) {
            return;
        }
        queue.add(oneDEnemyPos);
        while (queue.size() != 0) {
            int v = queue.remove();
            if (v == oneDPlayerPos) {
                return;
            }

            for (int w : adj(e.xPos, e.yPos, world)) {
                if (v == oneDPlayerPos) {
                    return;
                }

                if (distTo[w] > distTo[v] + 1) {
                    distTo[w] = distTo[v] + 1;
                    edgeTo[w] = v;
                    queue.add(w);
                }

            }
        }
    }


    public static void solve(TETile[][] world) {
        Enemy e = enemy.get(0);
        int oneDEnemyPos = xyTo1D(e.xPos, e.yPos);
        astar(oneDEnemyPos,world);
    }
}
