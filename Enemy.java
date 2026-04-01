
public abstract class Enemy extends Character {

    public int attackPattern;
    public int detectionRange;

    public abstract void calculatePath(int targetX, int targetY);

    
}
