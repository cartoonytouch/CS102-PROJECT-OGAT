package Items.Weapons;
import java.awt.event.*;

import Items.Item;

public abstract class Weapon extends Item {
    
    private int attackDamage;
    private int attackSpeed;
    private float range;
    private int upgradeLevel;
    private long swingTime;
    public boolean ASbuff;
    public boolean ADbuff;
    public boolean isEquipped;
    KeyEvent event;
    boolean up,down,right,left = false;

    public static final int attackDamageLow = 10;
    public static final int attackDamageModerate = 20;
    public static final int attackDamageHigh = 30;

    public static final int attackSpeedLow = 1;
    public static final int attackSpeedModerate = 2;
    public static final int attackSpeedHigh = 3;

    public static final float rangeLow = 10;
    public static final float rangeModerate = 20;
    public static final float rangeHigh = 30;


    Weapon(int AD, int AS, float R)
    {
        
        this.attackDamage = AD;
        this.attackSpeed = AS;
        this.range = R;
        this.upgradeLevel = 1;
        this.ADbuff = false;
        this.ASbuff = false;

    }
    void swing()
    {
        
        long currentTime = System.currentTimeMillis();

        if(currentTime - swingTime < 30 / this.getAttackSpeed())
        {
            return;
        }

        swingTime = currentTime;

        //Hitbox detection will be implemented
        

    }

    public void update()
    {
        if(!isEquipped)
        {
            ADbuff = false;
            ASbuff = false;
        }
    }

    abstract void upgrade();



    public double getAttackDamage() {
        return attackDamage + ((ADbuff ? 1 : 0)*10);
    }

    public double getAttackSpeed() {
        return attackSpeed + (ASbuff ? 1 : 0);
    }

    public double getRange() {
        return range;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setRange(int range) {
        this.range = range;
    }
    public void setUpgradeLevel() {
        this.upgradeLevel = 2;
    }
   



}

