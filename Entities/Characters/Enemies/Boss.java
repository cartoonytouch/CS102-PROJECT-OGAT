package Entities.Characters.Enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import java.util.ArrayList;

import Entities.Projectile;
import HelperClasses.Vector2D;


public class Boss extends Enemy{

    Random rng;
    
    long startTime;
    long attackCooldown = 0;
    public ArrayList<Projectile> projectiles = new ArrayList<>();

    public Boss(int hp, int x, int y)
    {
        super(x, y);

        health = hp;
        direction = "down";
        spped = 1;
        detectionRange = Integer.MAX_VALUE;
        attackPattern = 2;

        this.rng = new Random();
    }

    @Override
    public void attack()
    {
        if (attackPattern == 0)
        {
            return;
        }
        else if (attackPattern == 1)
        {
            if (overlay.currentRoom.localEnemies.size()<= 3)
            {
                int type = rng.nextInt(2);
                int coordX = rng.nextInt(overlay.currentRoom.width - 2) + 1;
                int coordY = rng.nextInt(overlay.currentRoom.height - 2) + 1;
                int[] coords = {coordX, coordY};

                while (!overlay.currentRoom.emptyCoord(coords))
                {
                    coordX = rng.nextInt(overlay.currentRoom.width - 2) + 1;
                    coordY = rng.nextInt(overlay.currentRoom.height - 2) + 1;
                    coords[0] = coordX;
                    coords[1] = coordY;
                }
                overlay.currentRoom.spawnEnemies(coordX, coordY, type);
            }
        }
        else if (attackPattern == 2)
        {
            spawnProjectile();
        }
    }

    @Override
    public void takeDamage(int damage)
    {
        health -= damage;

        if(health<= 0)
        {
            health = 0;
            killBoss();
        }
    }

    public void killBoss()
    {
        overlay.currentRoom.removeEnemyFromRoom(this);
    }

    public void spawnProjectile()
    {
        Projectile p = new Projectile(xCoord, yCoord, 5, overlay.player, 10);
        p.bindToOverlay(overlay);
        overlay.currentRoom.projectiles.add(p);
    }

    public void update()
    {
        if (overlay == null)
        {
            return;
        }

        move();
        Rectangle bossBox = getSolidArea();
        Rectangle playerBox = overlay.player.getSolidArea();
        if (bossBox != null && playerBox != null && bossBox.intersects(playerBox))
        {
            overlay.player.takeDamage(1);
        }

        long currentTime = System.currentTimeMillis();
        if (startTime == 0)
        {
            startTime = currentTime;
            return;
        }

        if(currentTime - startTime >= 3000)
        {
            startTime = currentTime;
            attackPattern = rng.nextInt(2) + 1;
            attack();
        }
    }

    @Override
    public void move()
    {
        if (overlay == null)
        {
            return;
        }

        double dx = overlay.player.xCoord - this.xCoord;
        double dy = overlay.player.yCoord - this.yCoord;
        if (Math.hypot(dx, dy) < 1.0)
        {
            return;
        }

        Vector2D directionVector = new Vector2D(dx, dy);
        directionVector.normalize();
        directionVector.scale(this.spped);
        this.xCoord += directionVector.x;
        this.yCoord += directionVector.y;
    }

    @Override
    public void draw(Graphics2D g2)
    {
        if (overlay == null)
        {
            return;
        }

        int size = overlay.tileSize;
        g2.setColor(new Color(85, 0, 0));
        g2.fillOval(xCoord, yCoord, size, size);

        g2.setColor(new Color(170, 20, 20));
        g2.fillOval(xCoord + (size / 8), yCoord + (size / 8), (size * 3) / 4, (size * 3) / 4);

        g2.setColor(Color.WHITE);
        g2.fillOval(xCoord + (size / 3), yCoord + (size / 3), size / 8, size / 8);
        g2.fillOval(xCoord + (size / 2), yCoord + (size / 3), size / 8, size / 8);

        g2.setColor(Color.BLACK);
        g2.fillOval(xCoord + (size / 3) + 4, yCoord + (size / 3) + 2, size / 16, size / 16);
        g2.fillOval(xCoord + (size / 2) + 4, yCoord + (size / 3) + 2, size / 16, size / 16);

        int barWidth = size;
        int barHeight = 8;
        int barX = xCoord;
        int barY = yCoord - 14;
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);
        g2.setColor(new Color(220, 30, 30));
        g2.fillRect(barX, barY, Math.max(0, (health * barWidth) / 10), barHeight);
        g2.setColor(Color.WHITE);
        g2.drawRect(barX, barY, barWidth, barHeight);
    }
}
