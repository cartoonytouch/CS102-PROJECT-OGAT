package Entities.StaticEntities;

import java.util.Random;

import Entities.FloorObjects;

public class Tile extends FloorObjects {

    public int coordX;
    public int coordY;
    public int tileType;
    public char dir;

    public Tile(int tCoordX, int tCoordY, int tileVersion, Random rng)
    {
        this.coordX = tCoordX;
        this.coordY = tCoordY;
        this.tileType = tileVersion;

        switch (rng.nextInt(4))
        {
            case 0: dir = 'n'; break;
            case 1: dir = 'e'; break;
            case 2: dir = 's'; break;
            default: dir = 'w'; break;
        }
    }
}

