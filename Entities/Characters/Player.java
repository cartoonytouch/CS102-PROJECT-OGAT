package Entities.Characters;

import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entities.StaticEntities.Door;
import Entities.StaticEntities.Rock;
import Entities.StaticEntities.Wall;
import HelperClasses.KeyHandler;
import Items.Item;
import Renderers.DynamicOverlay;

public class Player extends GameCharacter {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public boolean canStun;

    public int mana;
    public int currency;
    public int inventoryLimit;

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
    public BufferedImage[] beardDownn= new BufferedImage[8];
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

    public Player(DynamicOverlay overlay, KeyHandler keyH){

        this.overlay = overlay;
        this.keyH = keyH;
        this.mana = overlay.tileSize * overlay.maxScreenCol;

        solidArea = new Rectangle(10, 20, overlay.tileSize - 20, overlay.tileSize - 20);

        screenX = overlay.screenWidth / 2 - (overlay.tileSize / 2);
        screenY = overlay.screenHeight / 2 - (overlay.tileSize / 2);

        setDefault();
        getPlayerImg();

    }

    public void setDefault(){

        xCoord = overlay.screenWidth / 2;
        yCoord = overlay.screenHeight / 2;

        spped = 4;
        direction = "down";

        health = 3;
        //mana = 100;
        currency = 0;
        inventoryLimit = 5;

        canParry = true;

        SpriteNum =0;
    }

