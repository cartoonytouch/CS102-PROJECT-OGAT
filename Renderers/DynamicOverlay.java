package Renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Entities.Characters.Player;
import Entities.Characters.Enemies.Enemy;
import HelperClasses.KeyHandler;
import Map.Room.Room;
import Menus.Game;
import Menus.PauseMenu;

public class DynamicOverlay extends JPanel implements Runnable {
    private static final int MINIMAP_VIEW_RADIUS = 2;
    private static final int MINIMAP_TILE_SIZE = 26;
    private static final int MINIMAP_PADDING = 12;
    private static final int MINIMAP_MARGIN = 18;

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

    public enum GameState {
        RUNNING, PAUSED, GAME_OVER
    }

    private GameState gameState = GameState.RUNNING;
    

    int FPS = 60;

    private final KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    public final Player player = new Player(this, keyH);

    public Room currentRoom;
    public RoomRenderPanel roomRenderer;

    private BufferedImage startRoomSprite;
    private BufferedImage emptyRoomSprite;
    private BufferedImage enemyRoomSprite;
    private BufferedImage bossRoomSprite;
    private BufferedImage shopRoomSprite;

    public DynamicOverlay(Room[][] grid, Room startRoom)
    {
        this.mapGrid = grid;
        this.curGridX = startRoom.gridX;
        this.curGridY = startRoom.gridY;
        this.currentRoom = startRoom;

        if (this.currentRoom != null)
        {
            this.roomRenderer = new RoomRenderPanel(this.currentRoom, tileSize);
            this.roomRenderer.setSize(screenWidth, screenHeight);
        }

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        loadMinimapAssets();

        player.xCoord = (screenWidth / 2) - (tileSize / 2);
        player.yCoord = (screenHeight / 2) - (tileSize / 2);

        bindCurrentRoomEnemies();
        revealRooms();
    }

