package Entities;
import Entities.Characters.Player;

import HelperClasses.Vector2D;
import Renderers.DynamicOverlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Projectile extends Entity {

    
    private double preciseX;
    private double preciseY;

    public DynamicOverlay overlay;
    
    private Vector2D direction;
    private int damage;
    private boolean active;
    private String type;
    private LinkedList<Node> path;
    private int repathCounter;
    private int repathRate;
    private int pathIndex;

    private class Node{

        int column;
        int row;
        int g; //cost
        int h; //estimated cost
        int f; //total cost
        Node parent;

        Node(int column,int row)
        {
            this.column = column;
            this.row = row;
        }
    }

    public Projectile(int startX, int startY, int speed, Player target, int damage,String type) {
        
        this.xCoord = startX+40;
        this.yCoord = startY+40;
        this.spped = speed;
        this.type = type;

        this.path = new LinkedList<>();
        this.repathCounter = 0;
        this.repathRate = 15;
        this.pathIndex = 0;

        
        
        this.preciseX = startX+40;
        this.preciseY = startY+40; 
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
        if (!active || overlay == null) return;
    
            if(this.type.equals("RED"))
            {
                preciseX += direction.x * spped;
                preciseY += direction.y * spped;

                xCoord = (int) Math.round(preciseX);
                yCoord = (int) Math.round(preciseY);

                Rectangle projectileBox = getSolidArea();
                Rectangle playerBox = overlay.player.getSolidArea();

                if (isOutsideRoom())
                {
                    destroy();
                    return;
                }

                if(projectileBox != null && playerBox != null && projectileBox.intersects(playerBox))
                {
                    overlay.player.takeDamage(this.getDamage());
                    destroy();
                }
            }
            else if(this.type.equals("CYAN"))
            {

                repathCounter++;

                if (repathCounter >= repathRate || path.isEmpty() || pathIndex >= path.size())
                {
                    aStar();
                    repathCounter = 0;
                }

                followPath();

                Rectangle projectileBox = getSolidArea();
                Rectangle playerBox = overlay.player.getSolidArea();

                if (isOutsideRoom())
                {
                    destroy();
                    return;
                }

                if(projectileBox != null && playerBox != null && projectileBox.intersects(playerBox))
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
            

            if(this.type.equals("RED"))
            {
                g2.setColor(Color.RED);
                g2.fillOval(xCoord, yCoord, solidArea.width, solidArea.height);
            }
            else if(this.type.equals("BLUE"))
            {
                g2.setColor(Color.BLUE);
                g2.fillOval(xCoord, yCoord, solidArea.width, solidArea.height);
            }
            else if(this.type.equals("CYAN"))
            {
                g2.setColor(Color.CYAN);
                g2.fillOval(xCoord, yCoord, solidArea.width, solidArea.height);
            }
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

    private boolean isOutsideRoom()
    {
        if (overlay.currentRoom == null)
        {
            return false;
        }

        int maxX = overlay.currentRoom.width * overlay.tileSize;
        int maxY = overlay.currentRoom.height * overlay.tileSize;

        return xCoord < -solidArea.width
            || yCoord < -solidArea.height
            || xCoord > maxX
            || yCoord > maxY;
    }
                private void aStar()
    {
        if (overlay == null || overlay.currentRoom == null)
        {
            return;
        }

        Rectangle playerBox = overlay.player.getSolidArea();

        int startColumn = xCoord / overlay.tileSize;
        int startRow = yCoord / overlay.tileSize;

        int goalColumn = (int)(playerBox.getCenterX() / overlay.tileSize);
        int goalRow = (int)(playerBox.getCenterY() / overlay.tileSize);

        Node startNode = new Node(startColumn, startRow);
        Node goalNode = new Node(goalColumn, goalRow);

        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();

        startNode.g = 0;
        startNode.h = heuristic(startNode, goalNode);
        startNode.f = startNode.g + startNode.h;

        openList.add(startNode);

        while (!openList.isEmpty())
        {
            Node current = getLowestF(openList);

            if (current.column == goalNode.column && current.row == goalNode.row)
            {
                buildPath(current);
                return;
            }

            openList.remove(current);
            closedList.add(current);

            ArrayList<Node> neighbors = getNeighbors(current);

            for (Node neighbor : neighbors)
            {
                if (findNode(closedList, neighbor.column, neighbor.row) != null)
                {
                    continue;
                }

                int tentativeG = current.g + 1;

                Node existingOpenNode = findNode(openList, neighbor.column, neighbor.row);

                if (existingOpenNode == null)
                {
                    neighbor.g = tentativeG;
                    neighbor.h = heuristic(neighbor, goalNode);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = current;

                    openList.add(neighbor);
                }
                else if (tentativeG < existingOpenNode.g)
                {
                    existingOpenNode.g = tentativeG;
                    existingOpenNode.f = existingOpenNode.g + existingOpenNode.h;
                    existingOpenNode.parent = current;
                }
            }
    }

    path.clear();   
    }
    private int euclideanDistance(int startX,int startY,int targetX,int targetY)
    {
        int h = (int)Math.sqrt(Math.pow(startX - targetX,2) + (Math.pow(startY - targetY,2))); 

        return h;
    }
    private int heuristic(Node a, Node b)
    {
        return euclideanDistance(a.column, a.row, b.column, b.row);
    }
    private Node findNode(ArrayList<Node> list, int column, int row)
    {
        for (Node node : list)
        {
            if (node.column == column && node.row == row)
            {
                return node;
            }
        }

        return null;
    }

    private Node getLowestF(ArrayList<Node> openList)
    {
        Node best = openList.get(0);

        for (Node node : openList)
        {
            if (node.f < best.f)
            {
                best = node;
            }
        }

        return best;
    }

    private boolean isWalkable(int column, int row)
    {
        if (overlay == null || overlay.currentRoom == null)
        {
            return false;
        }

        if (column < 0 || row < 0 || column >= overlay.currentRoom.width || row >= overlay.currentRoom.height)
        {
            return false;
        }

        Object obj = overlay.currentRoom.localObjectGrid[row][column];

        return !(obj instanceof Entities.StaticEntities.Wall)
            && !(obj instanceof Entities.StaticEntities.Rock);
    }

    private void addNeighbor(ArrayList<Node> neighbors, int column, int row)
    {
        if (isWalkable(column, row))
        {
            neighbors.add(new Node(column, row));
        }
    }

    private ArrayList<Node> getNeighbors(Node current)
    {
        ArrayList<Node> neighbors = new ArrayList<>();

        addNeighbor(neighbors, current.column, current.row - 1);
        addNeighbor(neighbors, current.column, current.row + 1);
        addNeighbor(neighbors, current.column - 1, current.row);
        addNeighbor(neighbors, current.column + 1, current.row);

        return neighbors;
    }

    private void buildPath(Node goalNode)
    {
        path.clear();

        Node current = goalNode;

        while (current != null)
        {
            path.addFirst(current);
            current = current.parent;
        }

        pathIndex = 1;
    }
    private void followPath()
    {
        if (path == null || path.isEmpty())
        {
            return;
        }

        double targetX;
        double targetY;

        if (pathIndex >= path.size())
        {
            Rectangle playerBox = overlay.player.getSolidArea();

            if (playerBox == null)
            {
                return;
            }

            targetX = playerBox.getCenterX();
            targetY = playerBox.getCenterY();
        }
        else
        {
            Node targetNode = path.get(pathIndex);
            targetX = targetNode.column * overlay.tileSize + overlay.tileSize / 2.0;
            targetY = targetNode.row * overlay.tileSize + overlay.tileSize / 2.0;
        }

        double dx = targetX - preciseX;
        double dy = targetY - preciseY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < spped)
        {
            preciseX = targetX;
            preciseY = targetY;

            if (pathIndex < path.size())
            {
                pathIndex++;
            }
        }
        else
        {
            preciseX += (dx / distance) * spped;
            preciseY += (dy / distance) * spped;
        }

        xCoord = (int)Math.round(preciseX);
        yCoord = (int)Math.round(preciseY);
        
            
    }
}
