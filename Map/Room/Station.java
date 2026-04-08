package Map.Room;

import java.awt.Rectangle;

import Entities.Characters.Player;
import Entities.StaticEntities.StaticEntity;

public abstract class Station extends StaticEntity {

    private final String stationName;

    protected Station(int coordX, int coordY, String stationName)
    {
        this.coordX = coordX;
        this.coordY = coordY;
        this.dir = 'n';
        this.stationName = stationName;
    }

    public String getStationName()
    {
        return stationName;
    }

    public Rectangle getBounds(int tileSize)
    {
        return new Rectangle(coordX * tileSize, coordY * tileSize, tileSize, tileSize);
    }

    public boolean isPlayerNearby(Player player, int tileSize)
    {
        if (player == null || player.getSolidArea() == null)
        {
            return false;
        }

        Rectangle interactionArea = getBounds(tileSize);
        interactionArea.grow(tileSize, tileSize);
        return interactionArea.intersects(player.getSolidArea());
    }

    public String getInteractionText()
    {
        return "Press E to use " + stationName;
    }

    public abstract String interact(Player player);
}
