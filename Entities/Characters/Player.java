package Entities.Characters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import java.awt.*;
import javax.swing.*;

import javax.imageio.ImageIO;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import Entities.Characters.Enemies.Enemy;
import Entities.StaticEntities.Door;
import Entities.StaticEntities.Pit;
import Entities.StaticEntities.Rock;
import Entities.StaticEntities.Wall;
import HelperClasses.KeyHandler;
import Items.Consumable;
import Items.Inventory;
import Items.Item;
import Items.ItemCatalog;
import Items.Passive;
import Items.Weapons.Hammer;
import Items.Weapons.Spear;
import Items.Weapons.Sword;
import Items.Weapons.Weapon;
import Map.Room.RewardDrop;
import Map.Room.Station;
import MusicsandSounds.Sound;
import Renderers.DynamicOverlay;

public class Player extends GameCharacter {
    private static final int NORMAL_SPEED = 4;
    private static final int DASH_SPEED = 12;
    private static final int SPEED_BUFF = 7;
    private static final int PARRY_DURATION = 20;

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    Sound sound = new Sound();

    public boolean canStun;

    public int mana;
    public int currency;
    public static int inventoryLimit = 4;
    public Inventory inventory;

    public String playerClass;
    public String difficulty;
    public boolean canParry;

    public boolean isDashing = false;
    public int dashCounter = 0;
    public int staminaReCounter = 0;

    public boolean isAttacking = false;
    public int attackCounter = 0;
    
    public boolean damageAppliedForThisAttack = false;

    public boolean isParrying = false;
    public int parryCounter = 0;

    public boolean isInteracting = false;
    public boolean isConsuming = false;
    public long consumableStartTime = 0;
    public long consumableDuration = 0;
    public boolean isBuffActive = false;
    public boolean isAttackBuffActive = false;
    public String buffName = "";

    public boolean isMoving = false;

    public boolean isInvisible = false;
    public int invisibleCounter = 0;

    public int swapCounter = 0;
    public boolean isSwapping = false;

    public String appliedPassiveID;
    private final LinkedHashSet<String> discoveredItemIds = new LinkedHashSet<>();

    public BufferedImage[] bodyUp = new BufferedImage[8];
    public BufferedImage[] bodyDown = new BufferedImage[8];
    public BufferedImage[] bodyLeft = new BufferedImage[8];
    public BufferedImage[] bodyRight = new BufferedImage[8];

    public BufferedImage[] pantUp = new BufferedImage[8];
    public BufferedImage[] pantDown = new BufferedImage[8];
    public BufferedImage[] pantLeft = new BufferedImage[8];
    public BufferedImage[] pantRight = new BufferedImage[8];

    public BufferedImage[] headUp = new BufferedImage[8];
    public BufferedImage[] headDown = new BufferedImage[8];
    public BufferedImage[] headLeft = new BufferedImage[8];
    public BufferedImage[] headRight = new BufferedImage[8];

    public BufferedImage[] hairUp = new BufferedImage[8];
    public BufferedImage[] hairDown = new BufferedImage[8];
    public BufferedImage[] hairLeft = new BufferedImage[8];
    public BufferedImage[] hairRight = new BufferedImage[8];

    public BufferedImage[] shirtUp = new BufferedImage[8];
    public BufferedImage[] shirtDown = new BufferedImage[8];
    public BufferedImage[] shirtLeft = new BufferedImage[8];
    public BufferedImage[] shirtRight = new BufferedImage[8];

    public BufferedImage[] boatUp = new BufferedImage[8];
    public BufferedImage[] boatDown = new BufferedImage[8];
    public BufferedImage[] boatLeft = new BufferedImage[8];
    public BufferedImage[] boatRight = new BufferedImage[8];

    public BufferedImage[] helmetUp = new BufferedImage[8];
    public BufferedImage[] helmetDown = new BufferedImage[8];
    public BufferedImage[] helmetLeft = new BufferedImage[8];
    public BufferedImage[] helmetRight = new BufferedImage[8];

    public BufferedImage[] beardUp = new BufferedImage[8];
    public BufferedImage[] beardDownn = new BufferedImage[8];
    public BufferedImage[] beardLeft = new BufferedImage[8];
    public BufferedImage[] beardRight = new BufferedImage[8];

    public BufferedImage[] idlebodyUp = new BufferedImage[3];
    public BufferedImage[] idlebodyDown = new BufferedImage[3];
    public BufferedImage[] idlebodyLeft = new BufferedImage[3];
    public BufferedImage[] idlebodyRight = new BufferedImage[3];

    public BufferedImage[] idlepantUp = new BufferedImage[3];
    public BufferedImage[] idlepantDown = new BufferedImage[3];
    public BufferedImage[] idlepantLeft = new BufferedImage[3];
    public BufferedImage[] idlepantRight = new BufferedImage[3];

    public BufferedImage[] idleheadUp = new BufferedImage[3];
    public BufferedImage[] idleheadDown = new BufferedImage[3];
    public BufferedImage[] idleheadLeft = new BufferedImage[3];
    public BufferedImage[] idleheadRight = new BufferedImage[3];

    public BufferedImage[] idleshirtUp = new BufferedImage[3];
    public BufferedImage[] idleshirtDown = new BufferedImage[3];
    public BufferedImage[] idleshirtLeft = new BufferedImage[3];
    public BufferedImage[] idleshirtRight = new BufferedImage[3];

