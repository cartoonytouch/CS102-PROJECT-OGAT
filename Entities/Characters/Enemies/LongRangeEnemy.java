package Entities.Characters.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entities.Projectile;

public class LongRangeEnemy extends Enemy {

    private static boolean customImagesLoaded = false;
    private static boolean attemptedLoad = false;

    long startTime;
    long attackCooldown = 0;

    public ArrayList<Projectile> projectiles = new ArrayList<>();

    private static final BufferedImage[] idleBodyDown = new BufferedImage[3];
    private static final BufferedImage[] idlePantDown = new BufferedImage[3];
    private static final BufferedImage[] idleHeadDown = new BufferedImage[3];
    private static final BufferedImage[] idleShirtDown = new BufferedImage[3];
    private static final BufferedImage[] idlePatchDown = new BufferedImage[3];
    private static final BufferedImage[] idleBeardDown = new BufferedImage[3];
    private static final BufferedImage[] idleEyesDown = new BufferedImage[3];
    public BufferedImage longRangeMonster;

    public LongRangeEnemy(int gridX, int gridY)
    {
        super(gridX, gridY);
        health = 1;
        detectionRange = 0;
        attackPattern = 2;
        direction = "down";
        SpriteNum = 0;
        loadCustomSprites();
    }

    private void loadCustomSprites()
    {
        try
        {
            longRangeMonster = ImageIO.read(new File("Assets/EnemyAssets/LongRangeEnemy/LongRangeMonster.png"));

            customImagesLoaded = true;
        }
        catch (IOException e)
        {
            customImagesLoaded = false;
        }
    }


    @Override
    public void update()
    {
        if (overlay == null)
        {
            return;
        }

        if (customImagesLoaded)
        {
            spriteCounter++;
            if (spriteCounter > 10)
            {
                SpriteNum++;
                if (SpriteNum >= idleBodyDown.length)
                {
                    SpriteNum = 0;
                }
                spriteCounter = 0;
            }
        }

        long currentTime = System.currentTimeMillis();
        if (startTime == 0)
        {
            startTime = currentTime;
            return;
        }

        if(currentTime - startTime >= 2000)
        {
            startTime = currentTime;
            Projectile p = new Projectile(xCoord, yCoord, 5, overlay.player, 2);
            p.bindToOverlay(overlay);
            overlay.currentRoom.projectiles.add(p);
        }
    }

    @Override
    public void draw(Graphics2D g2)
    {
        if (!customImagesLoaded || overlay == null)
        {
            super.draw(g2);
            return;
        }

        drawLayer(g2, longRangeMonster);
    
    }

    private void drawLayer(Graphics2D g2, BufferedImage layer)
    {
        if (layer != null)
        {
            g2.drawImage(layer, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);
        }
    }

    @Override
    public void takeDamage(int amount)
    {
        health -= amount;
    }

    @Override
    public void attack()
    {
    }
}
