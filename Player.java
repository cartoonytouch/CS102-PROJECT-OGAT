
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import Map.Room.RoomObjects;

public class Player extends Character {

    GamePanel gp;

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

    private final Inventory inventory = new Inventory();


    public Player(GamePanel gp, KeyHandler keyH){

        this.gp = gp;
        this.keyH = keyH;
        this.mana = gp.tileSize*gp.maxScreenCol;

        solidArea = new Rectangle(10,20,gp.tileSize-20,gp.tileSize-20);

        screenX = gp.screenWidth/2 -(gp.tileSize/2);
        screenY = gp.screenHeight/2 -(gp.tileSize/2);

        setDefault();
        getPlayerImg();

    }

    public void setDefault(){

        xCoord = screenX;
        yCoord = screenY;

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

        int nextX = xCoord;
        int nextY = yCoord;



        if(keyH.upPressed == true){
            direction = "up";
            nextY -= spped;
        }
        else if(keyH.downPressed == true){
            direction = "down";
            nextY += spped;           
        }
        else if(keyH.leftPressed == true){
            direction = "left";
            nextX -= spped;
        }
        else if(keyH.rightPressed == true){
            direction = "right";
            nextX += spped;
        }

    

        Rectangle boxPlayer = createCollisionBox(nextX, nextY);

        boolean collision = false;
        boolean touchedDoor = false;


        if(gp.chest != null && isInvisible == false){

            Rectangle boxObject = new Rectangle(gp.chest.x, gp.chest.y + 15, gp.tileSize -10, gp.tileSize -15);
            if(boxPlayer.intersects(boxObject)){
                collision = true;
            }
        }

        if(gp.enemy1 != null && isInvisible == false) {

            Rectangle boxEnemy1 = new Rectangle(gp.enemy1.xCoord+10, gp.enemy1.yCoord+20, gp.tileSize-20, gp.tileSize-20);
            if(boxPlayer.intersects(boxEnemy1)){
                collision = true;
            }
        }

        if(gp.enemy2 != null && isInvisible == false){

            Rectangle boxEnemy2 = new Rectangle(gp.enemy2.xCoord+10, gp.enemy2.yCoord+20, gp.tileSize-20, gp.tileSize-20);
            if(boxPlayer.intersects(boxEnemy2)){
                collision = true;
            }
        }


        if (gp.currentRoom != null) {
            int leftCol = boxPlayer.x / gp.tileSize;
            int rightCol = (boxPlayer.x + boxPlayer.width) / gp.tileSize;
            int topRow = boxPlayer.y / gp.tileSize;
            int bottomRow = (boxPlayer.y + boxPlayer.height) / gp.tileSize;

            int maxCol = gp.currentRoom.width - 1;
            int maxRow = gp.currentRoom.height - 1;

            leftCol = Math.max(0, Math.min(leftCol, maxCol));
            rightCol = Math.max(0, Math.min(rightCol, maxCol));
            topRow = Math.max(0, Math.min(topRow, maxRow));
            bottomRow = Math.max(0, Math.min(bottomRow, maxRow));

            for (int r = topRow; r <= bottomRow; r++) {
                for (int c = leftCol; c <= rightCol; c++) {
                    Object obj = gp.currentRoom.localObjectGrid[r][c]; 
                    if (obj instanceof Map.Room.Wall || obj instanceof Map.Room.Rock) {
                        collision = true; 
                    }
                }
            }

        }

        if (gp.currentRoom != null && gp.currentRoom.placedDoors != null) {
            for (Map.Room.Door d : gp.currentRoom.placedDoors) {
                Rectangle doorRect = new Rectangle(d.coordX * gp.tileSize, d.coordY * gp.tileSize, gp.tileSize, gp.tileSize);
                
                if (boxPlayer.intersects(doorRect)) {
                    touchedDoor = true;
                    if (d.coordX == gp.currentRoom.width - 1) { 
                        gp.changeRoom(gp.curGridX + 1, gp.curGridY, "right");
                    } else if (d.coordX == 0) { 
                        gp.changeRoom(gp.curGridX - 1, gp.curGridY, "left");
                    } else if (d.coordY == 0) { 
                        gp.changeRoom(gp.curGridX, gp.curGridY - 1, "up");
                    } else if (d.coordY == gp.currentRoom.height - 1) { 
                        gp.changeRoom(gp.curGridX, gp.curGridY + 1, "down");
                    }
                    break; 
                }
            }
        }

        if(collision == false && touchedDoor == false){
            xCoord = clampXToRoom(nextX);
            yCoord = clampYToRoom(nextY);
        }

        if(isInvisible == true){

            invisibleCounter++;
            if(invisibleCounter > 60){
                isInvisible = false;
                invisibleCounter = 0;
            }
        }
        
        // if (xCoord > gp.screenWidth - gp.tileSize) { // rigth door
        //     gp.changeRoom(gp.curGridX + 1, gp.curGridY, "right");
        // }
        // else if (xCoord < 0) { // left door
        //     gp.changeRoom(gp.curGridX - 1, gp.curGridY, "left");
        // }
        // else if (yCoord < 0) { // up door
        //     gp.changeRoom(gp.curGridX, gp.curGridY - 1, "up");
        // }
        // else if (yCoord > gp.screenHeight - gp.tileSize) { // down door
        //     gp.changeRoom(gp.curGridX, gp.curGridY + 1, "down");
        // }
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

    public Inventory getInventory()
    {
        return inventory;
    }

    public int getSpeed()
    {
        return spped;
    }

    public void setSpeed(int speed)
    {
        this.spped = speed;
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


        if(isMoving == true){

            switch(direction){
        case "up":

            cBody = bodyUp[SpriteNum];
            cHair = hairUp[SpriteNum];
            cShirt = shirtUp[SpriteNum];
            cBoat = boatUp[SpriteNum];
            cHelmet = helmetUp[SpriteNum];
            cHead = headUp[SpriteNum];
            cBeard = beardUp[SpriteNum];
            cPant = pantUp[SpriteNum];
            break;

        case "down":

            cBody = bodyDown[SpriteNum];
            cHair = hairDown[SpriteNum];
            cShirt = shirtDown[SpriteNum];
            cBoat = boatDown[SpriteNum];
            cHelmet = helmetDown[SpriteNum];
            cHead = headDown[SpriteNum];
            cBeard = beardDownn[SpriteNum];
            cPant = pantDown[SpriteNum];
            break;

        case "left":

            cBody = bodyLeft[SpriteNum];
            cHair = hairLeft[SpriteNum];
            cShirt = shirtLeft[SpriteNum];
            cBoat = boatLeft[SpriteNum];
            cHelmet = helmetLeft[SpriteNum];
            cHead = headLeft[SpriteNum];
            cBeard = beardLeft[SpriteNum];
            cPant = pantLeft[SpriteNum];
            break;

        case "right":

            cBody = bodyRight[SpriteNum];
            cHair = hairRight[SpriteNum];
            cShirt = shirtRight[SpriteNum];
            cBoat = boatRight[SpriteNum];
            cHelmet = helmetRight[SpriteNum];
            cHead = headRight[SpriteNum];
            cBeard = beardRight[SpriteNum];
            cPant = pantRight[SpriteNum];
            break;
        }

        }
        else{

            switch(direction){
        case "up":

            cBody = idlebodyUp[SpriteNum];
            //cHair = idlehairUp[SpriteNum];
            cShirt = idleshirtUp[SpriteNum];
            //cBoat = idleboatUp[SpriteNum];
            cHelmet = idlehelmetUp[SpriteNum];
            cHead = idleheadUp[SpriteNum];
            //cBeard = idlebeardUp[SpriteNum];
            cPant = idlepantUp[SpriteNum];
            break;

        case "down":

            cBody = idlebodyDown[SpriteNum];
            //cHair = hairDown[SpriteNum];
            cShirt = idleshirtDown[SpriteNum];
            //cBoat = boatDown[SpriteNum];
            cHelmet = idlehelmetDown[SpriteNum];
            cHead = idleheadDown[SpriteNum];
            //cBeard = beardDonwn[SpriteNum];
            cPant = idlepantDown[SpriteNum];
            break;

        case "left":

            cBody = idlebodyLeft[SpriteNum];
            //cHair = hairLeft[SpriteNum];
            cShirt = idleshirtLeft[SpriteNum];
            //cBoat = boatLeft[SpriteNum];
            cHelmet = idlehelmetLeft[SpriteNum];
            cHead = idleheadLeft[SpriteNum];
            //cBeard = beardLeft[SpriteNum];
            cPant = idlepantLeft[SpriteNum];
            break;

        case "right":

            cBody = idlebodyRight[SpriteNum];
            //cHair = hairRight[SpriteNum];
            cShirt = idleshirtRight[SpriteNum];
            //cBoat = boatRight[SpriteNum];
            cHelmet = idlehelmetRight[SpriteNum];
            cHead = idleheadRight[SpriteNum];
            //cBeard = beardRight[SpriteNum];
            cPant = idlepantRight[SpriteNum];
            break;
        }
        }

        if(direction.equals("up")){

            /*if( cWeapon != null){
                g2.drawImage(cWeapon, x, y, gp.tileSize, gp.tileSize, null);

            }/* */
            if( cBody != null){
                g2.drawImage(cBody, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cHead != null){
                g2.drawImage(cHead, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cHair != null){
                g2.drawImage(cHair, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cBeard != null){
                g2.drawImage(cBeard, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cShirt != null){
                g2.drawImage(cShirt, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cPant != null){
                g2.drawImage(cPant, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cBoat != null){
                g2.drawImage(cBoat, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cHelmet != null){
                g2.drawImage(cHelmet, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
        }
        else{

            if( cBody != null){
                g2.drawImage(cBody, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cHead != null){
                g2.drawImage(cHead, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cHair != null){
                g2.drawImage(cHair, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cBeard != null){
                g2.drawImage(cBeard, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cShirt != null){
                g2.drawImage(cShirt, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cPant != null){
                g2.drawImage(cPant, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cBoat != null){
                g2.drawImage(cBoat, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            if( cHelmet != null){
                g2.drawImage(cHelmet, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

            }
            /*if( cWeapon != null){
                g2.drawImage(cWeapon, x, y, gp.tileSize, gp.tileSize, null);

            }/* */
        }

    }

    private Rectangle createCollisionBox(int proposedX, int proposedY)
    {
        return new Rectangle(proposedX + 24, proposedY + 40, gp.tileSize - 48, gp.tileSize - 40);
    }

    private int clampXToRoom(int proposedX)
    {
        if (gp.currentRoom == null)
        {
            return proposedX;
        }

        int minX = -24;
        int maxX = (gp.currentRoom.width * gp.tileSize) - 24 - (gp.tileSize - 48);
        return Math.max(minX, Math.min(proposedX, maxX));
    }

    private int clampYToRoom(int proposedY)
    {
        if (gp.currentRoom == null)
        {
            return proposedY;
        }

        int minY = -40;
        int maxY = (gp.currentRoom.height * gp.tileSize) - 40 - (gp.tileSize - 40);
        return Math.max(minY, Math.min(proposedY, maxY));
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