    public BufferedImage[] idlehelmetUp = new BufferedImage[3];
    public BufferedImage[] idlehelmetDown = new BufferedImage[3];
    public BufferedImage[] idlehelmetLeft = new BufferedImage[3];
    public BufferedImage[] idlehelmetRight = new BufferedImage[3];

    public Player(DynamicOverlay overlay, KeyHandler keyH)
    {
        this(overlay, keyH, "Swordsman","Easy");
    }

    public Player(DynamicOverlay overlay, KeyHandler keyH, String playerClass, String difficulty)
    {
        this.overlay = overlay;
        this.keyH = keyH;

        solidArea = buildSolidArea();

        screenX = overlay.screenWidth / 2 - (overlay.tileSize / 2);
        screenY = overlay.screenHeight / 2 - (overlay.tileSize / 2);

        this.inventory = new Inventory();
        this.playerClass = normalizePlayerClass(playerClass);
        this.difficulty = difficulty;

        setDefault();
        seedDiscoveredItemPool();
        grantStarterLoadout();
        getPlayerImg();
    }

    public void setDefault()
    {
        xCoord = screenX;
        yCoord = screenY;

        spped = NORMAL_SPEED;
        direction = "down";

        health = 6;
        mana = 100;
        currency = 0;
        canParry = true;
        SpriteNum = 0;
        appliedPassiveID = "";
    }

    public void resetForNewRun(String playerClass)
    {
        this.playerClass = normalizePlayerClass(playerClass);
        this.inventory = new Inventory();

        isDashing = false;
        dashCounter = 0;
        staminaReCounter = 0;
        isAttacking = false;
        attackCounter = 0;
        damageAppliedForThisAttack = false;
        isParrying = false;
        parryCounter = 0;
        isInteracting = false;
        isConsuming = false;
        consumableStartTime = 0;
        consumableDuration = 0;
        isBuffActive = false;
        isAttackBuffActive = false;
        buffName = "";
        isMoving = false;
        isInvisible = false;
        invisibleCounter = 0;
        swapCounter = 0;
        isSwapping = false;

        setDefault();
        seedDiscoveredItemPool();
        grantStarterLoadout();
    }

    private String normalizePlayerClass(String playerClass)
    {
        if (playerClass == null || playerClass.isBlank())
        {
            return "Swordsman";
        }
        if (playerClass.equalsIgnoreCase("Lancer"))
        {
            return "Spearman";
        }
        return playerClass;
    }

    private void grantStarterLoadout()
    {
        if (inventory == null || getChoosenWeapon() != null)
        {
            return;
        }

        if (playerClass.equals("Spearman"))
        {
            inventory.add(new Spear());
        }
        else if (playerClass.equals("Smasher"))
        {
            inventory.add(new Hammer());
        }
        else
        {
            inventory.add(new Sword());
        }

        inventory.setChoosedWeaponIndex(0);
        syncEquippedWeapons();
        discoverItem(getChoosenWeapon());
    }

    private void seedDiscoveredItemPool()
    {
        discoveredItemIds.clear();
        discoveredItemIds.addAll(ItemCatalog.getDefaultShopPool());
    }

