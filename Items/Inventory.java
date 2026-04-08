package Items;

import Entities.Characters.Player;
import Items.Weapons.Weapon;

public class Inventory {

    private int choosedWeaponIndex;
    private final Item[] items;

    public Inventory()
    {
        items = new Item[Player.inventoryLimit];
    }

    public void add(Item item)
    {
        if (item == null)
        {
            return;
        }

        if (!canAdd(item))
        {
            System.out.println("Inventory cannot accept " + item.getName() + ".");
            return;
        }

        if (item instanceof Weapon)
        {
            if (items[0] == null)
            {
                items[0] = item;
            }
            else if (items[1] == null)
            {
                items[1] = item;
            }
            else
            {
                System.out.println("Inventory Full!");
            }
            return;
        }

        if (item instanceof Consumable)
        {
            if (items[2] == null)
            {
                items[2] = item;
            }
            else
            {
                System.out.println("Consumable slot occupied!");
            }
            return;
        }

        if (item instanceof Passive)
        {
            if (items[3] == null)
            {
                items[3] = item;
            }
            else
            {
                System.out.println("Passive slot occupied!");
            }
            return;
        }

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

    public boolean canAdd(Item item)
    {
        if (item == null)
        {
            return false;
        }

        if (item instanceof Weapon)
        {
            if (containsItemId(item.getItemID()))
            {
                return false;
            }
            return items[0] == null || items[1] == null;
        }

        if (item instanceof Consumable)
        {
            return items[2] == null;
        }

        if (item instanceof Passive)
        {
            if (containsItemId(item.getItemID()))
            {
                return false;
            }
            return items[3] == null;
        }

        for (Item inventoryItem : items)
        {
            if (inventoryItem == null)
            {
                return true;
            }
        }

        return false;
    }

    public void remove(Item item)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] == item)
            {
                items[i] = null;
                if (choosedWeaponIndex == i)
                {
                    if (items[0] instanceof Weapon)
                    {
                        choosedWeaponIndex = 0;
                    }
                    else if (items[1] instanceof Weapon)
                    {
                        choosedWeaponIndex = 1;
                    }
                    else
                    {
                        choosedWeaponIndex = 0;
                    }
                }
                return;
            }
        }
    }

    public Item[] getItems()
    {
        return items;
    }

    public int getChoosedWeaponIndex()
    {
        return choosedWeaponIndex;
    }

    public void setChoosedWeaponIndex(int choosedWeaponIndex)
    {
        if (choosedWeaponIndex >= 0 && choosedWeaponIndex <= 1)
        {
            this.choosedWeaponIndex = choosedWeaponIndex;
        }
    }

    public void switchWeapon()
    {
        if (choosedWeaponIndex == 0 && items[1] instanceof Weapon)
        {
            choosedWeaponIndex = 1;
        }
        else if (choosedWeaponIndex == 1 && items[0] instanceof Weapon)
        {
            choosedWeaponIndex = 0;
        }
    }

    public boolean containsItemId(String itemId)
    {
        if (itemId == null || itemId.isBlank())
        {
            return false;
        }

        for (Item inventoryItem : items)
        {
            if (inventoryItem != null && itemId.equals(inventoryItem.getItemID()))
            {
                return true;
            }
        }

        return false;
    }
}
