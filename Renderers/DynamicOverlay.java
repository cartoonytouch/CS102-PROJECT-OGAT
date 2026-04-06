package Renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Entities.Characters.Player;
import Entities.Characters.Enemies.Enemy;
import HelperClasses.KeyHandler;
import Map.Room.Room;

public class DynamicOverlay extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 5;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 13;
    public final int maxScreenRow = 9;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public Room[][] mapGrid;
    public int curGridX;
    public int curGridY;

    int FPS = 60;

    private final KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    public final Player player = new Player(this, keyH);

    public Room currentRoom;
    public RoomRenderPanel roomRenderer;

    public DynamicOverlay(Room[][] grid, Room startRoom)
    {
        mapGrid = grid;
        curGridX = startRoom.gridX;
        curGridY = startRoom.gridY;
        currentRoom = startRoom;

        if (currentRoom != null)
        {
            roomRenderer = new RoomRenderPanel(currentRoom, tileSize);
            roomRenderer.setSize(screenWidth, screenHeight);
        }

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        player.xCoord = (screenWidth / 2) - (tileSize / 2);
        player.yCoord = (screenHeight / 2) - (tileSize / 2);

        bindCurrentRoomEnemies();
    }

    public void changeRoom(int targetX, int targetY, String direction)
    {
        Room nextRoom = null;

        for (int x = 0; x < mapGrid.length; x++)
        {
            for (int y = 0; y < mapGrid[x].length; y++)
            {
                Room room = mapGrid[x][y];
                if (room != null && room.gridX == targetX && room.gridY == targetY)
                {
                    nextRoom = room;
                }
            }
        }

        if (nextRoom != null)
        {
            curGridX = targetX;
            curGridY = targetY;
            currentRoom = nextRoom;
            roomRenderer.setActiveRoom(currentRoom);
            bindCurrentRoomEnemies();

            int roomPixelWidth = currentRoom.width * tileSize;
            int roomPixelHeight = currentRoom.height * tileSize;

            if (direction.equals("right")) player.xCoord = tileSize + 20;
            if (direction.equals("left")) player.xCoord = roomPixelWidth - (tileSize * 2) - 20;
            if (direction.equals("up")) player.yCoord = roomPixelHeight - (tileSize * 2) - 20;
            if (direction.equals("down")) player.yCoord = tileSize + 20;
        }
        else
        {
            if (direction.equals("right")) player.xCoord -= 20;
            if (direction.equals("left")) player.xCoord += 20;
            if (direction.equals("up")) player.yCoord += 20;
            if (direction.equals("down")) player.yCoord -= 20;
        }
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run()
    {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null)
        {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1)
            {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update()
    {
        player.update();
        if (currentRoom != null)
        {
            currentRoom.checkCleared();
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (roomRenderer != null)
        {
            roomRenderer.paintComponent(g2);
        }

        if (currentRoom != null)
        {
            for (Enemy enemy : currentRoom.localEnemies)
            {
                enemy.draw(g2);
            }
        }

        player.draw(g2);
        g2.dispose();
    }

    private void bindCurrentRoomEnemies()
    {
        if (currentRoom == null)
        {
            return;
        }

        for (Enemy enemy : currentRoom.localEnemies)
        {
            enemy.bindToOverlay(this);
        }
    }
}
