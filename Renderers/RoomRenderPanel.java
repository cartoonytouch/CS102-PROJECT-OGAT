package Renderers;
import javax.swing.JPanel;

import Map.Room.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RoomRenderPanel extends JPanel {
    private Room room;
    private final int TILE_SIZE = 80;

    private BufferedImage wallSprite;
    private BufferedImage floorSprite;
    private BufferedImage pitSprite;
    private BufferedImage rockSprite;
    private BufferedImage doorSprite;

    public RoomRenderPanel(Room activeRoom) {
        this.room = activeRoom;
        
        if (this.room != null)
        {
            int pixelWidth = room.width * TILE_SIZE;
            int pixelHeight = room.height * TILE_SIZE;
            this.setPreferredSize(new Dimension(pixelWidth, pixelHeight));
        }
        try
        {
            // Modify string parameters to match exact local file paths
            wallSprite = ImageIO.read(new File("Assets/RoomAssets/wall.png"));
            floorSprite = ImageIO.read(new File("Assets/RoomAssets/floor.png"));
            pitSprite = ImageIO.read(new File("Assets/RoomAssets/pit.png"));
            rockSprite = ImageIO.read(new File("Assets/RoomAssets/rock.png"));
            doorSprite = ImageIO.read(new File("Assets/RoomAssets/door.png"));
        }
        catch (IOException e)
        {
            System.out.println("Asset allocation failure: " + e.getMessage());
        }
    }

    public void setActiveRoom(Room newRoom) {
        this.room = newRoom;
        if (this.room != null)
        {
            this.setPreferredSize(new Dimension(room.width * TILE_SIZE, room.height * TILE_SIZE));
            this.revalidate(); 
        }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (room == null) return;

        // layer 1 : floor and pits
        for (int y = 0; y < room.height; y++)
        {
            for (int x = 0; x < room.width; x++)
            {
                FloorObjects floorID = room.localFloorGrid[y][x];
                
                if (floorID instanceof Tile)
                {
                    if (floorSprite != null)
                    {
                        g.drawImage(floorSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                    else
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
                else if (floorID instanceof Pit)
                {
                    if (pitSprite != null)
                    {
                        g.drawImage(pitSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                    else
                    {
                        g.setColor(Color.BLACK);
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        // layer 2: collidable objects
        for (int y = 0; y < room.height; y++)
        {
            for (int x = 0; x < room.width; x++)
            {
                RoomObjects objID = room.localObjectGrid[y][x];
                
                if (objID instanceof Wall)
                {
                    if (wallSprite != null)
                    {
                        g.drawImage(wallSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                    else
                    {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
                else if (objID instanceof Rock)
                {
                    if (rockSprite != null)
                    {
                        g.drawImage(rockSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                    else
                    {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        // layer 3: doors
        if (room.placedDoors != null)
        {
            for (Door d : room.placedDoors)
            {
                // Adjust d.gridX and d.gridY to match your Door class field names
                if (doorSprite != null)
                {
                    g.drawImage(doorSprite, d.coordX * TILE_SIZE, d.coordY * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
                else
                {
                    g.setColor(Color.ORANGE);
                    g.fillRect(d.coordX * TILE_SIZE, d.coordY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}
