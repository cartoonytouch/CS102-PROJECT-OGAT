package Renderers;
import javax.swing.JPanel;

import Map.Room.*;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class RoomRenderPanel extends JPanel {
    private Room room;
    private final int TILE_SIZE = 114;

    private BufferedImage wallSprite1;
    private BufferedImage wallSprite2;
    private BufferedImage wallSprite3;
    private BufferedImage wallSprite4;
    private BufferedImage wallSprite5;
    private BufferedImage wallSprite6;
    private BufferedImage wallSprite7;
    private BufferedImage floorSprite1;
    private BufferedImage floorSprite2;
    private BufferedImage floorSprite3;
    private BufferedImage floorSprite4;
    private BufferedImage pitSprite;
    private BufferedImage rockSprite;
    private BufferedImage doorSprite;
    private BufferedImage openDoorSprite;

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
            wallSprite1 = ImageIO.read(new File("Assets/RoomAssets/wall1.png"));
            wallSprite2 = ImageIO.read(new File("Assets/RoomAssets/wall2.png"));
            wallSprite3 = ImageIO.read(new File("Assets/RoomAssets/wall3.png"));
            wallSprite4 = ImageIO.read(new File("Assets/RoomAssets/wall4.png"));
            wallSprite5 = ImageIO.read(new File("Assets/RoomAssets/wall5.png"));
            wallSprite6 = ImageIO.read(new File("Assets/RoomAssets/wall6.png"));
            wallSprite7 = ImageIO.read(new File("Assets/RoomAssets/wall7.png"));
            floorSprite1 = ImageIO.read(new File("Assets/RoomAssets/floor1.png"));
            floorSprite2 = ImageIO.read(new File("Assets/RoomAssets/floor2.png"));
            floorSprite3 = ImageIO.read(new File("Assets/RoomAssets/floor3.png"));
            floorSprite4 = ImageIO.read(new File("Assets/RoomAssets/floor4.png"));
            pitSprite = ImageIO.read(new File("Assets/RoomAssets/pit.png"));
            rockSprite = ImageIO.read(new File("Assets/RoomAssets/rock.png"));
            doorSprite = ImageIO.read(new File("Assets/RoomAssets/closeddoor.png"));
            openDoorSprite = ImageIO.read(new File("Assets/RoomAssets/opendoor.png"));
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
                    Tile tile = (Tile)floorID;
                    switch(tile.tileType)
                    {
                        case 1: drawRandomRotatedTile(g, floorSprite1, tile); break;
                        case 2: drawRandomRotatedTile(g, floorSprite2, tile); break;
                        case 3: drawRandomRotatedTile(g, floorSprite3, tile); break;
                        case 4: drawRandomRotatedTile(g, floorSprite4, tile); break;
                    }
                }
                else if (floorID instanceof Pit)
                {
                    g.drawImage(pitSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null); 
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
                    Wall wall = (Wall)objID;
                    switch(wall.type)
                    {
                        case 1: drawRotatedRoomObj(g, wallSprite1, wall); break;
                        case 2: drawRotatedRoomObj(g, wallSprite2, wall); break;
                        case 3: drawRotatedRoomObj(g, wallSprite3, wall); break;
                        case 4: g.drawImage(wallSprite4, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null); break;
                        case 5: g.drawImage(wallSprite5, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null); break;
                        case 6: g.drawImage(wallSprite6, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null); break;
                        case 7: g.drawImage(wallSprite7, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null); break;

                    }
                }
                else if (objID instanceof Rock)
                {
                    g.drawImage(rockSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }

        // layer 3: doors
        if (room.placedDoors != null)
        {
            for (Door d : room.placedDoors)
            {
                if (d.open == false)
                {
                    drawRotatedRoomObj(g, doorSprite, d);
                }
                else
                {
                    drawRotatedRoomObj(g, openDoorSprite, d);
                }
            }
        }
    }

    private void drawRotatedRoomObj(Graphics g, BufferedImage sprite, RoomObjects obj) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        int x = obj.coordX * TILE_SIZE;
        int y = obj.coordY * TILE_SIZE;
        
        int centerX = x + (TILE_SIZE / 2);
        int centerY = y + (TILE_SIZE / 2);
        
        double angleInRadians = 0;
        
        // rotate
        switch (obj.dir)
        { 
            case 'n': angleInRadians = 0; break;
            case 'e':  angleInRadians = Math.toRadians(90); break;
            case 's': angleInRadians = Math.toRadians(180); break;
            case 'w':  angleInRadians = Math.toRadians(270); break;
        }
        
        g2d.rotate(angleInRadians, centerX, centerY);
        g2d.drawImage(sprite, x, y, TILE_SIZE, TILE_SIZE, null);
        
        g2d.dispose(); 
    }

    private void drawRandomRotatedTile(Graphics g, BufferedImage sprite, Tile tile)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        int x = tile.coordX * TILE_SIZE;
        int y = tile.coordY * TILE_SIZE;
        
        int centerX = x + (TILE_SIZE / 2);
        int centerY = y + (TILE_SIZE / 2);
        
        double angleInRadians = 0;

        // rotate
        switch (tile.dir)
        { 
            case 'n': angleInRadians = 0; break;
            case 'e':  angleInRadians = Math.toRadians(90); break;
            case 's': angleInRadians = Math.toRadians(180); break;
            case 'w':  angleInRadians = Math.toRadians(270); break;
        }
        
        g2d.rotate(angleInRadians, centerX, centerY);
        g2d.drawImage(sprite, x, y, TILE_SIZE, TILE_SIZE, null);
        
        g2d.dispose(); 
    }
}
