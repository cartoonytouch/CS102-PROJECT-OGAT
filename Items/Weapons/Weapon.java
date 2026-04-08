package Items.Weapons;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.*;

import javax.imageio.ImageIO;

import Items.Item;

public abstract class Weapon extends Item {

    public static final int MAX_UPGRADE_LEVEL = 5;

    private final int baseAttackDamage;
    private int attackDamage;
    private int attackSpeed;
    private int range;
    private int upgradeLevel;
    private long swingTime;
    private final int drawOffset;
    private final int visualScale;

    public boolean ASbuff;
    public boolean ADbuff;

    public boolean isEquipped;

    protected BufferedImage upSprite;
    protected BufferedImage downSprite;
    protected BufferedImage rightSprite;
    protected BufferedImage leftSprite;

    public static final int attackDamageLow = 10;
    public static final int attackDamageModerate = 20;
    public static final int attackDamageHigh = 30;

    public static final int attackSpeedLow = 1;
    public static final int attackSpeedModerate = 2;
    public static final int attackSpeedHigh = 3;

    public static final int rangeLow = 15;
    public static final int rangeModerate = 20;
    public static final int rangeHigh = 25;

    public boolean isSwinging = false;
    private long swingStartTime = 0;
    public Rectangle weaponHitbox = new Rectangle(0, 0, 0, 0);

    protected Weapon(int attackDamage, int attackSpeed, int range, int drawOffset, int visualScale)
    {
        this.baseAttackDamage = attackDamage;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.drawOffset = drawOffset;
        this.visualScale = visualScale;
        this.upgradeLevel = 1;
        this.ADbuff = false;
        this.ASbuff = false;

        this.swingTime = 300;
    }

    protected void loadSharedSprite(String path)
    {
        try
        {
            BufferedImage sprite = ImageIO.read(new File(path));
            upSprite = sprite;
            downSprite = sprite;
            rightSprite = sprite;
            leftSprite = sprite;
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Failed to load weapon asset: " + path, e);
        }
    }

    public boolean canSwing()
    {
        long currentTime = System.currentTimeMillis();
        return currentTime - swingTime >= 300 / getAttackSpeed();
    }

    public void swing()
    {
        if (isEquipped && canSwing()) {
            isSwinging = true;
            swingStartTime = System.currentTimeMillis();
        }
    }

    @Override
    public void update()
    {
        if (!isEquipped) {
            ADbuff = false;
            ASbuff = false;
            isSwinging = false; 
        }
    }

    public void upgrade()
    {
        if (!canUpgrade())
        {
            return;
        }

        setUpgradeLevel(upgradeLevel + 1);
    }

    public int getAttackDamage()
    {
        return attackDamage + ((ADbuff ? 1 : 0) * 10);
    }

    public int getAttackSpeed()
    {
        return attackSpeed + (ASbuff ? 1 : 0);
    }

    public int getRange()
    {
        return range;
    }

    public void setAttackDamage(int attackDamage)
    {
        this.attackDamage = attackDamage;
    }

    public void setAttackSpeed(int attackSpeed)
    {
        this.attackSpeed = attackSpeed;
    }

    public void setRange(int range)
    {
        this.range = range;
    }

    public void setUpgradeLevel(int upgradeLevel)
    {
        int clampedLevel = Math.max(1, Math.min(MAX_UPGRADE_LEVEL, upgradeLevel));
        this.upgradeLevel = clampedLevel;
        this.attackDamage = baseAttackDamage + ((clampedLevel - 1) * 5);
    }

    public BufferedImage getSprite(String direction)
    {
        switch (direction)
        {
            case "up":
                return upSprite;
            case "down":
                return downSprite;
            case "right":
                return rightSprite;
            case "left":
                return leftSprite;
            default:
                return downSprite;
        }
    }

    public int getDrawOffset()
    {
        return drawOffset;
    }

    public int getVisualScale()
    {
        return visualScale;
    }

    public int getAttackDuration()
    {
        return Math.max(15, 60 / getAttackSpeed());
    }

    public int getDamageStartFrame()
    {
        return getAttackDuration() / 3;
    }

    public int getDamageEndFrame()
    {
        return getAttackDuration() / 2;
    }

    public int getUpgradeLevel()
    {
        return upgradeLevel;
    }

    public boolean canUpgrade()
    {
        return upgradeLevel < MAX_UPGRADE_LEVEL;
    }

    public int getMaxUpgradeLevel()
    {
        return MAX_UPGRADE_LEVEL;
    }
}
