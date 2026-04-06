package Map.Room;
import java.util.Random;

import Characters.Enemy;
import Items.Item;
import Interfaces.Updatable;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Room implements Updatable{
    // the grids 
    public FloorObjects[][] localFloorGrid;
    public RoomObjects[][] localObjectGrid;
    public int[][] localMiniGrid;
    

    // dimensions and the absolute location of 
    // the top left corner on the map grid.
    final public int width = 15;
    final public int height = 9;
    public int gridX;
    public int gridY;

    // some info about room
    public String type;
    boolean isCleared;
    private Random rng;
    boolean spawnedRewards;
    boolean doorsOpen;

    public boolean isDiscovered;

    // things that need to be rendered
    private List<int[]> occupiedCoords = new ArrayList<>();
    public List<Enemy> localEnemies = new ArrayList<>();
    private List<RoomObjects> placedRoomObjects = new ArrayList<>();
    private List<FloorObjects> placedFloorObjects = new ArrayList<>();
    public List<Door> placedDoors = new ArrayList<>();


    // room constructor
    public Room(String roomType, int gridX, int gridY, Random sharedRng)
    {
        // initialize the fields of the room 
        type = roomType;
        localFloorGrid = new FloorObjects[height][width];
        localObjectGrid = new RoomObjects[height][width];
        this.gridX = gridX;
        this.gridY = gridY;
        this.rng = sharedRng;
        spawnedRewards = false;

        if(type.equals("Start") == false)
        {
            doorsOpen = false;
        }

        
        // initialize if its cleared or not
        if (type.equals("Start") || type.equals("Shop") || type.equals("Empty"))
        {
            this.spawnedRewards = true;
            this.isCleared = true;
        }
        else
        {
            this.isCleared = false;
        }
        
        // 
        this.initializeLocalGrid();
    }

    // initialize the room itself
    public void initializeLocalGrid()
    {
        // generate walls
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                

                int randomNum = rng.nextInt(10) + 1;
                int type = 1;
                if(randomNum <= 5) type = 1;
                else if(randomNum <= 8) type = 2;
                else if(randomNum <= 10) type = 3;
                // corners
                if(y == 0 && x == 0)
                {
                    type = 4;
                    Wall newTopLeftEdgeWall = new Wall(x,y,'n', type);
                    localObjectGrid[y][x] = newTopLeftEdgeWall;
                }
                if(y == 0 && x == width - 1)
                {
                    type = 5;
                    Wall newTopRightEdgeWall = new Wall(x,y,'n', type);
                    localObjectGrid[y][x] = newTopRightEdgeWall;
                }
                if(y == height - 1 && x == 0)
                {
                    type = 6;
                    Wall newBottomLeftEdgeWall = new Wall(x,y,'n', type);
                    localObjectGrid[y][x] = newBottomLeftEdgeWall;
                }
                if(y == height - 1 && x == width - 1)
                {
                    type = 7;
                    Wall newBottomRightEdgeWall = new Wall(x,y,'n', type);
                    localObjectGrid[y][x] = newBottomRightEdgeWall;
                }

                // sides
                if(y == 0 && x != 0 && x != width - 1)
                {
                    Wall newNorthWall = new Wall(x,y,'n', type);
                    localObjectGrid[y][x] = newNorthWall;
                }
                if(x == width - 1 && y != 0 && y != height - 1)
                {
                    Wall newEastWall = new Wall(x,y,'e', type);
                    localObjectGrid[y][x] = newEastWall;
                }
                if(y == height - 1 && x != 0 && x != width - 1)
                {
                    Wall newSouthWall = new Wall(x,y,'s', type);
                    localObjectGrid[y][x] = newSouthWall;
                }
                if(x == 0 && y != 0 && y != height - 1)
                {
                    Wall newWestWall = new Wall(x,y,'w', type);
                    localObjectGrid[y][x] = newWestWall;
                }
            }
        }
        if (this.type.equals("Empty") || this.type.equals("Enemy"))
        {
            if (this.type.equals("Enemy"))
            {

                // place the enemies
                int enemyCount = rng.nextInt(9);
                //create a local enemy 

                for (int i = 0; i <= enemyCount; i++)
                {
                    // long range if type == 0, contact if type == 1
                    // decide the type
                    int type = rng.nextInt(2);

                    // get the coords differnt than the placed objects
                    int coordX = rng.nextInt(width - 2) + 1;
                    int coordY = rng.nextInt(height - 2) + 1;
                    int[] coords = {coordX, coordY};

                    while (!emptyCoord(coords))
                    {
                        coordX = rng.nextInt(width - 2) + 1;
                        coordY = rng.nextInt(height - 2) + 1;
                        coords[0] = coordX;
                        coords[1] = coordY;
                    }
                    spawnEnemies(coordX, coordY, type);
                }
            }

            // place pits
            int pitCount = (rng.nextInt(3)) * 2;
            for (int i = 1; i <= pitCount; i++)
            {
                    // coordLists
                    int coordX = rng.nextInt(width - 2) + 1;
                    int coordY = rng.nextInt(height - 2) + 1;
                    int[] coords = {coordX, coordY};

                    while (!emptyCoord(coords) || blocksDoors(coords) )
                    {
                        coordX = rng.nextInt(width - 2) + 1;
                        coordY = rng.nextInt(height - 2) + 1;
                        coords[0] = coordX;
                        coords[1] = coordY;
                    }
                    spawnPits(coordX, coordY);
            }

            // place rocks
            int rockCount = rng.nextInt(6);
            for (int i = 1; i <= rockCount; i++)
            {
                // coordLists
                int coordX = rng.nextInt(width - 2) + 1;
                int coordY = rng.nextInt(height - 2) + 1;
                int[] coords = {coordX, coordY};

                while (!emptyCoord(coords) || blocksDoors(coords) )
                {
                    coordX = rng.nextInt(width - 2) + 1;
                    coordY = rng.nextInt(height - 2) + 1;
                    coords[0] = coordX;
                    coords[1] = coordY;
                }
                spawnRocks(coordX, coordY);
            }
        }
        spawnTiles();
    }

    // spawn tiles
    public void spawnTiles()
    {
        for (int row = 0; row < localFloorGrid.length; row++)
        {
            for (int col = 0; col < localFloorGrid[row].length; col++)
            {
                int randomNum = rng.nextInt(14) + 1;
                int type = 1;
                if(randomNum <= 8) type = 1;
                else if(randomNum <= 10) type = 2;
                else if(randomNum <= 13) type = 3;
                else if(randomNum <= 14) type = 4;

                if (localFloorGrid[row][col] instanceof Pit != true) 
                {
                    Tile floorTile = new Tile(col, row, type, rng);
                    localFloorGrid[row][col] = floorTile;
                    placedFloorObjects.add(floorTile);
                }
            }
        }
    }

    // spawn pits
    public void spawnPits(int pCoordX, int pCoordY)
    {
        Pit newPit = new Pit(pCoordX, pCoordY);
        localFloorGrid[pCoordY][pCoordX] = newPit;
        placedFloorObjects.add(newPit);
        int[] coordArray = {pCoordX, pCoordY};
        occupiedCoords.add(coordArray);
    }

    // spawn enemies
    public void spawnEnemies(int eCoordX, int eCoordY, int type)
    {
        //Enemy newEnemy = new Enemy(eCoordX, eCoordY, type);
        //placedRoomObjects.add(newEnemy);
        //localEnemies.add(newEnemy);
        int[] coordArray = {eCoordX,eCoordY};
        occupiedCoords.add(coordArray);
    }

    // spawn rocks
    public void spawnRocks(int rCoordX, int rCoordY)
    {
        
        Rock newRock = new Rock(rCoordX, rCoordY);
        localObjectGrid[rCoordY][rCoordX] = newRock;
        placedRoomObjects.add(newRock);
        int[] coordArray = {rCoordX, rCoordY};
        occupiedCoords.add(coordArray);
    }

    // spawn doors
    public void addDoor(int direction)
    {
        // direction: 0=North, 1=East, 2=South, 3=West
        // grid coordinate math targets the center of the respective wall

        int midX = width / 2;
        int midY = height / 2;

        int doorX = -1;
        int doorY = -1;
        char doorDir = '/';

        if (direction == 0)
        { // north Wall
            doorX = midX;
            doorY = 0;
            doorDir = 'n';
        }
        else if (direction == 1)
        { // east Wall
            doorX = width - 1;
            doorY = midY;
            doorDir = 'e';
        } 
        else if (direction == 2)
        { // south Wall
            doorX = midX;
            doorY = height - 1;
            doorDir = 's';
        }
        else if (direction == 3)
        { // west Wall
            doorX = 0;
            doorY = midY;
            doorDir = 'w';
        }

        // remove collision wall from logical matrix
        localObjectGrid[doorY][doorX] = null;

        // instantiate Door entity for collision/state evaluation
        Door newDoor = new Door(doorX, doorY, doorDir);
        placedDoors.add(newDoor);
        
        // mark coordinate as occupied
        occupiedCoords.add(new int[]{doorX, doorY});
    }

    // checks if the current coord is occupied
    public boolean emptyCoord(int[] coords)
    {
        boolean empty = true;
        for (int[] occupiedCoord : occupiedCoords)
        {
            if (Arrays.equals(coords,occupiedCoord))
            {
                empty = false;
                return empty;
            }
        }
        return empty;
    }

    public boolean blocksDoors(int[] coords)
    {
        int coordX = coords[0];
        int coordY = coords[1];
        
        int midX = width / 2;
        int midY = height / 2;
        
        // defines a 2-tile clearance radius around all potential doors
        int safeRadius = 2; 

        // north door clearance
        if (Math.abs(coordX - midX) <= safeRadius && coordY <= safeRadius)
        {
            return true; 
        }
        
        // south door clearance
        if (Math.abs(coordX - midX) <= safeRadius && coordY >= (height - 1 - safeRadius))
        {
            return true;
        }
        
        // west door celarance
        if (coordX <= safeRadius && Math.abs(coordY - midY) <= safeRadius)
        {
            return true;
        }
        
        // east door clearance
        if (coordX >= (width - 1 - safeRadius) && Math.abs(coordY - midY) <= safeRadius)
        {
            return true;
        }

        return false;
    }

    // update to remove the enemy from the room
    public void removeEnemyFromRoom(Enemy e)
    {
        localEnemies.remove(e);
        placedRoomObjects.remove(e);
    }

    // update to check if the room is cleared
    public void checkCleared()
    {
        if (localEnemies.size() == 0)
        {
            isCleared = true;
            openAllDoors();
            if (type.equals("Enemy"))
            {
                spawnRewards();
            }
        }
    }

    // open all the doors of the room
    public void openAllDoors()
    {
        for(Door door : placedDoors)
        {
            door.openDoor();
        }
    }

    // spawn the items after room is cleared
    // needs the item pool 
    // WIP
    // WIP
    // WIP
    public void spawnRewards()
    {
        int spawnItemCount = rng.nextInt(1);
        int spawnCoinCount = rng.nextInt(3);

        //spawn items
        for(int i = 0; i < spawnItemCount; i++)
        {
            int randomIndex = rng.nextInt(100);
        }

        //spawn coins
        for(int i = 0; i < spawnCoinCount; i++)
        {
            
        }
    }

    public void addItem(Item item)
    {

    }
    
    public void removeItem(Item item)
    {
        
    }

    @Override
    public void update()
    {   
        checkCleared();
        if(isCleared == true)
        {
            if(doorsOpen == false)
            {
                openAllDoors();
                doorsOpen = true;
            }
            if(spawnedRewards == false)
            {
                spawnRewards();
                spawnedRewards = true;
            }
        }
    }
}
