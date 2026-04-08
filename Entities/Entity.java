package Entities;

import java.awt.Rectangle;

import Interfaces.Updatable;

import HelperClasses.Vector2D;



public abstract class Entity implements Updatable {

    public int xCoord;
    public int yCoord;
    public int spped;

    public Rectangle solidArea;

    public Rectangle getSolidArea()
    {
        if (solidArea == null)
        {
            return null;
        }

        return new Rectangle(
            xCoord + solidArea.x,
            yCoord + solidArea.y,
            solidArea.width,
            solidArea.height
        );
    }
}
