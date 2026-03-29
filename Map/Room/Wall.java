package Map.Room;
public class Wall extends RoomObjects{
    int coordX;
    int coordY;
    String side;
    
    public Wall(int wCoordX, int wCoordY)
    {
        this.coordX = wCoordX;
        this.coordY = wCoordY;
    }
}
