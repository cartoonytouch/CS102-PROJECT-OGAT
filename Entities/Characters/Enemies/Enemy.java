package Entities.Characters.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Entities.Characters.GameCharacter;
import Renderers.DynamicOverlay;

public abstract class Enemy extends GameCharacter {

    public int attackPattern;
    public int detectionRange;
    protected final int spawnGridX;
    protected final int spawnGridY;

    protected BufferedImage bodySprite;
    protected BufferedImage pantSprite;
    protected BufferedImage headSprite;
    protected BufferedImage shirtSprite;
    protected BufferedImage helmetSprite;

    protected Enemy(int gridX, int gridY)
    {
        this.spawnGridX = gridX;
        this.spawnGridY = gridY;
        this.direction = "down";
        loadPlaceholderSprites();
    }

    public void bindToOverlay(DynamicOverlay overlay)
    {
        this.overlay = overlay;
        this.xCoord = spawnGridX * overlay.tileSize;
        this.yCoord = spawnGridY * overlay.tileSize;
    }

    @Override
    public void move() {
    }

    @Override
    public void attack() {
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g2)
    {
        if (overlay == null) {
            return;
        }

        drawLayer(g2, bodySprite);
        drawLayer(g2, headSprite);
        drawLayer(g2, shirtSprite);
        drawLayer(g2, pantSprite);
        drawLayer(g2, helmetSprite);
    }

    @Override
    public boolean checkCollision() {
        return false;
    }

    public abstract void calculatePath(int targetX, int targetY);

    private void loadPlaceholderSprites()
    {
        try {
            bodySprite = loadDownIdleFrame("Assets/PlayerAssets/idle/body.png");
            pantSprite = loadDownIdleFrame("Assets/PlayerAssets/idle/pant.png");
            headSprite = loadDownIdleFrame("Assets/PlayerAssets/idle/head.png");
            shirtSprite = loadDownIdleFrame("Assets/PlayerAssets/idle/shirt.png");
            helmetSprite = loadDownIdleFrame("Assets/PlayerAssets/idle/helmet.png");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load placeholder enemy sprites", e);
        }
    }

    private BufferedImage loadDownIdleFrame(String path) throws IOException
    {
        BufferedImage sheet = ImageIO.read(new File(path));
        int frameWidth = sheet.getWidth() / 8;
        int frameHeight = sheet.getHeight() / 4;
        return sheet.getSubimage(0, 2 * frameHeight, frameWidth, frameHeight);
    }

    private void drawLayer(Graphics2D g2, BufferedImage layer)
    {
        if (layer != null) {
            g2.drawImage(layer, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);
        }
    }
}
