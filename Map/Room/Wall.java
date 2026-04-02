package Map.Room;
public class Wall extends RoomObjects{
    public Wall(int wCoordX, int wCoordY, char wallSide, int type)
    {
        this.coordX = wCoordX;
        this.coordY = wCoordY;
        this.dir = wallSide;
        this.type = type;
    }
}
