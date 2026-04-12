package Renderers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import Entities.Characters.Enemies.Boss;
import Entities.Characters.Enemies.Enemy;
import Entities.Characters.Enemies.LongRangeEnemy;
import Entities.Characters.Enemies.MeleeEnemy;
import Items.Inventory;
import Items.Item;
import Items.ItemCatalog;
import Items.Weapons.Weapon;
import Map.Room.BuyStation;
import Map.Room.RewardDrop;
import Map.Room.Room;
import Map.Room.Station;

public class SaveSystem {

    private static final String SAVE_FILE = "save.json";
    private static final Gson GSON = new Gson();

    public static void save(DynamicOverlay game)
    {
        if (game == null || game.player == null)
        {
            return;
        }

        try (FileWriter writer = new FileWriter(SAVE_FILE))
        {
            GSON.toJson(buildGameData(game), writer);
            System.out.println("Game Saved");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void load(DynamicOverlay game)
    {
        GameData data = readSaveData();
        if (game == null || data == null)
        {
            return;
        }

        applySaveData(game, data);
        System.out.println("Game Loaded");
    }

    public static GameData readSaveData()
    {
        if (!hasSaveFile())
        {
            return null;
        }

        try (FileReader reader = new FileReader(SAVE_FILE))
        {
            return GSON.fromJson(reader, GameData.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasSaveFile()
    {
        return Files.isRegularFile(Path.of(SAVE_FILE));
    }

    public static void clearSave()
    {
        try
        {
            Files.deleteIfExists(Path.of(SAVE_FILE));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static GameData buildGameData(DynamicOverlay game)
    {
        GameData data = new GameData();

        data.mapSeed = game.getMapSeed();
        data.playerX = game.player.xCoord;
        data.playerY = game.player.yCoord;
        data.playerHealth = game.player.health;
        data.playerMana = game.player.mana;
        data.playerCurrency = game.player.currency;
        data.playerClass = game.player.getPlayerClass();
        data.difficulty = game.player.getDifficulty();
        data.discoveredItemIds = game.player.getDiscoveredItemIds().toArray(new String[0]);

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
        data.rooms = serializeRooms(game);

        return data;
    }

    private static GameData.RoomState[] serializeRooms(DynamicOverlay game)
    {
        List<GameData.RoomState> roomStates = new ArrayList<>();
        Room[][] mapGrid = game.mapGrid;

        if (mapGrid == null)
        {
            return new GameData.RoomState[0];
        }

        for (int x = 0; x < mapGrid.length; x++)
        {
            for (int y = 0; y < mapGrid[x].length; y++)
            {
                Room room = mapGrid[x][y];
                if (room == null)
                {
                    continue;
                }

                GameData.RoomState roomState = new GameData.RoomState();
                roomState.gridX = room.gridX;
                roomState.gridY = room.gridY;
                roomState.isDiscovered = room.isDiscovered;
                roomState.isCleared = room.isCleared();
                roomState.enemies = serializeEnemies(room.localEnemies, game.tileSize);
                roomState.rewardDrop = serializeReward(room.getRewardDrop());
                roomState.buyStation = serializeBuyStation(room.placedStations);
                roomStates.add(roomState);
            }
        }

        return roomStates.toArray(new GameData.RoomState[0]);
    }

    private static GameData.EnemyState[] serializeEnemies(List<Enemy> enemies, int tileSize)
    {
        if (enemies == null || enemies.isEmpty())
        {
            return new GameData.EnemyState[0];
        }

        List<GameData.EnemyState> enemyStates = new ArrayList<>();
        for (Enemy enemy : enemies)
        {
            if (enemy == null)
            {
                continue;
            }

            GameData.EnemyState enemyState = new GameData.EnemyState();
            enemyState.enemyType = getEnemyType(enemy);
            if (enemy.overlay == null)
            {
                enemyState.xCoord = enemy.getSpawnGridX() * tileSize;
                enemyState.yCoord = enemy.getSpawnGridY() * tileSize;
            }
            else
            {
                enemyState.xCoord = enemy.xCoord;
                enemyState.yCoord = enemy.yCoord;
            }
            enemyState.health = enemy.health;
            enemyStates.add(enemyState);
        }

        return enemyStates.toArray(new GameData.EnemyState[0]);
    }

    private static String getEnemyType(Enemy enemy)
    {
        if (enemy instanceof Boss)
        {
            return "Boss";
        }
        if (enemy instanceof LongRangeEnemy)
        {
            return "LongRangeEnemy";
        }
        return "MeleeEnemy";
    }

    private static GameData.RewardState serializeReward(RewardDrop rewardDrop)
    {
        if (rewardDrop == null)
        {
            return null;
        }

        GameData.RewardState rewardState = new GameData.RewardState();
        rewardState.coordX = rewardDrop.getCoordX();
        rewardState.coordY = rewardDrop.getCoordY();
        rewardState.rewardType = rewardDrop.getRewardType().name();
        rewardState.coinAmount = rewardDrop.getCoinAmount();
        rewardState.itemId = (rewardDrop.getItemReward() != null) ? rewardDrop.getItemReward().getItemID() : null;
        return rewardState;
    }

    private static GameData.BuyStationState serializeBuyStation(List<Station> stations)
    {
        if (stations == null)
        {
            return null;
        }

        for (Station station : stations)
        {
            if (station instanceof BuyStation)
            {
                BuyStation buyStation = (BuyStation) station;
                if (!buyStation.hasGeneratedOffers())
                {
                    return null;
                }

                GameData.BuyStationState buyStationState = new GameData.BuyStationState();
                buyStationState.offerItemIds = buyStation.getOfferItemIds();
                buyStationState.offerPrices = buyStation.getOfferPrices();
                return buyStationState;
            }
        }

        return null;
    }

    private static void applySaveData(DynamicOverlay game, GameData data)
    {
        restorePlayer(game, data);
        restoreRooms(game, data.rooms);

        game.curGridX = data.gridX;
        game.curGridY = data.gridY;
        if (isValidGrid(game.mapGrid, data.gridX, data.gridY) && game.mapGrid[data.gridX][data.gridY] != null)
        {
            game.currentRoom = game.mapGrid[data.gridX][data.gridY];
        }

        game.bindCurrentRoomEnemies();
        game.roomRenderer.setActiveRoom(game.currentRoom);
        game.revealRooms();
    }

    private static void restorePlayer(DynamicOverlay game, GameData data)
    {
        game.player.xCoord = data.playerX;
        game.player.yCoord = data.playerY;
        game.player.health = data.playerHealth;
        game.player.mana = data.playerMana;
        game.player.currency = data.playerCurrency;

        if (data.playerClass != null && !data.playerClass.isBlank())
        {
            game.player.playerClass = data.playerClass;
        }

        game.player.inventory = new Inventory();
        Item[] loadedItems = game.player.getInventory().getItems();
        if (data.inventoryItemIds != null)
        {
            for (int i = 0; i < loadedItems.length && i < data.inventoryItemIds.length; i++)
            {
                Item item = ItemCatalog.createItem(data.inventoryItemIds[i]);
                if (item == null)
                {
                    continue;
                }

                item.setPlayer(game.player);
                loadedItems[i] = item;

                if (item instanceof Weapon && data.inventoryWeaponLevels != null && i < data.inventoryWeaponLevels.length)
                {
                    ((Weapon) item).setUpgradeLevel(data.inventoryWeaponLevels[i]);
                }
            }
        }

        game.player.getInventory().setChoosedWeaponIndex(data.choosedWeaponIndex);
        game.player.restoreDiscoveredItemIds(data.discoveredItemIds);
        game.player.refreshInventoryState();
    }

    private static void restoreRooms(DynamicOverlay game, GameData.RoomState[] roomStates)
    {
        if (roomStates == null || game.mapGrid == null)
        {
            return;
        }

        for (GameData.RoomState roomState : roomStates)
        {
            if (roomState == null || !isValidGrid(game.mapGrid, roomState.gridX, roomState.gridY))
            {
                continue;
            }

            Room room = game.mapGrid[roomState.gridX][roomState.gridY];
            if (room == null)
            {
                continue;
            }

            room.isDiscovered = roomState.isDiscovered;
            room.localEnemies.clear();
            restoreEnemies(game, room, roomState.enemies);
            room.setRewardDrop(restoreReward(roomState.rewardDrop));
            room.setCleared(roomState.isCleared);
            restoreBuyStation(room.placedStations, roomState.buyStation);
        }
    }

    private static void restoreEnemies(DynamicOverlay game, Room room, GameData.EnemyState[] enemyStates)
    {
        if (enemyStates == null)
        {
            return;
        }

        for (GameData.EnemyState enemyState : enemyStates)
        {
            Enemy enemy = createEnemyFromState(enemyState, game, room);
            if (enemy != null)
            {
                room.localEnemies.add(enemy);
            }
        }
    }

    private static Enemy createEnemyFromState(GameData.EnemyState enemyState, DynamicOverlay game, Room room)
    {
        if (enemyState == null || enemyState.health <= 0)
        {
            return null;
        }

        int gridX = Math.max(1, Math.min(room.width - 2, enemyState.xCoord / game.tileSize));
        int gridY = Math.max(1, Math.min(room.height - 2, enemyState.yCoord / game.tileSize));

        Enemy enemy;
        if ("Boss".equals(enemyState.enemyType))
        {
            enemy = new Boss(enemyState.health, gridX, gridY);
        }
        else if ("LongRangeEnemy".equals(enemyState.enemyType))
        {
            enemy = new LongRangeEnemy(gridX, gridY);
        }
        else
        {
            enemy = new MeleeEnemy(gridX, gridY);
        }

        enemy.bindToOverlay(game);
        enemy.xCoord = enemyState.xCoord;
        enemy.yCoord = enemyState.yCoord;
        enemy.health = enemyState.health;
        return enemy;
    }

    private static RewardDrop restoreReward(GameData.RewardState rewardState)
    {
        if (rewardState == null)
        {
            return null;
        }

        if ("ITEM".equals(rewardState.rewardType))
        {
            Item rewardItem = ItemCatalog.createItem(rewardState.itemId);
            if (rewardItem != null)
            {
                return new RewardDrop(rewardState.coordX, rewardState.coordY, rewardItem);
            }
        }

        return new RewardDrop(rewardState.coordX, rewardState.coordY, Math.max(1, rewardState.coinAmount));
    }

    private static void restoreBuyStation(List<Station> stations, GameData.BuyStationState buyStationState)
    {
        if (stations == null || buyStationState == null)
        {
            return;
        }

        for (Station station : stations)
        {
            if (station instanceof BuyStation)
            {
                ((BuyStation) station).restoreOffers(buyStationState.offerItemIds, buyStationState.offerPrices);
                return;
            }
        }
    }

    private static boolean isValidGrid(Room[][] mapGrid, int gridX, int gridY)
    {
        return mapGrid != null  
            && gridX >= 0
            && gridY >= 0
            && gridX < mapGrid.length
            && gridY < mapGrid[gridX].length;
    }
}
