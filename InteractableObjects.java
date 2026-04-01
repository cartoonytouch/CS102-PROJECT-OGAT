import java.awt.Graphics2D;

public abstract class InteractableObjects {

    public int x,y;
    public String name;
    public boolean isInteracted = false;

    public abstract void draw(Graphics2D g2, int tileSİze);

    public abstract void Interact();
    
}
