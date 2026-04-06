
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import Renderers.DynamicOverlay;

public class Heart {

    DynamicOverlay overlay;
    BufferedImage heart_full, heart_empty;

    public Heart(DynamicOverlay overlay){

        this.overlay = overlay;
        getHeartImages();
    }

    public void getHeartImages(){

        try{

            heart_full = ImageIO.read(getClass().getResourceAsStream("/hearts/heart_fll.png"));
            heart_empty = ImageIO.read(getClass().getResourceAsStream("/hearts/heart_emp.png"));
            
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g2){

        drawPlayerLife(g2);
    }

    public void drawPlayerLife(Graphics2D g2){

        int x = overlay.tileSize/2;
        int y = overlay.tileSize/2;

        if(overlay.player.health >= 1){
            g2.drawImage(heart_full, x,y, overlay.tileSize , overlay.tileSize,null);
        }
        else{
            g2.drawImage(heart_empty, x,y, overlay.tileSize , overlay.tileSize,null);
        }

        x += overlay.tileSize;

        if(overlay.player.health >= 2){
            g2.drawImage(heart_full, x,y, overlay.tileSize , overlay.tileSize,null);
        }
        else{
            g2.drawImage(heart_empty, x,y, overlay.tileSize , overlay.tileSize,null);
        }

        x += overlay.tileSize;

        if(overlay.player.health >= 3){
            g2.drawImage(heart_full, x,y, overlay.tileSize , overlay.tileSize,null);
        }
        else{
            g2.drawImage(heart_empty, x,y, overlay.tileSize , overlay.tileSize,null);
        }
    }
    
}
