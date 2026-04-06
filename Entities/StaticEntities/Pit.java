package Entities.StaticEntities;

import Entities.FloorObjects;

public class Pit extends FloorObjects{
    public Pit(int pCoordX, int pCoordY)
    {
        coordX = pCoordX;
        coordY = pCoordY;
    }

    public int coordX;
    public int coordY;
}
