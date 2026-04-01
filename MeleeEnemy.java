
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class MeleeEnemy extends Enemy{


    public boolean isAttacking = false;
    public int attackDown = 0;
    public int attackCounter = 0;

    public MeleeEnemy(GamePanel gp){

        this.gp = gp;
        setDefault();
        getEnemyImg();

    }

    public void setDefault(){

        xCoord = 300;
        yCoord = 300;
        spped = 2;
        direction = "down";

        health = 50;

        detectionRange = 150;
        attackPattern = 1;
    }

    public void getEnemyImg(){

        try{

            up1 = ImageIO.read(getClass().getResourceAsStream("/enmy/enmy_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/enmy/enmy_up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/enmy/enmy_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/enmy/enmy_down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/enmy/enmy_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/enmy/enmy_left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/enmy/enmy_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/enmy/enmy_right2.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void followPlayer(){

        int distanceX = Math.abs(xCoord-gp.player.xCoord);
        int distanceY = Math.abs(yCoord-gp.player.yCoord);

        if(distanceX <= detectionRange && distanceY <= detectionRange){
            
            int nextX = xCoord;
            int nextY = yCoord;

            if(gp.player.xCoord > xCoord){

                nextX += spped;
                direction = "right";
            }
            else if(gp.player.xCoord < xCoord){

                nextX -= spped;
                direction = "left";
            }

            if(gp.player.yCoord > yCoord){

                nextY += spped;
                direction = "down";
            }
            else if(gp.player.yCoord < yCoord){

                nextY -= spped;
                direction = "up";
            }

            Rectangle boxEnemy = new Rectangle(nextX+10, nextY+20, gp.tileSize-20, gp.tileSize-20);

            boolean collision = false;

            Rectangle boxPlayer = new Rectangle(gp.player.xCoord+10, gp.player.yCoord+20, gp.tileSize-20, gp.tileSize-20);

            if(boxEnemy.intersects(boxPlayer)){
                collision = true;
            }

            if(gp.chest != null){

                Rectangle boxObject = new Rectangle(gp.chest.x+5, gp.chest.y+15,gp.tileSize-10, gp.tileSize-15);
                if(boxEnemy.intersects(boxObject)){
                    collision = true;
                }
            }

            if(collision == false){

                xCoord = nextX;
                yCoord = nextY;
            }
        }




    }

    @Override
    public void move() {

        followPlayer();

    }

    @Override
    public void attack() {

    }

    @Override
    public void takeDamage(int amount) {

        health -= amount;
    }

    @Override
    public void draw(Graphics2D g2){

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

        if(img != null){
            g2.drawImage(img, xCoord, yCoord, gp.tileSize, gp.tileSize, null);

        }

    

        if(isAttacking == true){
            g2.setColor(new java.awt.Color(255,0,0,150));
            g2.fillRect(xCoord,yCoord,gp.tileSize, gp.tileSize);
        }

    }

    public void update(){

        if(isAttacking == false){

            move();

            spriteCounter++;
            if(spriteCounter > 12){
                if(SpriteNum == 1){
                    SpriteNum = 2;
                }
                else if(SpriteNum == 2){
                    SpriteNum = 1;
                }
                spriteCounter = 0;
            }

            if(attackDown > 0){

                attackDown--;
            }

            int distanceX = Math.abs(xCoord-gp.player.xCoord);
            int distanceY = Math.abs(yCoord-gp.player.yCoord);

            if(distanceX < gp.tileSize && distanceY < gp.tileSize && attackDown == 0){

                isAttacking = true;
                attackCounter = 0;
            }
        }
        else{

            attackCounter++;
            if(attackCounter == 30){

                if(gp.player.isParrying == true){
                    attackDown = 60;
                    System.out.println("paryyyyyyyy");

                }
                else{
                    if(gp.player.isInvisible == false){
                        gp.player.takeDamage(1);
                        gp.player.isInvisible = true;
                        attackDown = 60;
                    }
                }

                isAttacking = false;
                attackCounter = 0;
        
            }
        }

    }

    @Override
    public boolean checkCollision() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkCollision'");
    }

    @Override
    public void calculatePath(int targetX, int targetY) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculatePath'");
    }
    
}
