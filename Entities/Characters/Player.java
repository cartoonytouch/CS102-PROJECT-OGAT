package Entities.Characters;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Entities.StaticEntities.Door;
import Entities.StaticEntities.Rock;
import Entities.StaticEntities.Wall;
import Entities.StaticEntities.Pit;
import HelperClasses.KeyHandler;
import Items.Inventory;
import Items.Item;
import Items.Passive;
import Items.Weapons.Weapon;
import Renderers.DynamicOverlay;

public class Player extends GameCharacter {
    KeyHandler keyH;

    public static final int inventoryLimit = 5;

    public final int screenX;
    public final int screenY;

    public boolean canStun;

    public int mana;
    public int currency;
    

    public String playerClass;
    public boolean canParry;

    public boolean isDashing = false;
    public int dashCounter = 0;
    public int staminaReCounter = 0;

    public boolean isAttacking = false;
    public int attackCounter = 0;

    public boolean isParrying = false;
    public int parryCounter = 0;

    public boolean isInteracting = false;
    public boolean isConsuming = false;

    public boolean isMoving = false;

    public boolean isInvisible = false;
    public int invisibleCounter = 0;

    public String appliedPassiveID;
    public Inventory inventory;

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
        this.overlay = overlay;
        this.keyH = keyH;
        mana = overlay.tileSize * overlay.maxScreenCol;

        solidArea = buildSolidArea();

        screenX = overlay.screenWidth / 2 - (overlay.tileSize / 2);
        screenY = overlay.screenHeight / 2 - (overlay.tileSize / 2);

        setDefault();
        getPlayerImg();
    }

    public void setDefault()
    {
        xCoord = screenX;
        yCoord = screenY;

        spped = 4;
        direction = "down";

        health = 3;
        currency = 0;
        canParry = true;
        SpriteNum = 0;
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

        if(overlay.currentRoom != null)
        {
            for (Pit pit : overlay.currentRoom.placedPits)
            {
                Rectangle pitRectangle = new Rectangle(
                    (pit.coordX * overlay.tileSize)+(((overlay.tileSize)-(overlay.tileSize/4))/2),
                    (pit.coordY * overlay.tileSize)+(((overlay.tileSize)-(overlay.tileSize/4))/2)-10,
                    overlay.tileSize/4,
                    overlay.tileSize/4
                );

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
        if (!isAttacking)
        {
            isAttacking = true;
            System.out.println("Attack!");
            ((Weapon)this.getInventory().getItems()[0]).swing();
        }
    }

    @Override
    public void takeDamage(int amount)
    {
        health -= amount;
    }

    public void dash()
    {
        if (mana >= 20 && !isDashing)
        {
            isDashing = true;
            mana -= 20;
            spped = 12;
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
    }

    public void consumeItem()
    {
        System.out.println("Consume!");
    }

    public void removePassiveEffect()
    {
        if(appliedPassiveID.equals("21"))
        {
            this.health -= 1;
        }
        else if(appliedPassiveID.equals("22"))
        {
            this.mana -= 20;
        }
        else if(appliedPassiveID.equals("23"))
        {
            if(this.getInventory().getItems()[0] instanceof Weapon)
            {
                Weapon w0 = (Weapon)this.getInventory().getItems()[0];
                w0.ADbuff = false;
            }

            if(this.getInventory().getItems()[1] instanceof Weapon)
            {
                Weapon w1 = (Weapon)this.getInventory().getItems()[1];
                w1.ADbuff = false;
            }
        }
        else if(appliedPassiveID.equals("24"))
        {
            if(this.getInventory().getItems()[0] instanceof Weapon)
            {
                Weapon w0 = (Weapon)this.getInventory().getItems()[0];
                w0.ASbuff = false;
            }

            if(this.getInventory().getItems()[1] instanceof Weapon)
            {
                Weapon w1 = (Weapon)this.getInventory().getItems()[1];
                w1.ASbuff = false;
            }
        }
        else if(appliedPassiveID.equals("25"))
        {
            this.spped -= 2;
        }

        appliedPassiveID = "";
    }

    public void updatePassiveEffect()
    {
        Item passiveItem = this.getInventory().getItems()[3];
        String currentPassiveID = "";

        if(passiveItem instanceof Passive)
        {
            currentPassiveID = passiveItem.getItemID();
        }

        if(!appliedPassiveID.equals(currentPassiveID))
        {
            removePassiveEffect();

            if(currentPassiveID.equals("21"))
            {
                this.health += 1;
            }
            else if(currentPassiveID.equals("22"))
            {
                this.mana += 20;
            }
            else if(currentPassiveID.equals("23"))
            {
                if(this.getInventory().getItems()[0] instanceof Weapon)
                {
                    Weapon w0 = (Weapon)this.getInventory().getItems()[0];
                    w0.ADbuff = true;
                }

                if(this.getInventory().getItems()[1] instanceof Weapon)
                {
                    Weapon w1 = (Weapon)this.getInventory().getItems()[1];
                    w1.ADbuff = true;
                }
            }
            else if(currentPassiveID.equals("24"))
            {
                if(this.getInventory().getItems()[0] instanceof Weapon)
                {
                    Weapon w0 = (Weapon)this.getInventory().getItems()[0];
                    w0.ASbuff = true;
                }

                if(this.getInventory().getItems()[1] instanceof Weapon)
                {
                    Weapon w1 = (Weapon)this.getInventory().getItems()[1];
                    w1.ASbuff = true;
                }
            }
            else if(currentPassiveID.equals("25"))
            {
                this.spped += 2;
            }

            appliedPassiveID = currentPassiveID;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void update()
    {
        if (keyH.spacePressed)
        {
            dash();
        }
        if (keyH.jPressed)
        {
            attack();
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

        if (isDashing)
        {
            dashCounter++;
            if (dashCounter > 10)
            {
                isDashing = false;
                dashCounter = 0;
                spped = 4;
            }
        }
        if (isAttacking)
        {
            attackCounter++;
            if (attackCounter > 20)
            {
                isAttacking = false;
                attackCounter = 0;
            }
        }
        if (isParrying)
        {
            parryCounter++;
            if (parryCounter > 10)
            {
                isParrying = false;
                parryCounter = 0;
            }
        }

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed)
        {
            isMoving = true;
            move();
        }
        else
        {
            isMoving = false;
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

        drawLayer(g2, cBody);
        drawLayer(g2, cHead);
        drawLayer(g2, cHair);
        drawLayer(g2, cBeard);
        drawLayer(g2, cShirt);
        drawLayer(g2, cPant);
        drawLayer(g2, cBoat);
        drawLayer(g2, cHelmet);
    }

    private void drawLayer(Graphics2D g2, BufferedImage layer)
    {
        if (layer != null)
        {
            g2.drawImage(layer, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);
        }
    }
}
