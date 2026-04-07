package Items;
import Items.Weapons.*;

import Entities.Characters.Player;


public class Inventory {
    
    Item[] items;

    public Inventory()
    {
        items = new Item[Player.inventoryLimit];

    }
    
    void add(Item item)
    {
        int counter = 0;

        for(Item i : this.getItems())
        {
            if(i != null)
            {
                counter++;
            }
        }
        if(counter >= this.getItems().length - 1)
        {
            System.out.println("Inventory Full!");
            return;
        }
        if(item instanceof Weapon)
        {
            if(this.items[0] == null || this.items[1] == null)
            {
                if(this.items[0] == null)
                {
                    this.items[0] = item;
                }
                else
                {
                    this.items[1] = item;
                }

            }
        }
        else if(item instanceof Consumable)
        {
            this.items[2] = item;
        }
        else if(item instanceof Passive)
        {
            this.items[3] = item;
        }
    }
    void remove(Item item){
    

        for(int i = 0; i < this.items.length; i++)
        {
            if(this.items[i].equals(item))
            {
                items[i] = null;
                return;
            }
        }
    }

    public Item[] getItems() {
        return items;
    }
}


