package Map.Room;
import java.util.Random;

public class Tile extends FloorObjects{

    public int coordX;
    public int coordY;
    public int tileType;
    public char dir;
    private Random rng;
    
    public Tile(int tCoordX, int tCoordY, int tileVersion, Random sharedRng)
    {
        this.rng = sharedRng;
        this.coordX = tCoordX;
        this.coordY = tCoordY;
        this.tileType = tileVersion;
        int dirInt = rng.nextInt(4);
        switch (dirInt)
        { 
            case 0: dir = 'n'; break;
            case 1: dir = 's'; break;
            case 2: dir = 'e'; break;
            case 3: dir = 'w'; break;
        }
    }
}