    public void getPlayerImg(){


        try{

            BufferedImage bodySheet = ImageIO.read(new File("Assets/PlayerAssets/run/body_sh.png"));
            BufferedImage pantSheet = ImageIO.read(new File("Assets/PlayerAssets/run/pant_sh.png"));
            BufferedImage headSheet = ImageIO.read(new File("Assets/PlayerAssets/run/head_sh.png"));
            //BufferedImage hairSheet = ImageIO.read(getClass().getResourceAsStream("/res/hair_sh.png"));
            BufferedImage shirtSheet = ImageIO.read(new File("Assets/PlayerAssets/run/shirt_sh.png"));
            //BufferedImage boatSheet = ImageIO.read(getClass().getResourceAsStream("/res/boat_sh.png"));
            BufferedImage helmetSheet = ImageIO.read(new File("Assets/PlayerAssets/run/helmet_sh.png"));
            //BufferedImage beardSheet = ImageIO.read(getClass().getResourceAsStream("/res/beard_sh.png"));

            BufferedImage idleBody = ImageIO.read(new File("Assets/PlayerAssets/idle/body.png"));
            BufferedImage idlePant = ImageIO.read(new File("Assets/PlayerAssets/idle/pant.png"));
            BufferedImage idleHead = ImageIO.read(new File("Assets/PlayerAssets/idle/head.png"));
            BufferedImage idleShirt = ImageIO.read(new File("Assets/PlayerAssets/idle/shirt.png"));
            BufferedImage idleHelmet = ImageIO.read(new File("Assets/PlayerAssets/idle/helmet.png"));


            int frameWidth = bodySheet.getWidth()/8;
            int frameHeight = bodySheet.getHeight()/4;

            for(int i=0; i<8; i++){

                bodyUp[i] = bodySheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                bodyDown[i] = bodySheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                bodyLeft[i] = bodySheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                bodyRight[i] = bodySheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                pantUp[i] = pantSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                pantDown[i] = pantSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                pantLeft[i] = pantSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                pantRight[i] = pantSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                headUp[i] = headSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                headDown[i] = headSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                headLeft[i] = headSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                headRight[i] = headSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                //hairUp[i] = hairSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                //hairDown[i] = hairSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                //hairLeft[i] = hairSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                //hairRight[i] = hairSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                shirtUp[i] = shirtSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                shirtDown[i] = shirtSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                shirtLeft[i] = shirtSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                shirtRight[i] = shirtSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                //boatUp[i] = boatSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                //boatDown[i] = boatSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                //boatLeft[i] = boatSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                //boatRight[i] = boatSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                helmetUp[i] = helmetSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                helmetDown[i] = helmetSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                helmetLeft[i] = helmetSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                helmetRight[i] = helmetSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                //beardUp[i] = beardSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                //beardDonwn[i] = beardSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                //beardLeft[i] = beardSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                //beardRight[i] = beardSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

            }

            for(int i=0; i<3; i++){

                idlebodyUp[i] = idleBody.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                idlebodyDown[i] = idleBody.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                idlebodyLeft[i] = idleBody.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                idlebodyRight[i] = idleBody.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                idlepantUp[i] = idlePant.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                idlepantDown[i] = idlePant.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                idlepantLeft[i] = idlePant.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                idlepantRight[i] = idlePant.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                idleheadUp[i] = idleHead.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                idleheadDown[i] = idleHead.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                idleheadLeft[i] = idleHead.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                idleheadRight[i] = idleHead.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                //hairUp[i] = hairSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                //hairDown[i] = hairSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                //hairLeft[i] = hairSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                //hairRight[i] = hairSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                idleshirtUp[i] = idleShirt.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                idleshirtDown[i] = idleShirt.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                idleshirtLeft[i] = idleShirt.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                idleshirtRight[i] = idleShirt.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                //boatUp[i] = boatSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                //boatDown[i] = boatSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                //boatLeft[i] = boatSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                //boatRight[i] = boatSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                idlehelmetUp[i] = idleHelmet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                idlehelmetDown[i] = idleHelmet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                idlehelmetLeft[i] = idleHelmet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                idlehelmetRight[i] = idleHelmet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

                //beardUp[i] = beardSheet.getSubimage(i*frameWidth, 0*frameHeight, frameWidth, frameHeight);
                //beardDonwn[i] = beardSheet.getSubimage(i*frameWidth, 2*frameHeight, frameWidth, frameHeight);
                //beardLeft[i] = beardSheet.getSubimage(i*frameWidth, 1*frameHeight, frameWidth, frameHeight);
                //beardRight[i] = beardSheet.getSubimage(i*frameWidth, 3*frameHeight, frameWidth, frameHeight);

            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void move() {
        int deltaX = 0;
        int deltaY = 0;

        if (keyH.leftPressed && !keyH.rightPressed) {
            deltaX -= spped;
        } else if (keyH.rightPressed && !keyH.leftPressed) {
            deltaX += spped;
        }

        if (keyH.upPressed && !keyH.downPressed) {
            deltaY -= spped;
        } else if (keyH.downPressed && !keyH.upPressed) {
            deltaY += spped;
        }

        if (deltaX == 0 && deltaY == 0) {
            updateInvisibility();
            return;
        }

        if (deltaY < 0) {
            direction = "up";
        } else if (deltaY > 0) {
            direction = "down";
        } else if (deltaX < 0) {
            direction = "left";
        } else if (deltaX > 0) {
            direction = "right";
        }

        if (deltaX != 0 && deltaY != 0) {
            double diagonalScale = 1.0 / Math.sqrt(2.0);
            deltaX = (int) Math.round(deltaX * diagonalScale);
            deltaY = (int) Math.round(deltaY * diagonalScale);

            if (deltaX == 0) {
                deltaX = keyH.leftPressed ? -1 : 1;
            }
            if (deltaY == 0) {
                deltaY = keyH.upPressed ? -1 : 1;
            }
        }

        boolean changedRoom = false;

        if (deltaX != 0) {
            changedRoom = attemptMove(deltaX, 0);
        }
        if (!changedRoom && deltaY != 0) {
            attemptMove(0, deltaY);
        }

        updateInvisibility();
    }

    private boolean attemptMove(int deltaX, int deltaY)
    {
        int nextX = xCoord + deltaX;
        int nextY = yCoord + deltaY;
        Rectangle boxPlayer = createCollisionBox(nextX, nextY);

        if (collidesWithStaticRoomObjects(boxPlayer)) {
            return false;
        }

        if (overlay.currentRoom != null && overlay.currentRoom.placedDoors != null) {
            for (Door d : overlay.currentRoom.placedDoors) {
                Rectangle doorRect = new Rectangle(d.coordX * overlay.tileSize, d.coordY * overlay.tileSize, overlay.tileSize, overlay.tileSize);

                if (boxPlayer.intersects(doorRect)) {
                    if (!d.open) {
                        return false;
                    }

                    if (d.coordX == overlay.currentRoom.width - 1) {
                        overlay.changeRoom(overlay.curGridX + 1, overlay.curGridY, "right");
                    } else if (d.coordX == 0) {
                        overlay.changeRoom(overlay.curGridX - 1, overlay.curGridY, "left");
                    } else if (d.coordY == 0) {
                        overlay.changeRoom(overlay.curGridX, overlay.curGridY - 1, "up");
                    } else if (d.coordY == overlay.currentRoom.height - 1) {
                        overlay.changeRoom(overlay.curGridX, overlay.curGridY + 1, "down");
                    }
                    return true;
                }
            }
        }

        xCoord = nextX;
        yCoord = nextY;
        return false;
    }

    private Rectangle createCollisionBox(int proposedX, int proposedY)
    {
        return new Rectangle(proposedX + 24, proposedY + 40, overlay.tileSize - 48, overlay.tileSize - 40);
    }

    private boolean collidesWithStaticRoomObjects(Rectangle boxPlayer)
    {
        if (overlay.currentRoom == null) {
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

        for (int r = topRow; r <= bottomRow; r++) {
            for (int c = leftCol; c <= rightCol; c++) {
                Object obj = overlay.currentRoom.localObjectGrid[r][c];
                if (obj instanceof Wall || obj instanceof Rock) {
                    return true;
                }
            }
        }

        return false;
    }

    private void updateInvisibility()
    {
        if(isInvisible == true){

            invisibleCounter++;
            if(invisibleCounter > 60){
                isInvisible = false;
                invisibleCounter = 0;
            }
        }
    }

    @Override
    public void attack() {

        if(isAttacking == false){

            isAttacking = true;
            System.out.println("Attack!");
        }
    }

    @Override
    public void takeDamage(int amount) {

        health-= amount;
    }

    public void dash(){

        if(mana >= 20 && isDashing == false){

            isDashing = true;
            mana -= 20;
            spped = 12;

        }
        
    }

    public void parry(){

        if(mana >= 10 && isParrying == false){

            isParrying = true;
            mana -= 10;
            System.out.println("Parry!");
        }
    }

    public void interact(){

        // int distanceX = Math.abs(xCoord - gp.chest.x);
        // int distanceY = Math.abs(yCoord - gp.chest.y);

        // int reachable = gp.tileSize + 10;

        // if(distanceX <= reachable && distanceY <= reachable){

        //     gp.chest.Interact();
        // }
    }

    public void consumeItem(){
        System.out.println("Consume!");
    }


    public void update(){

        if(keyH.spacePressed == true){
            dash();
        }
        if(keyH.jPressed == true){
            attack();
        }
        if(keyH.kPressed == true){
            parry();
        }

        if(keyH.ePressed == true && isInteracting == false){
            interact();
            isInteracting = true;
        }
        if(keyH.ePressed == false){
            isInteracting = false;
        }
        if(keyH.cPressed == true && isConsuming == false){
            consumeItem();
            isConsuming = true;
        }
        if(keyH.cPressed == false){
            isConsuming = false;
        }

        if(isDashing == true){
            dashCounter++;

            if(dashCounter > 10){
                isDashing = false;
                dashCounter = 0;
                spped = 4;
            }
        }
        if(isAttacking == true){
            attackCounter++;

            if(attackCounter > 20){
                isAttacking = false;
                attackCounter = 0;
            }
        }
        if(isParrying == true){
            parryCounter++;

            if(parryCounter > 10){
                isParrying = false;
                parryCounter = 0;
            }
        }

        if(keyH.upPressed == true || keyH.downPressed == true 
            || keyH.leftPressed == true || keyH.rightPressed == true){

            isMoving = true;
            move();
        }
        else{

            isMoving = false;
        }

        spriteCounter++;
        if(spriteCounter > 5){

            SpriteNum++;
            if(SpriteNum >= 8){
                SpriteNum = 0;
            }
            spriteCounter = 0;
        }
        if(mana < 100){

            staminaReCounter++;

            if(staminaReCounter >= 60){

                mana += 10;

                if(mana > 100){
                    mana = 100;
                }

                staminaReCounter = 0;

            }

        }
    }   


    public void draw(Graphics2D g2){

        BufferedImage cBody = null, cHair = null, cShirt = null, cBoat = null, cHelmet = null, cHead = null, cBeard = null, cPant = null;
        int walkFrame = SpriteNum;
        int idleFrame = SpriteNum % 3;


        if(isMoving == true){

            switch(direction){
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
        }

        }
        else{

            switch(direction){
        case "up":

            cBody = idlebodyUp[idleFrame];
            //cHair = idlehairUp[SpriteNum];
            cShirt = idleshirtUp[idleFrame];
            //cBoat = idleboatUp[SpriteNum];
            cHelmet = idlehelmetUp[idleFrame];
            cHead = idleheadUp[idleFrame];
            //cBeard = idlebeardUp[SpriteNum];
            cPant = idlepantUp[idleFrame];
            break;

        case "down":

            cBody = idlebodyDown[idleFrame];
            //cHair = hairDown[SpriteNum];
            cShirt = idleshirtDown[idleFrame];
            //cBoat = boatDown[SpriteNum];
            cHelmet = idlehelmetDown[idleFrame];
            cHead = idleheadDown[idleFrame];
            //cBeard = beardDonwn[SpriteNum];
            cPant = idlepantDown[idleFrame];
            break;

        case "left":

            cBody = idlebodyLeft[idleFrame];
            //cHair = hairLeft[SpriteNum];
            cShirt = idleshirtLeft[idleFrame];
            //cBoat = boatLeft[SpriteNum];
            cHelmet = idlehelmetLeft[idleFrame];
            cHead = idleheadLeft[idleFrame];
            //cBeard = beardLeft[SpriteNum];
            cPant = idlepantLeft[idleFrame];
            break;

        case "right":

            cBody = idlebodyRight[idleFrame];
            //cHair = hairRight[SpriteNum];
            cShirt = idleshirtRight[idleFrame];
            //cBoat = boatRight[SpriteNum];
            cHelmet = idlehelmetRight[idleFrame];
            cHead = idleheadRight[idleFrame];
            //cBeard = beardRight[SpriteNum];
            cPant = idlepantRight[idleFrame];
            break;
        }
        }

        if(direction.equals("up")){

            /*if( cWeapon != null){
                g2.drawImage(cWeapon, x, y, overlay.tileSize, overlay.tileSize, null);

            }/* */
            if( cBody != null){
                g2.drawImage(cBody, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cHead != null){
                g2.drawImage(cHead, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cHair != null){
                g2.drawImage(cHair, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cBeard != null){
                g2.drawImage(cBeard, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cShirt != null){
                g2.drawImage(cShirt, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cPant != null){
                g2.drawImage(cPant, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cBoat != null){
                g2.drawImage(cBoat, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cHelmet != null){
                g2.drawImage(cHelmet, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
        }
        else{

            if( cBody != null){
                g2.drawImage(cBody, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cHead != null){
                g2.drawImage(cHead, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cHair != null){
                g2.drawImage(cHair, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cBeard != null){
                g2.drawImage(cBeard, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cShirt != null){
                g2.drawImage(cShirt, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cPant != null){
                g2.drawImage(cPant, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cBoat != null){
                g2.drawImage(cBoat, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            if( cHelmet != null){
                g2.drawImage(cHelmet, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

            }
            /*if( cWeapon != null){
                g2.drawImage(cWeapon, x, y, overlay.tileSize, overlay.tileSize, null);

            }/* */
        }

    }
    /*public void draw(Graphics2D g2){

        //g2.setColor(Color.WHITE);

        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage img = null;

        switch(direction){
        case "up":

            if(SpriteNum == 1){
                img = up1;
            }
            if(SpriteNum == 2){
                img = up2;
            }
            break;

        case "down":

            if(SpriteNum == 1){
                img = down1;
            }
            if(SpriteNum == 2){
                img = down2;
            }
            break;

        case "left":

            if(SpriteNum == 1){
                img = left1;
            }
            if(SpriteNum == 2){
                img = left2;
            }
            break;

        case "right":

            if(SpriteNum == 1){
                img = right1;
            }
            if(SpriteNum == 2){
                img = right2;
            }
            break;
        }

        g2.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);

    }/* */

    @Override
    public boolean checkCollision() {
        return false;
    }

    public void useItem(Item item){

    }

    public void dropItem(Item item){

    }
    
}
