public class Inventory {
    
    Item[] items;

    Inventory()
    {
        items = new Item[5];

    }
    
    void add(Item item)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] == null)
            {
                items[i] = item;
                return;
            }
        }

        System.out.println("Inventory Full!");
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


