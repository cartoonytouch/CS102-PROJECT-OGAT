package Renderers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import Items.Consumable;
import Items.Inventory;
import Items.Item;
import Items.Passive;
import Items.Weapons.GreatAxe;
import Items.Weapons.Halberd;
import Items.Weapons.Hammer;
import Items.Weapons.Longsword;
import Items.Weapons.Spear;
import Items.Weapons.Sword;

public class SaveSystem {

    private static final String SAVE_FILE = "save.json";

    public static void save(DynamicOverlay game)
    {
        try
        {
            Gson gson = new Gson();
            GameData data = new GameData();

            data.playerX = game.player.xCoord;
            data.playerY = game.player.yCoord;
            data.playerHealth = game.player.health;
            data.playerMana = game.player.mana;
            data.playerCurrency = game.player.currency;
            data.playerClass = game.player.getPlayerClass();

            if (game.player.getInventory() != null)
            {
                Item[] items = game.player.getInventory().getItems();
                data.inventoryItemIds = new String[items.length];
                for (int i = 0; i < items.length; i++)
                {
                    data.inventoryItemIds[i] = (items[i] != null) ? items[i].getItemID() : null;
                }
                data.choosedWeaponIndex = game.player.getInventory().getChoosedWeaponIndex();
            }

            data.gridX = game.curGridX;
            data.gridY = game.curGridY;

            FileWriter writer = new FileWriter(SAVE_FILE);
            gson.toJson(data, writer);
            writer.close();

            System.out.println("Game Saved");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void load(DynamicOverlay game)
    {
        try
        {
            Gson gson = new Gson();
            FileReader reader = new FileReader(SAVE_FILE);

            GameData data = gson.fromJson(reader, GameData.class);
            reader.close();

            game.player.xCoord = data.playerX;
            game.player.yCoord = data.playerY;
            game.player.health = data.playerHealth;
            game.player.mana = data.playerMana;
            game.player.currency = data.playerCurrency;

            if (data.playerClass != null && !data.playerClass.isBlank())
            {
                game.player.playerClass = data.playerClass;
            }

            if (data.inventoryItemIds != null)
            {
                game.player.inventory = new Inventory();
                for (String itemId : data.inventoryItemIds)
                {
                    Item item = createItem(itemId);
                    if (item != null)
                    {
                        item.setPlayer(game.player);
                        game.player.getInventory().add(item);
                    }
                }
                game.player.getInventory().setChoosedWeaponIndex(data.choosedWeaponIndex);
                game.player.appliedPassiveID = "";
                game.player.updatePassiveEffect();
            }

            game.curGridX = data.gridX;
            game.curGridY = data.gridY;

            game.currentRoom = game.mapGrid[data.gridX][data.gridY];

            game.bindCurrentRoomEnemies();
            game.roomRenderer.setActiveRoom(game.currentRoom);
            game.revealRooms();

            System.out.println("Game Loaded");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static Item createItem(String itemId)
    {
        if (itemId == null || itemId.isBlank())
        {
            return null;
        }

        if (itemId.equals("31"))
        {
            return new Sword();
        }
        if (itemId.equals("32"))
        {
            return new Spear();
        }
        if (itemId.equals("33"))
        {
            return new Hammer();
        }
        if (itemId.equals("34"))
        {
            return new Longsword();
        }
        if (itemId.equals("35"))
        {
            return new Halberd();
        }
        if (itemId.equals("36"))
        {
            return new GreatAxe();
        }
        if (itemId.startsWith("2"))
        {
            return new Passive(itemId);
        }
        if (itemId.startsWith("1"))
        {
            return new Consumable(itemId);
        }

        return null;
    }
}
