package Entities.Characters.Enemies;

public class LongRangeEnemy extends Enemy{

    public LongRangeEnemy(int gridX, int gridY){
        super(gridX, gridY);
        health = 1;
        detectionRange = 0;
        attackPattern = 2;
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
    }
    
    @Override
    public void attack() {
    }
}
