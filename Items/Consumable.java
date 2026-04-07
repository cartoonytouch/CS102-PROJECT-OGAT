package Items;

public class Consumable extends Item {

    int restoreAmount;
    String type;

    Consumable(String type)
    {
        this.type = type;

        if(type.equals("Health"))
        {
            this.name = "Health Potion";
            this.itemID = "11";
            this.restoreAmount = 1;
        }
        else if(type.equals("Mana"))
        {
            this.name = "Mana Potion";
            this.itemID = "12";
            this.restoreAmount = 20;
        }

    }
    public int getRestoreAmount() {
        return restoreAmount;
    }
    public String getType() {
        return type;
    }
    // @Override
    // public boolean equals(Object obj) {
    //     // TODO Auto-generated method stub
    //     return super.equals(obj);
    // }
    // public static void main(String[] args) {
        
    //     Consumable c = new Consumable("Health");
    //     System.out.println(c.getName());
    //     System.out.println(c.getType());
    //     System.out.println(c.getItemID());
    //     System.out.println(c.getRestoreAmount());
    // }
    
    
}
