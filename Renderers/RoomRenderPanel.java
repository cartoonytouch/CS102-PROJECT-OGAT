package Renderers;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Entities.FloorObjects;
import Entities.StaticEntities.Door;
import Entities.StaticEntities.Pit;
import Entities.StaticEntities.Rock;
import Entities.StaticEntities.StaticEntity;
import Entities.StaticEntities.Tile;
import Entities.StaticEntities.Wall;
import Map.Room.Room;

public class RoomRenderPanel extends JPanel {
    private Room room;
    private final int tileSize;

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

    public RoomRenderPanel(Room activeRoom)
    {
        this(activeRoom, 80);
    }

    public RoomRenderPanel(Room activeRoom, int tileSize)
    {
        room = activeRoom;
        this.tileSize = tileSize;

        if (room != null)
        {
            int pixelWidth = room.width * tileSize;
            int pixelHeight = room.height * tileSize;
            this.setPreferredSize(new Dimension(pixelWidth, pixelHeight));
        }

        try
        {
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

    public void setActiveRoom(Room newRoom)
    {
        room = newRoom;
        if (room != null)
        {
            this.setPreferredSize(new Dimension(room.width * tileSize, room.height * tileSize));
            this.revalidate();
        }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (room == null)
        {
            return;
        }

        for (int y = 0; y < room.height; y++)
        {
            for (int x = 0; x < room.width; x++)
            {
                FloorObjects floorID = room.localFloorGrid[y][x];

                if (floorID instanceof Tile)
                {
                    Tile tile = (Tile) floorID;
                    switch (tile.tileType)
                    {
                        case 2:
                            drawRotatedTile(g, floorSprite2, tile);
                            break;
                        case 3:
                            drawRotatedTile(g, floorSprite3, tile);
                            break;
                        case 4:
                            drawRotatedTile(g, floorSprite4, tile);
                            break;
                        default:
                            drawRotatedTile(g, floorSprite1, tile);
                            break;
                    }
                }
                else if (floorID instanceof Pit)
                {
                    drawFallbackImage(g, pitSprite, x, y, Color.BLACK);
                }
            }
        }

        for (int y = 0; y < room.height; y++)
        {
            for (int x = 0; x < room.width; x++)
            {
                StaticEntity objID = room.localObjectGrid[y][x];

                if (objID instanceof Wall)
                {
                    Wall wall = (Wall) objID;
                    switch (wall.type)
                    {
                        case 2:
                            drawRotatedStaticEntity(g, wallSprite2, wall);
                            break;
                        case 3:
                            drawRotatedStaticEntity(g, wallSprite3, wall);
                            break;
                        case 4:
                            drawFallbackImage(g, wallSprite4, x, y, Color.DARK_GRAY);
                            break;
                        case 5:
                            drawFallbackImage(g, wallSprite5, x, y, Color.DARK_GRAY);
                            break;
                        case 6:
                            drawFallbackImage(g, wallSprite6, x, y, Color.DARK_GRAY);
                            break;
                        case 7:
                            drawFallbackImage(g, wallSprite7, x, y, Color.DARK_GRAY);
                            break;
                        default:
                            drawRotatedStaticEntity(g, wallSprite1, wall);
                            break;
                    }
                }
                else if (objID instanceof Rock)
                {
                    drawFallbackImage(g, rockSprite, x, y, Color.DARK_GRAY);
                }
            }
        }

        if (room.placedDoors != null)
        {
            for (Door door : room.placedDoors)
            {
                if (door.open)
                {
                    drawRotatedStaticEntity(g, openDoorSprite, door);
                }
                else
                {
                    drawRotatedStaticEntity(g, doorSprite, door);
                }
            }
        }
    }

    private void drawFallbackImage(Graphics g, BufferedImage sprite, int tileX, int tileY, Color fallbackColor)
    {
        if (sprite != null)
        {
            g.drawImage(sprite, tileX * tileSize, tileY * tileSize, tileSize, tileSize, null);
        }
        else
        {
            g.setColor(fallbackColor);
            g.fillRect(tileX * tileSize, tileY * tileSize, tileSize, tileSize);
        }
    }

    private void drawRotatedStaticEntity(Graphics g, BufferedImage sprite, StaticEntity entity)
    {
        if (sprite == null)
        {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(entity.coordX * tileSize, entity.coordY * tileSize, tileSize, tileSize);
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        int x = entity.coordX * tileSize;
        int y = entity.coordY * tileSize;
        int centerX = x + (tileSize / 2);
        int centerY = y + (tileSize / 2);

        g2d.rotate(rotationFor(entity.dir), centerX, centerY);
        g2d.drawImage(sprite, x, y, tileSize, tileSize, null);
        g2d.dispose();
    }

    private void drawRotatedTile(Graphics g, BufferedImage sprite, Tile tile)
    {
        if (sprite == null)
        {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(tile.coordX * tileSize, tile.coordY * tileSize, tileSize, tileSize);
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        int x = tile.coordX * tileSize;
        int y = tile.coordY * tileSize;
        int centerX = x + (tileSize / 2);
        int centerY = y + (tileSize / 2);

        g2d.rotate(rotationFor(tile.dir), centerX, centerY);
        g2d.drawImage(sprite, x, y, tileSize, tileSize, null);
        g2d.dispose();
    }

    private double rotationFor(char dir)
    {
        switch (dir)
        {
            case 'e':
                return Math.toRadians(90);
            case 's':
                return Math.toRadians(180);
            case 'w':
                return Math.toRadians(270);
            default:
                return 0;
        }
    }
}