    public void changeRoom(int targetX, int targetY, String direction)
    {
        Room nextRoom = null;

        for (int i = 0; i < mapGrid.length; i++)
        {
            for (int j = 0; j < mapGrid[i].length; j++)
            {
                Room room = mapGrid[i][j];
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
            revealRooms();

            int roomPixelWidth = currentRoom.width * tileSize;
            int roomPixelHeight = currentRoom.height * tileSize;

            if (direction.equals("right")) player.xCoord = tileSize + 20;
            if (direction.equals("left")) player.xCoord = roomPixelWidth - (tileSize * 2) - 20;
            if (direction.equals("up")) player.yCoord = roomPixelHeight - (tileSize * 2) - 20;
            if (direction.equals("down")) player.yCoord = tileSize + 20;
        }
        else
        {
            System.out.println("Borders. Theer is no door here.");
            if (direction.equals("right")) player.xCoord -= 20;
            if (direction.equals("left")) player.xCoord += 20;
            if (direction.equals("up")) player.yCoord += 20;
            if (direction.equals("down")) player.yCoord -= 20;
        }
    }

    public void revealRooms()
    {
        if (currentRoom == null)
        {
            return;
        }

        for (int i = 0; i < mapGrid.length; i++)
        {
            for (int j = 0; j < mapGrid[i].length; j++)
            {
                Room room = mapGrid[i][j];
                if (room != null)
                {
                    int distance = Math.abs(room.gridX - currentRoom.gridX) + Math.abs(room.gridY - currentRoom.gridY);
                    if (distance <= 1)
                    {
                        room.isDiscovered = true;
                    }
                }
            }
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
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null)
        {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1)
            {
                update();
                repaint();
                drawCount++;
                delta--;
            }

            if (timer >= 1000000000)
            {
                // System.out.println("FPS" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update()
{

    if (keyH.escPressed) {
        keyH.escPressed = false; // Reset so it doesn't flicker
        pauseGame();
        Game.switchMenu(new PauseMenu(this));
        return;
    }

    if (gameState != GameState.RUNNING) return;

    player.update();

    if (currentRoom != null)
    {
        bindCurrentRoomEnemies();
        currentRoom.checkCleared();

        for (Enemy enemy : currentRoom.localEnemies)
        {
            enemy.update();
        }
    }
}

    public void drawMinimap(Graphics2D g2)
    {
        if (currentRoom == null || mapGrid == null)
        {
            return;
        }

        Graphics2D miniG = (Graphics2D) g2.create();
        miniG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int viewDiameter = (MINIMAP_VIEW_RADIUS * 2) + 1;
        int viewportSize = viewDiameter * MINIMAP_TILE_SIZE;
        int frameWidth = viewportSize + (MINIMAP_PADDING * 2);
        int frameHeight = viewportSize + (MINIMAP_PADDING * 2);
        int frameX = MINIMAP_MARGIN;
        int frameY = screenHeight - frameHeight - MINIMAP_MARGIN;
        int innerX = frameX + MINIMAP_PADDING;
        int innerY = frameY + MINIMAP_PADDING;

        miniG.setColor(new Color(193, 166, 104, 255));
        miniG.setStroke(new BasicStroke(3f));
        miniG.drawRoundRect(frameX, frameY, frameWidth, frameHeight, 18, 18);

        Shape oldClip = miniG.getClip();
        miniG.setClip(innerX, innerY, viewportSize, viewportSize);

        for (int y = currentRoom.gridY - MINIMAP_VIEW_RADIUS; y <= currentRoom.gridY + MINIMAP_VIEW_RADIUS; y++)
        {
            for (int x = currentRoom.gridX - MINIMAP_VIEW_RADIUS; x <= currentRoom.gridX + MINIMAP_VIEW_RADIUS; x++)
            {
                if (x < 0 || y < 0 || x >= mapGrid.length || y >= mapGrid[x].length)
                {
                    continue;
                }

                Room room = mapGrid[x][y];
                if (room == null || !room.isDiscovered)
                {
                    continue;
                }

                int relativeX = x - currentRoom.gridX;
                int relativeY = y - currentRoom.gridY;
                int drawX = innerX + (relativeX + MINIMAP_VIEW_RADIUS) * MINIMAP_TILE_SIZE;
                int drawY = innerY + (relativeY + MINIMAP_VIEW_RADIUS) * MINIMAP_TILE_SIZE;

                drawMinimapRoom(miniG, room, drawX, drawY, MINIMAP_TILE_SIZE);

                if (room == currentRoom)
                {
                    miniG.setColor(new Color(255, 234, 145, 255));
                    miniG.setStroke(new BasicStroke(2.25f));
                    miniG.drawRect(drawX, drawY, MINIMAP_TILE_SIZE - 1, MINIMAP_TILE_SIZE - 1);
                }
            }
        }

        miniG.setClip(oldClip);
        miniG.dispose();
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
        drawMinimap(g2);
        g2.dispose();
    }

    public void bindCurrentRoomEnemies()
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

    private void loadMinimapAssets()
    {
        try
        {
            startRoomSprite = ImageIO.read(new File("Assets/MapAssets/startRoom.png"));
            emptyRoomSprite = ImageIO.read(new File("Assets/MapAssets/emptyRoom.png"));
            enemyRoomSprite = ImageIO.read(new File("Assets/MapAssets/enemyRoom.png"));
            bossRoomSprite = ImageIO.read(new File("Assets/MapAssets/bossRoom.png"));
            shopRoomSprite = ImageIO.read(new File("Assets/MapAssets/shopRoom.png"));
        }
        catch (IOException e)
        {
            System.out.println("minimap asset failure: " + e.getMessage());
        }
    }

    private void drawMinimapRoom(Graphics2D g2, Room room, int drawX, int drawY, int size)
    {
        BufferedImage sprite = null;

        switch (room.type)
        {
            case "Start":
                sprite = startRoomSprite;
                break;
            case "Enemy":
                sprite = enemyRoomSprite;
                break;
            case "Boss":
                sprite = bossRoomSprite;
                break;
            case "Shop":
                sprite = shopRoomSprite;
                break;
            default:
                sprite = emptyRoomSprite;
                break;
        }

        if (sprite != null)
        {
            g2.drawImage(sprite, drawX, drawY, size, size, null);
        }
        else
        {
            g2.setColor(new Color(120, 120, 120, 220));
            g2.fillRect(drawX, drawY, size, size);
        }
    }

    public void pauseGame() {
        gameState = GameState.PAUSED;
    }

    public void resumeGame() {
        gameState = GameState.RUNNING;
    }

    public void gameOver() {
        gameState = GameState.GAME_OVER;
    }

    public void saveGame()
    {
        SaveSystem.save(this);
    }

    public void loadGame()
    {
        SaveSystem.load(this);
    }
}
