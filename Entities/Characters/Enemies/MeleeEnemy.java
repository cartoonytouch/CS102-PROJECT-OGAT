package Entities.Characters.Enemies;

public class MeleeEnemy extends Enemy{
    public MeleeEnemy(int gridX, int gridY){
        super(gridX, gridY);
        health = 1;
        detectionRange = 0;
        attackPattern = 1;
    }

    @Override
    public void calculatePath(int targetX, int targetY) {
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
    }
}
