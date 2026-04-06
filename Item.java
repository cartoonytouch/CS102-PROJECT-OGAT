
public abstract class Item {

    String itemID;
    String name;
    Player player;

    public String getItemID() {
        return itemID;
    }
    void onPickup()
    {
        if (player != null && player.getInventory() != null)
        {
            player.getInventory().add(this);
        }

    }
    public Player getPlayer() {
        return player;
    }
    public String getName() {
        return name;
    }
    
}
