package Entities.StaticEntities;

import java.util.Random;

import Entities.FloorObjects;

public class Tile extends FloorObjects {
    public int coordX;
    public int coordY;
    public int tileType;
    public char dir;

    public Tile(int tCoordX, int tCoordY, int tileVersion, Random sharedRng)
    {
        coordX = tCoordX;
        coordY = tCoordY;
        tileType = tileVersion;

        switch (sharedRng.nextInt(4))
        {
            case 0:
                dir = 'n';
                break;
            case 1:
                dir = 's';
                break;
            case 2:
                dir = 'e';
                break;
            default:
                dir = 'w';
                break;
        }
    }
}
