package HelperClasses;
public class Vector2D {
    

    public double x;
    public double y;

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void normalize()
    {
        double lengthSquared = x*x + y*y;

        if (lengthSquared > 0.0000001f)
        {
            double inverse = 1.0f / (double) Math.sqrt(lengthSquared);

            x *= inverse;
            y *= inverse;
        }
        else 
        {
            x = 0;
            y = 0;
        }
    }

    public void add(Vector2D other)
    {
        this.x += other.x;
        this.y += other.y;
    }

    public void add(double dx, double dy) 
    {
        this.x += dx;
        this.y += dy;
    }

    public void scale(double scalar) 
    {
        this.x *= scalar;
        this.y *= scalar;
    }

    public void set(double x, double y) 
    {
        this.x = x;
        this.y = y;
    }
}

