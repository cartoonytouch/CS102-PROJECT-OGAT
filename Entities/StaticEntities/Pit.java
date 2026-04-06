package Entities.StaticEntities;

import Entities.FloorObjects;

public class Pit extends FloorObjects {
    public int coordX;
    public int coordY;

    public Pit(int pCoordX, int pCoordY)
    {
        coordX = pCoordX;
        coordY = pCoordY;
    }
}
