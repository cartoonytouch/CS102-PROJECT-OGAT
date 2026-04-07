package Items;

import Entities.Characters.Player;

public abstract class Item {

    protected String itemID;
    protected String name;
    protected Player player;

    public String getItemID()
    {
        return itemID;
    }

    public String getName()
    {
        return name;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }
}
