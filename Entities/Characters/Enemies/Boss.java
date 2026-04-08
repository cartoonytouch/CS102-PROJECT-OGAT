package Entities.Characters.Enemies;

import Map.Room.Room;

import Renderers.DynamicOverlay;

import java.util.Random;

import java.util.ArrayList;

import Entities.Projectile;


public class Boss extends Enemy{

    Random rng;
    
    long startTime;
    long attackCooldown = 0;
    public ArrayList<Projectile> projectiles = new ArrayList<>();

    public Boss(int hp, int x, int y)
    {
        super(x, y);

        health = hp;

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
        projectiles.add(p);
    }

    public void update()
    {
        if(attackCooldown == 0)
        {
            startTime = System.currentTimeMillis();
        }
        else
        {
            attackCooldown = System.currentTimeMillis() - startTime;
        }

        if(attackCooldown >= 5000)
        {
            attackCooldown = 0;
            attackPattern = rng.nextInt(2);
            attack();
        }
    }
}
