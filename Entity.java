import java.awt.Rectangle;

import Map.Room.RoomObjects;

public abstract class Entity extends RoomObjects {

    public int xCoord;
    public int yCoord;
    public int spped;

    public Rectangle solidArea;

    public abstract boolean checkCollision();
}
