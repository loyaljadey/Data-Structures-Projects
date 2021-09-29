package byow.lab12;

import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 *
 * @author Michael Jia
 */
public class HexWorld {


    private static final int SIZE = 9;
    private static final int WIDTH = SIZE + SIZE + 3*(SIZE+2*(SIZE-1));;
    private static final int HEIGHT = SIZE*2*5;
    private static int constant;


    public static void main(String[] args) {

        // make a space to draw the tiles
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // makes a new world
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        makeTesselation(world, SIZE);

        // draws the world to the screen
        ter.renderFrame(world);
    }

    /**
     * makes the tesselation of all the hexagons
     *
     * @param world the TETile object that keeps track of the world
     * @param size the size of the side length of the hexagon
     *
     * */
    public static void makeTesselation(TETile[][] world, int size) {
        int[][] coords = new int[19][2];
        coords[0][0] = size-1;
        coords[1][0] = size-1;
        coords[2][0] = size-1;
        coords[0][1] = 2*size;
        coords[1][1] = 4*size;
        coords[2][1] = 6*size;

        coords[3][0] = coords[0][0] + 2*size-1;
        coords[4][0] = coords[0][0] + 2*size-1;
        coords[5][0] = coords[0][0] + 2*size-1;
        coords[6][0] = coords[0][0] + 2*size-1;
        coords[3][1] = size;
        coords[4][1] = 3*size;
        coords[5][1] = 5*size;
        coords[6][1] = 7*size;

        coords[7][0] = coords[3][0] + 2*size-1;
        coords[8][0] = coords[3][0] + 2*size-1;
        coords[9][0] = coords[3][0] + 2*size-1;
        coords[10][0] = coords[3][0] + 2*size-1;
        coords[11][0] = coords[3][0] + 2*size-1;
        coords[7][1] = 0;
        coords[8][1] = 2*size;
        coords[9][1] = 4*size;
        coords[10][1] = 6*size;
        coords[11][1] = 8*size;

        coords[12][0] = coords[7][0] + 2*size-1;
        coords[13][0] = coords[7][0] + 2*size-1;
        coords[14][0] = coords[7][0] + 2*size-1;
        coords[15][0] = coords[7][0] + 2*size-1;
        coords[12][1] = size;
        coords[13][1] = 3*size;
        coords[14][1] = 5*size;
        coords[15][1] = 7*size;

        coords[16][0] = coords[12][0] + 2*size-1;
        coords[17][0] = coords[12][0] + 2*size-1;
        coords[18][0] = coords[12][0] + 2*size-1;
        coords[16][1] = 2*size;
        coords[17][1] = 4*size;
        coords[18][1] = 6*size;

        TETile[] biomeType = new TETile[]{Tileset.GRASS, Tileset.WATER, Tileset.FLOWER, Tileset.SAND, Tileset.MOUNTAIN, Tileset.TREE};

        for (int i = 0; i < 19; i++) {
            constant = ((coords[i][1] + 2*SIZE) - 1 + coords[i][1]);
            addHexagon(world, size, biomeType[(int) Math.floor(Math.random() * 5)], coords[i][0], coords[i][1], constant);
            constant = 0;
        }
    }

    /**
     * makes a hexagon of a given size of a type of world and location where to place it
     *
     * starts construction from the bottom left corner of the hexagon
     * prints first
     * recursive calls until the size of the print line is equal to [s + 2*(s-1)]
     *
     * @param world the TETile object that keeps track of the world
     * @param size the size of the side length of the hexagon
     * @param biome the type of world to be created
     * @param xlocation the x location of where to place the hexagon
     * @param ylocation the y location of where to place the location
     * */
    public static void addHexagon(TETile[][] world, int size, TETile biome, int xlocation, int ylocation, int constant) {

        for(int i = 0; i < size; i++) {
            world[xlocation+i][ylocation] = biome;
        }

        if (size < (SIZE + 2*(SIZE-1))) {
            addHexagon(world, size+2, biome, xlocation-1, ylocation+1, constant);
        }

        int reflectY = constant-ylocation;
        for(int i = 0; i < size; i++) {
            world[xlocation+i][reflectY] = biome;
        }

    }

}
