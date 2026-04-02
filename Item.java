
public abstract class Item {

    String itemID;
    String name;
    Player player;

    public String getItemID() {
        return itemID;
    }
    void onPickup()
    {
        
        this.player.getInventory().add(this);

    }
    public Player getPlayer() {
        return player;
    }
    public String getName() {
        return name;
    }
    
}