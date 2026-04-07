package Renderers;

import com.google.gson.Gson;
import java.io.*;

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

            game.curGridX = data.gridX;
            game.curGridY = data.gridY;

            game.currentRoom = game.mapGrid[data.gridX][data.gridY];

            //game.bindCurrentRoomEnemies();

            game.roomRenderer.setActiveRoom(game.currentRoom);

            System.out.println("Game Loaded");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
