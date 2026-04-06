
import java.awt.Color;
import java.awt.Graphics2D;

public class Chest extends InteractableObjects {


    public Chest(){

        name = "Sandık";
        x = 300;
        y = 200;

    }

    @Override
    public void draw(Graphics2D g2, int tileSize) {

        if(isInteracted == true){
            g2.setColor(Color.GREEN);
        }
        else{
            g2.setColor(Color.ORANGE);
        }
        g2.fillRect(x, y, tileSize, tileSize);
    }

    @Override
    public void Interact() {

        if(isInteracted == false){

            isInteracted = true;
        }
        else{

        }
    }

    
    
}
