package Entities.Characters.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LongRangeEnemy extends Enemy {

    private static boolean customImagesLoaded = false;
    private static boolean attemptedLoad = false;

    private static final BufferedImage[] idleBodyDown = new BufferedImage[3];
    private static final BufferedImage[] idlePantDown = new BufferedImage[3];
    private static final BufferedImage[] idleHeadDown = new BufferedImage[3];
    private static final BufferedImage[] idleShirtDown = new BufferedImage[3];
    private static final BufferedImage[] idlePatchDown = new BufferedImage[3];
    private static final BufferedImage[] idleBeardDown = new BufferedImage[3];
    private static final BufferedImage[] idleEyesDown = new BufferedImage[3];

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
        if (attemptedLoad)
        {
            return;
        }

        attemptedLoad = true;

        try
        {
            BufferedImage idleBody = ImageIO.read(new File("Assets/EnemyAssets/LongRangeEnemy/idle/body.png"));
            BufferedImage idlePant = ImageIO.read(new File("Assets/EnemyAssets/LongRangeEnemy/idle/pant.png"));
            BufferedImage idleHead = ImageIO.read(new File("Assets/EnemyAssets/LongRangeEnemy/idle/head.png"));
            BufferedImage idleShirt = ImageIO.read(new File("Assets/EnemyAssets/LongRangeEnemy/idle/shirt.png"));
            BufferedImage idlePatch = ImageIO.read(new File("Assets/EnemyAssets/LongRangeEnemy/idle/Eyepatch.png"));
            BufferedImage idleBeard = ImageIO.read(new File("Assets/EnemyAssets/LongRangeEnemy/idle/beard.png"));
            BufferedImage idleEyes = ImageIO.read(new File("Assets/EnemyAssets/LongRangeEnemy/idle/eyes.png"));

            populateFrames(idleBody, idleBodyDown);
            populateFrames(idlePant, idlePantDown);
            populateFrames(idleHead, idleHeadDown);
            populateFrames(idleShirt, idleShirtDown);
            populateFrames(idlePatch, idlePatchDown);
            populateFrames(idleBeard, idleBeardDown);
            populateFrames(idleEyes, idleEyesDown);

            customImagesLoaded = true;
        }
        catch (IOException e)
        {
            customImagesLoaded = false;
        }
    }

    private void populateFrames(BufferedImage sheet, BufferedImage[] targetFrames)
    {
        int frameCount = targetFrames.length;
        int frameWidth = sheet.getWidth() / frameCount;
        int frameHeight = sheet.getHeight() / 4;
        int rowOffset = 2 * frameHeight;

        for (int i = 0; i < frameCount; i++)
        {
            targetFrames[i] = sheet.getSubimage(i * frameWidth, rowOffset, frameWidth, frameHeight);
        }
    }

    @Override
    public void update()
    {
        if (!customImagesLoaded)
        {
            return;
        }

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

    @Override
    public void draw(Graphics2D g2)
    {
        if (!customImagesLoaded || overlay == null)
        {
            super.draw(g2);
            return;
        }

        int frameIndex = Math.max(0, Math.min(SpriteNum, idleBodyDown.length - 1));

        drawLayer(g2, idleBodyDown[frameIndex]);
        drawLayer(g2, idleHeadDown[frameIndex]);
        drawLayer(g2, idleEyesDown[frameIndex]);
        drawLayer(g2, idleBeardDown[frameIndex]);
        drawLayer(g2, idleShirtDown[frameIndex]);
        drawLayer(g2, idlePantDown[frameIndex]);
        drawLayer(g2, idlePatchDown[frameIndex]);
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
