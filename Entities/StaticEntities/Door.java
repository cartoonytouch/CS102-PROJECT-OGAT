package Entities.StaticEntities;

public class Door extends StaticEntity {
    public boolean open;

    public Door(int dCoordX, int dCoordY, int direction)
    {
        this(dCoordX, dCoordY, directionToDir(direction));
    }

    public Door(int dCoordX, int dCoordY, char doorDir)
    {
        coordX = dCoordX;
        coordY = dCoordY;
        dir = doorDir;
        open = false;
    }

    public void openDoor()
    {
        open = true;
    }

    private static char directionToDir(int direction)
    {
        switch (direction)
        {
            case 0: return 'n';
            case 1: return 'e';
            case 2: return 's';
            case 3: return 'w';
            default: return 'n';
        }
    }
}
