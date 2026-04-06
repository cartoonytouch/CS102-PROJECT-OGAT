package Entities.Characters;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entities.Entity;
import Renderers.DynamicOverlay;

public abstract class GameCharacter extends Entity {

    public DynamicOverlay overlay;

    public int health;
    public boolean isStunned;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;
    
    public int spriteCounter = 0;
    public int SpriteNum = 1;

    public abstract void move();
    public abstract void attack();
    public abstract void takeDamage(int amount);

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
            g2.drawImage(img, xCoord, yCoord, overlay.tileSize, overlay.tileSize, null);

        }

    }
}
