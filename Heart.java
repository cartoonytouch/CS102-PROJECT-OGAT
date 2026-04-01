
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


public class Heart {

    GamePanel gp;
    BufferedImage heart_full, heart_empty;

    public Heart(GamePanel gp){

        this.gp = gp;
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

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;

        if(gp.player.health >= 1){
            g2.drawImage(heart_full, x,y, gp.tileSize , gp.tileSize,null);
        }
        else{
            g2.drawImage(heart_empty, x,y, gp.tileSize , gp.tileSize,null);
        }

        x += gp.tileSize;

        if(gp.player.health >= 2){
            g2.drawImage(heart_full, x,y, gp.tileSize , gp.tileSize,null);
        }
        else{
            g2.drawImage(heart_empty, x,y, gp.tileSize , gp.tileSize,null);
        }

        x += gp.tileSize;

        if(gp.player.health >= 3){
            g2.drawImage(heart_full, x,y, gp.tileSize , gp.tileSize,null);
        }
        else{
            g2.drawImage(heart_empty, x,y, gp.tileSize , gp.tileSize,null);
        }
    }
    
}
