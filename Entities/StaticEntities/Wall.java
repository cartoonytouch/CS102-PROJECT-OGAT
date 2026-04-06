package Entities.StaticEntities;

public class Wall extends StaticEntity {
    public Wall(int wCoordX, int wCoordY, char wallSide, int wallType)
    {
        coordX = wCoordX;
        coordY = wCoordY;
        dir = wallSide;
        type = wallType;
    }
}
