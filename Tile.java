public class Tile extends FloorObjects{

    int coordX;
    int coordY;
    String tileType;
    
    public Tile(int tCoordX, int tCoordY, String roomType)
    {
        this.coordX = tCoordX;
        this.coordY = tCoordY;
        this.tileType = roomType;
    }
}
