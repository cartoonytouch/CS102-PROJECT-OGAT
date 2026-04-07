package Entities;
import Entities.Characters.Player;

import HelperClasses.Vector2D;
import Renderers.DynamicOverlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Projectile extends Entity {

    
    private double preciseX;
    private double preciseY;

    public DynamicOverlay overlay;
    
    private Vector2D direction;
    private int damage;
    private boolean active;

    public Projectile(int startX, int startY, int speed, Player target, int damage) {
        
        this.xCoord = startX;
        this.yCoord = startY;
        this.spped = speed; 
        
        
        this.preciseX = startX;
        this.preciseY = startY;
        this.damage = damage;
        this.active = true;

        // Calculate the vector pointing from the spawn point to the target
        double dx = target.xCoord - startX;
        double dy = target.yCoord - startY;

        // Create the vector and normalize it to get a pure directional unit vector
        this.direction = new Vector2D(dx, dy);
        this.direction.normalize();

        // Set up a basic collision box
        this.solidArea = new Rectangle(0, 0, 16, 16);
        
    }

    @Override
    public void update() {
        if (!active) return;

        preciseX += direction.x * spped;
        preciseY += direction.y * spped;

        xCoord = (int) Math.round(preciseX);
        yCoord = (int) Math.round(preciseY);

        if(this.solidArea.intersects(overlay.player.solidArea))
        {
            if(isActive())
            {
                overlay.player.takeDamage(this.getDamage());
                destroy();
            }
        }
    }

    public void bindToOverlay(DynamicOverlay overlay)
    {
        this.overlay = overlay;
    }

    public void draw(Graphics2D g2) {
        if (active) {
            g2.setColor(Color.RED);
            g2.fillOval(xCoord, yCoord, solidArea.width, solidArea.height);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void destroy() {
        this.active = false;
    }
    
    public int getDamage() {
        return damage;
    }
}