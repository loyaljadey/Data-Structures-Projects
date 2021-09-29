package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class World {


    /** INSTANCE VARIABLES */
    Long seed;
    TETile[][] world;
    Random RANDOM;


    /** CONSTRUCTOR */
    //construct the world here
    public World(Long seed, int width, int height) {
        this.seed = seed;
        RANDOM = new Random(seed);

        // makes a new world
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }



        //make an initial room
        makeRooms(RANDOM.nextInt(width), RANDOM.nextInt(height), RANDOM.nextInt(width), RANDOM.nextInt(height));
        //makes somewhere between 6-20 rooms
        for (int i = 0; i < RandomUtils.uniform(RANDOM, 6, 20); i++) {
            //make the room
            makeRooms(RANDOM.nextInt(width), RANDOM.nextInt(height), RANDOM.nextInt(width), RANDOM.nextInt(height));
            //connect the rooms with hallway
        }


    }


    /** HELPER METHODS */

    //create the rooms
    public void makeRooms(int xstart, int ystart, int width, int height) {

        if(!world[xstart][ystart].equals(Tileset.FLOOR)) {

        }

    }



    //create the paths of the hallways
    public void makeHalls(int xstart1, int ystart1, int xstart2, int ystart2) {

    }




    /** GRABBER METHODS */
    public Long getSeed() {
        return seed;
    }

    public TETile[][] getWorld() {
        return world;
    }

}
