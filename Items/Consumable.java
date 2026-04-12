package Items;

public class Consumable extends Item {

    public Consumable(String id)
    {
        this.itemID = id;

        if (id.equals("11"))
        {
            this.name = "Health Consumable";
        }
        else if (id.equals("12"))
        {
            this.name = "Mana Consumable";
        }
        else if (id.equals("13"))
        {
            this.name = "Attack Consumable";
        }
        else if (id.equals("14"))
        {
            this.name = "Health Regeneration Consumable";
        }
        else if (id.equals("15"))
        {
            this.name = "Speed Consumable";
        }
    }

    public void update()
    {}
}
