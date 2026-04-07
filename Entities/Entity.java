package Entities;

import java.awt.Rectangle;

import Interfaces.Updatable;



public abstract class Entity implements Updatable {

    public int xCoord;
    public int yCoord;
    public int spped;

    public Rectangle solidArea;
}
