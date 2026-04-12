package Items;

public class Passive extends Item {

    public Passive(String id)
    {
        this.itemID = id;

        if (id.equals("21"))
        {
            this.name = "Inventory Passive";
        }
        else if (id.equals("22"))
        {
            this.name = "Strength Passive";
        }
        else if (id.equals("23"))
        {
            this.name = "Attack Speed Passive";
        }
        else if (id.equals("24"))
        {
            this.name = "Strength Passive";
        }
        else if (id.equals("25"))
        {
            this.name = "Speed Passive";
        }
    }

    public void update()
    {
        
    }
}