    public void getPlayerImg()
    {
        try
        {
            BufferedImage bodySheet = ImageIO.read(new File("Assets/PlayerAssets/run/body_sh.png"));
            BufferedImage pantSheet = ImageIO.read(new File("Assets/PlayerAssets/run/pant_sh.png"));
            BufferedImage headSheet = ImageIO.read(new File("Assets/PlayerAssets/run/head_sh.png"));
            BufferedImage shirtSheet = ImageIO.read(new File("Assets/PlayerAssets/run/shirt_sh.png"));
            BufferedImage helmetSheet = ImageIO.read(new File("Assets/PlayerAssets/run/helmet_sh.png"));

            BufferedImage idleBody = ImageIO.read(new File("Assets/PlayerAssets/idle/body.png"));
            BufferedImage idlePant = ImageIO.read(new File("Assets/PlayerAssets/idle/pant.png"));
            BufferedImage idleHead = ImageIO.read(new File("Assets/PlayerAssets/idle/head.png"));
            BufferedImage idleShirt = ImageIO.read(new File("Assets/PlayerAssets/idle/shirt.png"));
            BufferedImage idleHelmet = ImageIO.read(new File("Assets/PlayerAssets/idle/helmet.png"));

            int frameWidth = bodySheet.getWidth() / 8;
            int frameHeight = bodySheet.getHeight() / 4;

            for (int i = 0; i < 8; i++)
            {
                bodyUp[i] = bodySheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                bodyDown[i] = bodySheet.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                bodyLeft[i] = bodySheet.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                bodyRight[i] = bodySheet.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

                pantUp[i] = pantSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                pantDown[i] = pantSheet.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                pantLeft[i] = pantSheet.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                pantRight[i] = pantSheet.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

                headUp[i] = headSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                headDown[i] = headSheet.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                headLeft[i] = headSheet.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                headRight[i] = headSheet.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

                shirtUp[i] = shirtSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                shirtDown[i] = shirtSheet.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                shirtLeft[i] = shirtSheet.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                shirtRight[i] = shirtSheet.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

                helmetUp[i] = helmetSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                helmetDown[i] = helmetSheet.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                helmetLeft[i] = helmetSheet.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                helmetRight[i] = helmetSheet.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);
            }

            for (int i = 0; i < 3; i++)
            {
                idlebodyUp[i] = idleBody.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                idlebodyDown[i] = idleBody.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                idlebodyLeft[i] = idleBody.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                idlebodyRight[i] = idleBody.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

                idlepantUp[i] = idlePant.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                idlepantDown[i] = idlePant.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                idlepantLeft[i] = idlePant.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                idlepantRight[i] = idlePant.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

                idleheadUp[i] = idleHead.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                idleheadDown[i] = idleHead.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                idleheadLeft[i] = idleHead.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                idleheadRight[i] = idleHead.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

                idleshirtUp[i] = idleShirt.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                idleshirtDown[i] = idleShirt.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                idleshirtLeft[i] = idleShirt.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                idleshirtRight[i] = idleShirt.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

                idlehelmetUp[i] = idleHelmet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                idlehelmetDown[i] = idleHelmet.getSubimage(i * frameWidth, 2 * frameHeight, frameWidth, frameHeight);
                idlehelmetLeft[i] = idleHelmet.getSubimage(i * frameWidth, frameHeight, frameWidth, frameHeight);
                idlehelmetRight[i] = idleHelmet.getSubimage(i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void move()
    {
        int deltaX = 0;
        int deltaY = 0;

        if (keyH.leftPressed && !keyH.rightPressed)
        {
            deltaX -= spped;
        }
        else if (keyH.rightPressed && !keyH.leftPressed)
        {
            deltaX += spped;
        }

        if (keyH.upPressed && !keyH.downPressed)
        {
            deltaY -= spped;
        }
        else if (keyH.downPressed && !keyH.upPressed)
        {
            deltaY += spped;
        }

        if (deltaX == 0 && deltaY == 0)
        {
            updateInvisibility();
            return;
        }

        if (deltaY < 0)
        {
            direction = "up";
        }
        else if (deltaY > 0)
        {
            direction = "down";
        }
        else if (deltaX < 0)
        {
            direction = "left";
        }
        else if (deltaX > 0)
        {
            direction = "right";
        }

        if (deltaX != 0 && deltaY != 0)
        {
            double diagonalScale = 1.0 / Math.sqrt(2.0);
            deltaX = (int) Math.round(deltaX * diagonalScale);
            deltaY = (int) Math.round(deltaY * diagonalScale);

            if (deltaX == 0)
            {
                deltaX = keyH.leftPressed ? -1 : 1;
            }
            if (deltaY == 0)
            {
                deltaY = keyH.upPressed ? -1 : 1;
            }
        }

        boolean changedRoom = false;

        if (deltaX != 0)
        {
            changedRoom = attemptMove(deltaX, 0);
        }
        if (!changedRoom && deltaY != 0)
        {
            attemptMove(0, deltaY);
        }

        updateInvisibility();
    }

    private boolean attemptMove(int deltaX, int deltaY)
    {
        int nextX = xCoord + deltaX;
        int nextY = yCoord + deltaY;
        Rectangle boxPlayer = createCollisionBox(nextX, nextY);

        if (collidesWithStaticRoomObjects(boxPlayer))
        {
            return false;
        }

        if (overlay.currentRoom != null && overlay.currentRoom.placedDoors != null)
        {
            for (Door door : overlay.currentRoom.placedDoors)
            {
                Rectangle doorRect = new Rectangle(
                    door.coordX * overlay.tileSize,
                    door.coordY * overlay.tileSize,
                    overlay.tileSize,
                    overlay.tileSize
                );

                if (boxPlayer.intersects(doorRect))
                {
                    if (!door.open)
                    {
                        return false;
                    }

                    if (door.coordX == overlay.currentRoom.width - 1)
                    {
                        overlay.changeRoom(overlay.curGridX + 1, overlay.curGridY, "right");
                    }
                    else if (door.coordX == 0)
                    {
                        overlay.changeRoom(overlay.curGridX - 1, overlay.curGridY, "left");
                    }
                    else if (door.coordY == 0)
                    {
                        overlay.changeRoom(overlay.curGridX, overlay.curGridY - 1, "up");
                    }
                    else if (door.coordY == overlay.currentRoom.height - 1)
                    {
                        overlay.changeRoom(overlay.curGridX, overlay.curGridY + 1, "down");
                    }
                    return true;
                }
            }
        }

        if (overlay.currentRoom != null)
        {
            for (Pit pit : overlay.currentRoom.placedPits)
            {
                Rectangle pitRectangle = createPitDamageBox(pit);

                if (boxPlayer.intersects(pitRectangle))
                {
                    takeDamage(1);
                    return false;
                }
            }
        }

        nextX = clampXToRoom(nextX);
        nextY = clampYToRoom(nextY);

        xCoord = nextX;
        yCoord = nextY;
        return false;
    }

    private Rectangle buildSolidArea()
    {
        int insetX = Math.round(overlay.tileSize * 0.30f);
        int insetY = overlay.tileSize / 2;
        int width = Math.max(overlay.tileSize / 3, overlay.tileSize - (insetX * 2));
        int height = Math.max(overlay.tileSize / 2, overlay.tileSize - insetY);

        return new Rectangle(insetX, insetY, width, height);
    }

    private Rectangle createCollisionBox(int proposedX, int proposedY)
    {
        return new Rectangle(
            proposedX + solidArea.x,
            proposedY + solidArea.y,
            solidArea.width,
            solidArea.height
        );
    }

    private Rectangle createPitDamageBox(Pit pit)
    {
        int inset = overlay.tileSize / 4;
        int size = overlay.tileSize - (inset * 2);
        return new Rectangle(
            (pit.coordX * overlay.tileSize) + inset,
            (pit.coordY * overlay.tileSize) + inset,
            size,
            size
        );
    }

    private int clampXToRoom(int proposedX)
    {
        if (overlay.currentRoom == null)
        {
            return proposedX;
        }

        int minX = -solidArea.x;
        int maxX = (overlay.currentRoom.width * overlay.tileSize) - solidArea.x - solidArea.width;
        return Math.max(minX, Math.min(proposedX, maxX));
    }

    private int clampYToRoom(int proposedY)
    {
        if (overlay.currentRoom == null)
        {
            return proposedY;
        }

        int minY = -solidArea.y;
        int maxY = (overlay.currentRoom.height * overlay.tileSize) - solidArea.y - solidArea.height;
        return Math.max(minY, Math.min(proposedY, maxY));
    }

    private boolean collidesWithStaticRoomObjects(Rectangle boxPlayer)
    {
        if (overlay.currentRoom == null)
        {
            return false;
        }

        int leftCol = boxPlayer.x / overlay.tileSize;
        int rightCol = (boxPlayer.x + boxPlayer.width) / overlay.tileSize;
        int topRow = boxPlayer.y / overlay.tileSize;
        int bottomRow = (boxPlayer.y + boxPlayer.height) / overlay.tileSize;

        int maxCol = overlay.currentRoom.width - 1;
        int maxRow = overlay.currentRoom.height - 1;

        leftCol = Math.max(0, Math.min(leftCol, maxCol));
        rightCol = Math.max(0, Math.min(rightCol, maxCol));
        topRow = Math.max(0, Math.min(topRow, maxRow));
        bottomRow = Math.max(0, Math.min(bottomRow, maxRow));

        for (int row = topRow; row <= bottomRow; row++)
        {
            for (int col = leftCol; col <= rightCol; col++)
            {
                Object obj = overlay.currentRoom.localObjectGrid[row][col];
                if (obj instanceof Wall || obj instanceof Rock)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private void updateInvisibility()
    {
        if (isInvisible)
        {
            invisibleCounter++;
            if (invisibleCounter > 60)
            {
                isInvisible = false;
                invisibleCounter = 0;
            }
        }
    }

    @Override
    public void attack()
    {
        Weapon weapon = getChoosenWeapon();

        if (!isAttacking && weapon != null && weapon.canSwing())
        {
            weapon.swing();
            isAttacking = true;
            playSoundEffect(3);
            
            attackCounter = 0;
            damageAppliedForThisAttack = false;
            System.out.println("Attack!");
        }
    }

    private Rectangle createAttackArea()
    {
        Weapon weapon = getChoosenWeapon();
        if (weapon == null)
        {
            return null;
        }

        BufferedImage sprite = weapon.getSprite(direction);
        if (sprite == null)
        {
            return null;
        }

        int scale = weapon.getVisualScale();
        int drawWidth = sprite.getWidth() * scale;
        int drawHeight = sprite.getHeight() * scale;
        int drawOffset = weapon.getDrawOffset();

        int weaponX = xCoord;
        int weaponY = yCoord;

        switch (direction)
        {
            case "up":
                weaponY = yCoord - drawHeight + drawOffset;
                weaponX = xCoord + (overlay.tileSize - drawWidth) / 2;
                break;
            case "down":
                weaponY = yCoord + overlay.tileSize - drawOffset;
                weaponX = xCoord + (overlay.tileSize - drawWidth) / 2;
                break;
            case "left":
                weaponY = yCoord + (overlay.tileSize - drawHeight) / 2;
                weaponX = xCoord - drawWidth + drawOffset;
                break;
            case "right":
                weaponY = yCoord + (overlay.tileSize - drawHeight) / 2;
                weaponX = xCoord + overlay.tileSize - drawOffset;
                break;
            default:
                break;
        }

        int inset = 8;

        return new Rectangle(
            weaponX + inset,
            weaponY + inset,
            Math.max(1, drawWidth - (inset * 2)),
            Math.max(1, drawHeight - (inset * 2))
        );
    }

    private void checkAttackHit()
    {
        if (!isAttacking || damageAppliedForThisAttack || overlay.currentRoom == null)
        {
            return;
        }

        Rectangle attackBox = createAttackArea();
        Weapon weapon = getChoosenWeapon();
        if (attackBox == null || weapon == null)
        {
            return;
        }

        for (int i = 0; i < overlay.currentRoom.localEnemies.size(); i++)
        {
            Enemy enemy = overlay.currentRoom.localEnemies.get(i);
            Rectangle enemyBox = enemy.getSolidArea();

            if (enemyBox != null && attackBox.intersects(enemyBox))
            {
                int attackDamage = weapon.getAttackDamage();
                if (isAttackBuffActive)
                {
                    attackDamage += 10;
                }

                enemy.takeDamage(attackDamage);
                damageAppliedForThisAttack = true;

                if (enemy.health <= 0 && overlay.currentRoom.localEnemies.size() > 0)
                {
                    overlay.currentRoom.localEnemies.remove(i);
                }
                else if (overlay.currentRoom.localEnemies.size() <= 0) {
                    return;
                }
                break;
            }
        }
    }

    @Override
    public void takeDamage(int amount)
    {
        if (amount <= 0 || health <= 0 || isInvisible || isParrying)
        {
            if(isParrying && amount > 0)
            {
                playSoundEffect(10);
                return;
            }
            return;
        }

        health = Math.max(0, health - amount);

        if (health <= 0)
        {
            overlay.gameOver();
            return;
        }

        isInvisible = true;
        invisibleCounter = 0;
    }

    public void dash()
    {
        if (mana >= 20 && !isDashing)
        {
            isDashing = true;
            mana -= 20;
            spped = DASH_SPEED;
        }
    }

    public void parry()
    {
        if (mana >= 10 && !isParrying)
        {
            isParrying = true;
            mana -= 10;
            System.out.println("Parry!");
        }
    }

    public void interact()
    {
        if (overlay.currentRoom == null || overlay.currentRoom.placedStations == null)
        {
            return;
        }

        Station closestStation = null;
        int closestDistance = Integer.MAX_VALUE;

        for (Station station : overlay.currentRoom.placedStations)
        {
            if (!station.isPlayerNearby(this, overlay.tileSize))
            {
                continue;
            }

            int distance = Math.abs((station.coordX * overlay.tileSize) - xCoord)
                + Math.abs((station.coordY * overlay.tileSize) - yCoord);

            if (distance < closestDistance)
            {
                closestDistance = distance;
                closestStation = station;
            }
        }

        if (closestStation != null)
        {
            System.out.println(closestStation.interact(this));
            overlay.openStationMenu(closestStation);
        }
    }

    public void consumeItem()
    {
        Item consumableItem = getInventory().getItems()[2];
        if (!(consumableItem instanceof Consumable))
        {
            return;
        }

        String itemId = consumableItem.getItemID();

        if (itemId.equals("11"))
        {
            this.health++;
            this.getInventory().remove(consumableItem);
        }
        else if (itemId.equals("12"))
        {
            this.mana = Math.min(this.mana + 20, 100);
            this.getInventory().remove(consumableItem);
        }
        else if (itemId.equals("13"))
        {
            buffName = "Attack";
            consumableStartTime = System.currentTimeMillis();
            consumableDuration = 3000;
            isBuffActive = true;
            isAttackBuffActive = true;
            this.getInventory().remove(consumableItem);
        }
        else if (itemId.equals("14"))
        {
            this.health += 2;
            this.getInventory().remove(consumableItem);
        }
        else if (itemId.equals("15"))
        {
            buffName = "Speed";
            spped = SPEED_BUFF;
            consumableStartTime = System.currentTimeMillis();
            consumableDuration = 3000;
            isBuffActive = true;
            this.getInventory().remove(consumableItem);
        }
    }

    public void removePassiveEffect()
    {
        if (appliedPassiveID == null || appliedPassiveID.isBlank())
        {
            appliedPassiveID = "";
            return;
        }

        if (appliedPassiveID.equals("21"))
        {
            this.health -= 1;
        }
        else if (appliedPassiveID.equals("22"))
        {
            this.mana = Math.max(0, this.mana - 20);
        }
        else if (appliedPassiveID.equals("23"))
        {
            updateWeaponBuffFlags(false, true);
        }
        else if (appliedPassiveID.equals("24"))
        {
            updateWeaponBuffFlags(false, false);
        }
        else if (appliedPassiveID.equals("25"))
        {
            this.spped = Math.max(NORMAL_SPEED, this.spped - 2);
        }

        appliedPassiveID = "";
    }

    public void updatePassiveEffect()
    {
        if (this.getInventory() == null)
        {
            return;
        }

        Item passiveItem = this.getInventory().getItems()[3];
        String currentPassiveID = "";

        if (passiveItem instanceof Passive)
        {
            currentPassiveID = passiveItem.getItemID();
        }

        if (!appliedPassiveID.equals(currentPassiveID))
        {
            removePassiveEffect();

            if (currentPassiveID.equals("21"))
            {
                this.health += 1;
            }
            else if (currentPassiveID.equals("22"))
            {
                this.mana += 20;
            }
            else if (currentPassiveID.equals("23"))
            {
                updateWeaponBuffFlags(true, true);
            }
            else if (currentPassiveID.equals("24"))
            {
                updateWeaponBuffFlags(true, false);
            }
            else if (currentPassiveID.equals("25"))
            {
                this.spped += 2;
            }

            appliedPassiveID = currentPassiveID;
        }
    }

    private void updateWeaponBuffFlags(boolean enabled, boolean attackBuff)
    {
        if (this.getInventory().getItems()[0] instanceof Weapon)
        {
            Weapon weapon0 = (Weapon) this.getInventory().getItems()[0];
            if (attackBuff)
            {
                weapon0.ADbuff = enabled;
            }
            else
            {
                weapon0.ASbuff = enabled;
            }
        }

        if (this.getInventory().getItems()[1] instanceof Weapon)
        {
            Weapon weapon1 = (Weapon) this.getInventory().getItems()[1];
            if (attackBuff)
            {
                weapon1.ADbuff = enabled;
            }
            else
            {
                weapon1.ASbuff = enabled;
            }
        }
    }

    public void swapWeapons()
    {
        this.getInventory().switchWeapon();
        syncEquippedWeapons();
        isSwapping = true;
        swapCounter = 0;

        Weapon currentWeapon = getChoosenWeapon();
        if (currentWeapon != null)
        {
            System.out.println("Swap! " + currentWeapon.getName());
        }
    }

    @Override
    public void update()
    {
        updatePassiveEffect();
        syncEquippedWeapons();

        

        for (int i=0; i<this.getInventory().getItems().length; i++)
        {
            if(this.getInventory().getItems()[i] != null)
            {
                this.getInventory().getItems()[i].update();
            }
        }

        if (keyH.spacePressed)
        {
            dash();
        }
        if (keyH.attackPressed)
        {
            attack();
            keyH.attackPressed = false;
        }
        if (keyH.kPressed)
        {
            parry();
        }

        if (keyH.ePressed && !isInteracting)
        {
            interact();
            isInteracting = true;
        }
        if (!keyH.ePressed)
        {
            isInteracting = false;
        }
        if (keyH.cPressed && !isConsuming)
        {
            consumeItem();
            isConsuming = true;
        }
        if (!keyH.cPressed)
        {
            isConsuming = false;
        }
        if (keyH.switchWeaponPressed && !isSwapping)
        {
            swapWeapons();
            keyH.switchWeaponPressed = false;
        }

        if (isSwapping)
        {
            swapCounter++;
            if (swapCounter > 20)
            {
                isSwapping = false;
                swapCounter = 0;
            }
        }

        if (isBuffActive)
        {
            long currentTime = System.currentTimeMillis();

            if (currentTime - consumableStartTime >= consumableDuration)
            {
                if (buffName.equals("Speed"))
                {
                    if (!isDashing)
                    {
                        spped = NORMAL_SPEED;
                        if (appliedPassiveID.equals("25"))
                        {
                            spped += 2;
                        }
                    }
                }
                else if (buffName.equals("Attack"))
                {
                    isAttackBuffActive = false;
                }

                buffName = "";
                consumableDuration = 0;
                isBuffActive = false;
            }
        }

        if (isDashing)
        {
            dashCounter++;
            if (dashCounter > 10)
            {
                isDashing = false;
                dashCounter = 0;
                spped = NORMAL_SPEED;
                if (isBuffActive && buffName.equals("Speed"))
                {
                    spped = SPEED_BUFF;
                }
                if (appliedPassiveID.equals("25"))
                {
                    spped += 2;
                }
            }
        }
        if (isAttacking)
        {
            attackCounter++;
            
            isMoving = false;

            Weapon weapon = getChoosenWeapon();
            if (weapon != null)
            {
                if (attackCounter >= weapon.getDamageStartFrame()
                    && attackCounter <= weapon.getDamageEndFrame())
                {
                    checkAttackHit();
                }

                if (attackCounter > weapon.getAttackDuration())
                {
                    isAttacking = false;
                    attackCounter = 0;
                    
                    damageAppliedForThisAttack = false;
                }
            }
            else
            {
                isAttacking = false;
                attackCounter = 0;
            }
        }
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed)
        {
            isMoving = true;
            move();
        }
        else
        {
            isMoving = false;
        }

        if (isParrying)
        {
            parryCounter++;
            if (parryCounter > PARRY_DURATION)
            {
                isParrying = false;
                parryCounter = 0;
            }
        }

        spriteCounter++;
        if (spriteCounter > 5)
        {
            SpriteNum++;
            if (SpriteNum >= 8)
            {
                SpriteNum = 0;
            }
            spriteCounter = 0;
        }

        if (mana < 100)
        {
            staminaReCounter++;
            if (staminaReCounter >= 60)
            {
                mana += 10;
                if (mana > 100)
                {
                    mana = 100;
                }
                staminaReCounter = 0;
            }
        }

        collectRewardDrop();
    }

    public void draw(Graphics2D g2)
    {
        BufferedImage cBody = null;
        BufferedImage cHair = null;
        BufferedImage cShirt = null;
        BufferedImage cBoat = null;
        BufferedImage cHelmet = null;
        BufferedImage cHead = null;
        BufferedImage cBeard = null;
        BufferedImage cPant = null;
        BufferedImage attackAnimation = null;
        int walkFrame = SpriteNum;
        int idleFrame = SpriteNum % 3;

        if (isMoving)
        {
            switch (direction)
            {
                case "up":
                    cBody = bodyUp[walkFrame];
                    cHair = hairUp[walkFrame];
                    cShirt = shirtUp[walkFrame];
                    cBoat = boatUp[walkFrame];
                    cHelmet = helmetUp[walkFrame];
                    cHead = headUp[walkFrame];
                    cBeard = beardUp[walkFrame];
                    cPant = pantUp[walkFrame];
                    break;
                case "down":
                    cBody = bodyDown[walkFrame];
                    cHair = hairDown[walkFrame];
                    cShirt = shirtDown[walkFrame];
                    cBoat = boatDown[walkFrame];
                    cHelmet = helmetDown[walkFrame];
                    cHead = headDown[walkFrame];
                    cBeard = beardDownn[walkFrame];
                    cPant = pantDown[walkFrame];
                    break;
                case "left":
                    cBody = bodyLeft[walkFrame];
                    cHair = hairLeft[walkFrame];
                    cShirt = shirtLeft[walkFrame];
                    cBoat = boatLeft[walkFrame];
                    cHelmet = helmetLeft[walkFrame];
                    cHead = headLeft[walkFrame];
                    cBeard = beardLeft[walkFrame];
                    cPant = pantLeft[walkFrame];
                    break;
                case "right":
                    cBody = bodyRight[walkFrame];
                    cHair = hairRight[walkFrame];
                    cShirt = shirtRight[walkFrame];
                    cBoat = boatRight[walkFrame];
                    cHelmet = helmetRight[walkFrame];
                    cHead = headRight[walkFrame];
                    cBeard = beardRight[walkFrame];
                    cPant = pantRight[walkFrame];
                    break;
                default:
                    break;
            }
        }
        else
        {
            switch (direction)
            {
                case "up":
                    cBody = idlebodyUp[idleFrame];
                    cShirt = idleshirtUp[idleFrame];
                    cHelmet = idlehelmetUp[idleFrame];
                    cHead = idleheadUp[idleFrame];
                    cPant = idlepantUp[idleFrame];
                    break;
                case "down":
                    cBody = idlebodyDown[idleFrame];
                    cShirt = idleshirtDown[idleFrame];
                    cHelmet = idlehelmetDown[idleFrame];
                    cHead = idleheadDown[idleFrame];
                    cPant = idlepantDown[idleFrame];
                    break;
                case "left":
                    cBody = idlebodyLeft[idleFrame];
                    cShirt = idleshirtLeft[idleFrame];
                    cHelmet = idlehelmetLeft[idleFrame];
                    cHead = idleheadLeft[idleFrame];
                    cPant = idlepantLeft[idleFrame];
                    break;
                case "right":
                    cBody = idlebodyRight[idleFrame];
                    cShirt = idleshirtRight[idleFrame];
                    cHelmet = idlehelmetRight[idleFrame];
                    cHead = idleheadRight[idleFrame];
                    cPant = idlepantRight[idleFrame];
                    break;
                default:
                    break;
            }
        }

        if (isAttacking && getChoosenWeapon() != null)
        {
            attackAnimation = getChoosenWeapon().getSprite(direction);
        }

        if ("up".equals(direction) && attackAnimation != null)
        {
            drawAttackAnimation(g2, attackAnimation);
        }

        drawLayer(g2, cBody);
        drawLayer(g2, cHead);
        drawLayer(g2, cHair);
        drawLayer(g2, cBeard);
        drawLayer(g2, cShirt);
        drawLayer(g2, cPant);
        drawLayer(g2, cBoat);
        drawLayer(g2, cHelmet);

        if (!"up".equals(direction) && attackAnimation != null)
        {
            drawAttackAnimation(g2, attackAnimation);
        }
    }

    private void drawLayer(Graphics2D g2, BufferedImage layer)
    {
        if (layer != null)
        {
            g2.drawImage(layer, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);
        }
    }

    private void drawAttackAnimation(Graphics2D g2, BufferedImage attackAnimation)
    {
        if (attackAnimation == null || getChoosenWeapon() == null)
        {
            return;
        }

        int weaponX = xCoord;
        int weaponY = yCoord;

        int scale = getChoosenWeapon().getVisualScale();
        int drawWidth = attackAnimation.getWidth() * scale;
        int drawHeight = attackAnimation.getHeight() * scale;
        int drawOffset = getChoosenWeapon().getDrawOffset();

        int AnimationOffsetX = 0;
        int AnimationOffsetY = 0;

        switch (direction)
        {
            case "up":
                weaponY = yCoord - drawHeight + drawOffset;
                weaponX = xCoord - 20 + (overlay.tileSize - drawWidth) / 2;
                attackAnimation = rotateImage(attackAnimation, -90);
                AnimationOffsetX = -40;
                AnimationOffsetY = 0;
                break;
            case "down":
                weaponY = yCoord + overlay.tileSize - drawOffset;
                weaponX = xCoord + (overlay.tileSize - drawWidth) / 2;
                attackAnimation = rotateImage(attackAnimation, 90);
                AnimationOffsetX = 40;
                AnimationOffsetY = 0;
                break;
            case "left":
                weaponY = yCoord + (overlay.tileSize - drawHeight) / 2;
                weaponX = xCoord - 20 - drawWidth + drawOffset;
                attackAnimation = rotateImage(attackAnimation, 180);
                AnimationOffsetX = 0;
                AnimationOffsetY = 40;
                break;
            case "right":
                weaponY = yCoord + (overlay.tileSize - drawHeight) / 2;
                weaponX = xCoord + overlay.tileSize - drawOffset;
                attackAnimation = rotateImage(attackAnimation, 0);
                AnimationOffsetX = 0;
                AnimationOffsetY = -40;
                break;
            default:
                break;
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);


        Weapon weapon = getChoosenWeapon();
        if(attackCounter < weapon.getAttackDuration()/3)
        {
            BufferedImage rotate1 = rotateImage(attackAnimation, 0);
            g2.drawImage(rotate1, weaponX + AnimationOffsetX, weaponY + AnimationOffsetY, drawWidth, drawHeight, null);
        }
        else if(attackCounter < (2*weapon.getAttackDuration())/3)
        {
            BufferedImage rotate2 = rotateImage(attackAnimation, 45);
            g2.drawImage(rotate2, weaponX, weaponY, (int)(drawWidth*Math.sqrt(2)), (int)(drawHeight*Math.sqrt(2)), null);
        }
        else if (attackCounter < weapon.getAttackDuration())
        {
            if(AnimationOffsetX == 40)
            {
                AnimationOffsetX = -40;
            }
            else if(AnimationOffsetX == -40)
            {
                AnimationOffsetX = 40;
            }

            if(AnimationOffsetY == 40)
            {
                AnimationOffsetY = -40;
            }
            else if(AnimationOffsetY == -40)
            {
                AnimationOffsetY = 40;
            }

            BufferedImage rotate3 = rotateImage(attackAnimation, 90);
            g2.drawImage(rotate3, weaponX + AnimationOffsetX, weaponY + AnimationOffsetY, drawWidth, drawHeight, null);
        }
    }

    public BufferedImage rotateImage(BufferedImage inputImage, double degrees) {
    double rads = Math.toRadians(degrees);
    double sin = Math.abs(Math.sin(rads));
    double cos = Math.abs(Math.cos(rads));
    
    int w = inputImage.getWidth();
    int h = inputImage.getHeight();
    
    
    int newWidth = (int) Math.floor(w * cos + h * sin);
    int newHeight = (int) Math.floor(h * cos + w * sin);

    BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = rotated.createGraphics();
    
    
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    
    g2d.translate((newWidth - w) / 2, (newHeight - h) / 2);
    
    
    g2d.rotate(rads, w / 2.0, h / 2.0);
    
    g2d.drawRenderedImage(inputImage, null);
    g2d.dispose();

    return rotated;
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public Weapon getChoosenWeapon()
    {
        if (inventory == null)
        {
            return null;
        }

        int index = inventory.getChoosedWeaponIndex();
        Item[] items = inventory.getItems();
        if (index < 0 || index >= items.length)
        {
            return null;
        }
        if (!(items[index] instanceof Weapon))
        {
            return null;
        }
        return (Weapon) items[index];
    }

    public void changeCurrency(int amount)
    {
        this.currency = currency - amount;
    }

    public void addCurrency(int amount)
    {
        if (amount > 0)
        {
            this.currency += amount;
        }
    }

    public int getCurrency()
    {
        return currency;
    }

    public String getPlayerClass()
    {
        return playerClass;
    }

    public void discoverItem(Item item)
    {
        if (item != null)
        {
            discoverItemId(item.getItemID());
        }
    }

    public void discoverItemId(String itemId)
    {
        if (itemId != null && !itemId.isBlank())
        {
            discoveredItemIds.add(itemId);
        }
    }

    public List<String> getDiscoveredShopItemIds()
    {
        List<String> discoveredShopItems = new ArrayList<>();
        for (String itemId : discoveredItemIds)
        {
            if (ItemCatalog.isShopEligible(itemId))
            {
                discoveredShopItems.add(itemId);
            }
        }
        return discoveredShopItems;
    }

    public List<String> getDiscoveredItemIds()
    {
        return new ArrayList<>(discoveredItemIds);
    }

    public void restoreDiscoveredItemIds(String[] itemIds)
    {
        discoveredItemIds.clear();
        discoveredItemIds.addAll(ItemCatalog.getDefaultShopPool());

        if (itemIds == null)
        {
            return;
        }

        for (String itemId : itemIds)
        {
            discoverItemId(itemId);
        }
    }

    public void refreshInventoryState()
    {
        appliedPassiveID = "";
        updatePassiveEffect();
        syncEquippedWeapons();
    }

    private void syncEquippedWeapons()
    {
        if (inventory == null)
        {
            return;
        }

        Item[] inventoryItems = inventory.getItems();
        for (int i = 0; i < 2 && i < inventoryItems.length; i++)
        {
            if (inventoryItems[i] instanceof Weapon)
            {
                ((Weapon) inventoryItems[i]).isEquipped = (i == inventory.getChoosedWeaponIndex());
            }
        }
    }

    private void collectRewardDrop()
    {
        if (overlay.currentRoom == null)
        {
            return;
        }

        RewardDrop rewardDrop = overlay.currentRoom.getRewardDrop();
        if (rewardDrop == null)
        {
            return;
        }

        Rectangle playerBox = getSolidArea();
        Rectangle rewardBounds = rewardDrop.getBounds(overlay.tileSize);

        if (playerBox == null || !playerBox.intersects(rewardBounds))
        {
            return;
        }

        if (rewardDrop.isCoinReward())
        {
            addCurrency(rewardDrop.getCoinAmount());
            overlay.currentRoom.clearRewardDrop();
            return;
        }

        Item itemReward = rewardDrop.getItemReward();
        if (itemReward == null || inventory == null || !inventory.canAdd(itemReward))
        {
            return;
        }

        itemReward.setPlayer(this);
        inventory.add(itemReward);
        discoverItem(itemReward);
        overlay.currentRoom.clearRewardDrop();
    }
    public void playMusic(int i)
    {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void playSoundEffect(int i)
    {
        sound.setFile(i);
        sound.play();
    }
    public void playDifferentMusic(int i)
    {
        sound.stop();
        sound.close();
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public String getDifficulty() {
        return difficulty;
    }
}
