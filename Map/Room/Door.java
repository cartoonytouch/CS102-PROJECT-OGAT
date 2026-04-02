package Map.Room;

public class Door extends RoomObjects{
    public boolean open;
    public Door(int dCoordX, int dCoordY, int direction)
    {
        coordX = dCoordX;
        coordY = dCoordY;
        open = false;
    }

    public Door(int dCoordX, int dCoordY, char dir)
    {
        coordX = dCoordX;
        coordY = dCoordY;
        open = false;
        this.dir = dir;
    }

    public void openDoor()
    {
        open = true;
    }
}
