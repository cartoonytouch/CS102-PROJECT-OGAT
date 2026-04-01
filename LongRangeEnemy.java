
import java.io.IOException;
import javax.imageio.ImageIO;

public class LongRangeEnemy extends Enemy{

    public LongRangeEnemy(GamePanel gp){

        this.gp = gp;
        setDefault();
        getEnemyImg();
        
    }

    public void setDefault(){

        xCoord = 500;
        yCoord = 100;
        spped = 0;
        direction = "down";

        health = 30;

        detectionRange = 350;
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

    public void stationaryAttack(){

    }

    @Override
    public void move() {
    }

    @Override
    public void attack() {

        stationaryAttack();
    }

    @Override
    public void takeDamage(int amount) {

        health -= amount;
    }
    
    @Override
    public void update(){

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
    }

    @Override
    public void calculatePath(int targetX, int targetY) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculatePath'");
    }

    @Override
    public boolean checkCollision() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkCollision'");
    }
    
}
