package Renderers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import Items.Consumable;
import Items.Inventory;
import Items.Item;
import Items.ItemCatalog;
import Items.Passive;
import Items.Weapons.Weapon;

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
                data.inventoryWeaponLevels = new int[items.length];
                for (int i = 0; i < items.length; i++)
                {
                    data.inventoryItemIds[i] = (items[i] != null) ? items[i].getItemID() : null;
                    if (items[i] instanceof Weapon)
                    {
                        data.inventoryWeaponLevels[i] = ((Weapon) items[i]).getUpgradeLevel();
                    }
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
                    Item item = ItemCatalog.createItem(itemId);
                    if (item != null)
                    {
                        item.setPlayer(game.player);
                        game.player.getInventory().add(item);
                    }
                }

                if (data.inventoryWeaponLevels != null)
                {
                    Item[] loadedItems = game.player.getInventory().getItems();
                    for (int i = 0; i < loadedItems.length && i < data.inventoryWeaponLevels.length; i++)
                    {
                        if (loadedItems[i] instanceof Weapon)
                        {
                            ((Weapon) loadedItems[i]).setUpgradeLevel(data.inventoryWeaponLevels[i]);
                        }
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

}
