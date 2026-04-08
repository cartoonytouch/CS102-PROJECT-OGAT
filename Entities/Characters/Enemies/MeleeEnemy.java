package Entities.Characters.Enemies;

import HelperClasses.Vector2D;

import java.awt.*;

public class MeleeEnemy extends Enemy{
    public MeleeEnemy(int gridX, int gridY){
        super(gridX, gridY);
        health = 1;
        detectionRange = 0;
        attackPattern = 1;
        spped = 1;
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
    }

    @Override
    public void update()
    {
        this.solidArea = new Rectangle(xCoord, yCoord, overlay.tileSize, overlay.tileSize);
        double dx = overlay.player.xCoord - this.xCoord;
        double dy = overlay.player.yCoord - this.yCoord;
  
        Vector2D direction = new Vector2D(dx, dy);
        direction.normalize();
        direction.scale(this.spped);
        this.xCoord += direction.x;
        this.yCoord += direction.y;

        if(this.solidArea.intersects(overlay.player.solidArea))
        {
            overlay.player.takeDamage(1);
        }
    }
}
