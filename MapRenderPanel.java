import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MapRenderPanel extends JPanel {
    private Room[][] grid;
    private final int ROOM_WIDTH = 80;
    private final int ROOM_HEIGHT = 80;

    private BufferedImage startRoomSprite;
    private BufferedImage emptyRoomSprite;
    private BufferedImage enemyRoomSprite;
    private BufferedImage bossRoomSprite;
    private BufferedImage shopRoomSprite;

    private int minX, maxX, minY, maxY;

    public MapRenderPanel(Room[][] activeGrid) {
        try
        {
            startRoomSprite = ImageIO.read(new File("startRoom.png"));
            emptyRoomSprite = ImageIO.read(new File("emptyRoom.png"));
            enemyRoomSprite = ImageIO.read(new File("enemyRoom.png"));
            bossRoomSprite = ImageIO.read(new File("bossRoom.png"));
            shopRoomSprite = ImageIO.read(new File("shopRoom.png"));    
        }
        catch (IOException e)
        {
            System.out.println("asset failure: " + e.getMessage());
        }

        setActiveGrid(activeGrid);
    }

    public void setActiveGrid(Room[][] newGrid) {
        this.grid = newGrid;
        if (this.grid != null)
        {
            calculateBounds();
            int pixelWidth = (maxX - minX + 1) * ROOM_WIDTH;
            int pixelHeight = (maxY - minY + 1) * ROOM_HEIGHT;
            this.setPreferredSize(new Dimension(pixelWidth, pixelHeight));
            this.revalidate(); 
        }
        this.repaint();
    }

    private void calculateBounds() {
        minX = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        minY = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;

        for (Room[] roomArr : grid)
        {
            for (Room room : roomArr)
            {
                if (room != null)
                {
                    maxX = Math.max(maxX, room.gridX);
                    minX = Math.min(minX, room.gridX);
                    maxY = Math.max(maxY, room.gridY);
                    minY = Math.min(minY, room.gridY);
                }
            }
        }
        
        if (minX == Integer.MAX_VALUE) {
            minX = 0; maxX = 0; minY = 0; maxY = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (grid == null) return;

        for (int y = minY; y <= maxY; y++)
        {
            for (int x = minX; x <= maxX; x++)
            {
                Room room = grid[x][y];

                if (room == null) continue;

                int drawX = (x - minX) * ROOM_WIDTH;
                int drawY = (y - minY) * ROOM_HEIGHT;

                switch (room.type)
                {
                    case "Start":
                        if (startRoomSprite != null) g.drawImage(startRoomSprite, drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT, null);
                        else { g.setColor(Color.WHITE); g.fillRect(drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT); }
                        break;
                    case "Empty":
                        if (emptyRoomSprite != null) g.drawImage(emptyRoomSprite, drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT, null);
                        else { g.setColor(Color.LIGHT_GRAY); g.fillRect(drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT); }
                        break;
                    case "Enemy":
                        if (enemyRoomSprite != null) g.drawImage(enemyRoomSprite, drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT, null);
                        else { g.setColor(Color.RED); g.fillRect(drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT); }
                        break;
                    case "Boss":
                        if (bossRoomSprite != null) g.drawImage(bossRoomSprite, drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT, null);
                        else { g.setColor(Color.MAGENTA); g.fillRect(drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT); }
                        break;
                    case "Shop":
                        if (shopRoomSprite != null) g.drawImage(shopRoomSprite, drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT, null);
                        else { g.setColor(Color.YELLOW); g.fillRect(drawX, drawY, ROOM_WIDTH, ROOM_HEIGHT); }
                        break;
                }
            }
        }
    }
}
