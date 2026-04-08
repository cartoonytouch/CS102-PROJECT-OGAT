package Entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Renderers.DynamicOverlay;

public class Heart {

    private static final int MAX_HEARTS = 6;

    private final DynamicOverlay overlay;
    private BufferedImage heartFull;
    private BufferedImage heartEmpty;

    public Heart(DynamicOverlay overlay)
    {
        this.overlay = overlay;
        loadHeartImages();
    }

    private void loadHeartImages()
    {
        try
        {
            heartFull = ImageIO.read(getClass().getResourceAsStream("/Assets/ItemAssets/FullHeart.png"));
            heartEmpty = ImageIO.read(getClass().getResourceAsStream("/Assets/ItemAssets/EmptyHeart.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2)
    {
        int x = overlay.tileSize / 2;
        int y = overlay.tileSize / 2;

        for (int i = 1; i <= MAX_HEARTS; i++)
        {
            BufferedImage heartSprite = (overlay.player.health >= i) ? heartFull : heartEmpty;
            if (heartSprite != null)
            {
                g2.drawImage(heartSprite, x, y, overlay.tileSize, overlay.tileSize, null);
            }
            x += overlay.tileSize;
        }
    }
}
