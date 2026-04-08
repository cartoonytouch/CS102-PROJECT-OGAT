package Map.Room;
import java.util.Random;

import Entities.FloorObjects;
import Entities.Projectile;
import Entities.Characters.Player;
import Entities.Characters.Enemies.Enemy;
import Entities.Characters.Enemies.LongRangeEnemy;
import Entities.Characters.Enemies.MeleeEnemy;
import Entities.Characters.Enemies.Boss;
import Entities.StaticEntities.Door;
import Entities.StaticEntities.Pit;
import Entities.StaticEntities.Rock;
import Entities.StaticEntities.StaticEntity;
import Entities.StaticEntities.Tile;
import Entities.StaticEntities.Wall;
import Items.Item;
import Items.ItemCatalog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Room {
    // the grids 
    public FloorObjects[][] localFloorGrid;
    public StaticEntity[][] localObjectGrid;
    public int[][] localMiniGrid;
    

    // dimensions and the absolute location of 
    // the top left corner on the map grid.
    final public int width = 13;
    final public int height = 9;
    public int gridX;
    public int gridY;

    // some info about room
    public String type;
    boolean isCleared;
    private Random rng;

    public boolean isDiscovered;

    // things that need to be rendered
    private List<int[]> occupiedCoords = new ArrayList<>();
    public List<Enemy> localEnemies = new ArrayList<>();
    private List<StaticEntity> placedRoomObjects = new ArrayList<>();
    private List<FloorObjects> placedFloorObjects = new ArrayList<>();
    public List<Door> placedDoors = new ArrayList<>();
    public List<Pit> placedPits = new ArrayList<>();
    public List<Station> placedStations = new ArrayList<>();
    public List<Projectile> projectiles = new ArrayList<>();
    private RewardDrop rewardDrop;
    private boolean spawnedRewards;


    // room constructor
    public Room(String roomType, int gridX, int gridY, Random sharedRng)
    {
        // initialize the fields of the room 
        type = roomType;
        localFloorGrid = new FloorObjects[height][width];
        localObjectGrid = new StaticEntity[height][width];
        this.gridX = gridX;
        this.gridY = gridY;
        this.rng = sharedRng;
        this.rewardDrop = null;
        this.spawnedRewards = false;
        
        // initialize if its cleared or not
        if (type.equals("Start") || type.equals("Shop") || type.equals("Empty"))
        {
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
                int wallType = randomWallType();

                if (y == 0 && x == 0)
                {
                    localObjectGrid[y][x] = new Wall(x, y, 'n', 4);
                }
                else if (y == 0 && x == width - 1)
                {
                    localObjectGrid[y][x] = new Wall(x, y, 'n', 5);
                }
                else if (y == height - 1 && x == 0)
                {
                    localObjectGrid[y][x] = new Wall(x, y, 'n', 6);
                }
                else if (y == height - 1 && x == width - 1)
                {
                    localObjectGrid[y][x] = new Wall(x, y, 'n', 7);
                }
                else if (y == 0)
                {
                    localObjectGrid[y][x] = new Wall(x, y, 'n', wallType);
                }
                else if (x == width - 1)
                {
                    localObjectGrid[y][x] = new Wall(x, y, 'e', wallType);
                }
                else if (y == height - 1)
                {
                    localObjectGrid[y][x] = new Wall(x, y, 's', wallType);
                }
                else if (x == 0)
                {
                    localObjectGrid[y][x] = new Wall(x, y, 'w', wallType);
                }
            }
        }
        if (this.type.equals("Empty") || this.type.equals("Enemy"))
        {
            if (this.type.equals("Enemy"))
            {

                // place the enemies
                int enemyCount = rng.nextInt(3) + 1;
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
            int pitCount = rng.nextInt(10);
            for (int i = 0; i <= pitCount; i++)
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
            for (int i = 0; i <= rockCount; i++)
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
        if (this.type.equals("Boss"))
        {
            spawnBoss();
        }
        else if (this.type.equals("Shop"))
        {
            spawnStations();
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
                if (localFloorGrid[row][col] instanceof Pit != true) 
                {
                    Tile floorTile = new Tile(col, row, randomTileType(), rng);
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
        placedPits.add(newPit);
        int[] coordArray = {pCoordX, pCoordY};
        occupiedCoords.add(coordArray);
    }

    // spawn enemies
    public void spawnEnemies(int eCoordX, int eCoordY, int type)
    {
        Enemy newEnemy = (type == 0) ? new LongRangeEnemy(eCoordX, eCoordY) : new MeleeEnemy(eCoordX, eCoordY);
        localEnemies.add(newEnemy);
        int[] coordArray = {eCoordX,eCoordY};
        occupiedCoords.add(coordArray);
    }

    // spawn boss (always spawns in the center so no parameter needed)
    public void spawnBoss()
    {
        Boss boss = new Boss(60, width/2, height/2);
        localEnemies.add(boss);
        int[] coordArray = {width/2, height/2};
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

    public void spawnStations()
    {
        int centerY = height / 2;
        placeStation(new BuyStation((width / 2) - 2, centerY, rng));
        placeStation(new UpgradeStation((width / 2) + 2, centerY));
    }

    private void placeStation(Station station)
    {
        if (station == null)
        {
            return;
        }

        localObjectGrid[station.coordY][station.coordX] = station;
        placedStations.add(station);
        occupiedCoords.add(new int[]{station.coordX, station.coordY});
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
        char doorDir = 'n';

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
        if (isCleared)
        {
            newDoor.openDoor();
        }
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
    public void checkCleared(Player player)
    {
        if (localEnemies.size() == 0)
        {
            if (!isCleared)
            {
                isCleared = true;
                openAllDoors();

                if (type.equals("Enemy"))
                {
                    spawnRewards(player);
                }
            }
        }
    }

    public void spawnRewards(Player player)
    {
        if (spawnedRewards || !type.equals("Enemy"))
        {
            return;
        }

        int[] rewardCoords = findRewardSpawnLocation();
        if (rewardCoords == null)
        {
            spawnedRewards = true;
            return;
        }

        Item rewardItem = createRewardItem(player);
        if (rewardItem != null && rng.nextBoolean())
        {
            rewardDrop = new RewardDrop(rewardCoords[0], rewardCoords[1], rewardItem);
        }
        else
        {
            rewardDrop = new RewardDrop(rewardCoords[0], rewardCoords[1], rng.nextInt(3) + 1);
        }

        spawnedRewards = true;
    }

    private Item createRewardItem(Player player)
    {
        if (player == null)
        {
            return null;
        }

        List<String> candidatePool = new ArrayList<>(player.getDiscoveredItemIds());
        List<String> eligibleItemIds = new ArrayList<>();

        for (String itemId : candidatePool)
        {
            Item item = ItemCatalog.createItem(itemId);
            if (item == null)
            {
                continue;
            }

            if (player.getInventory() != null && player.getInventory().canAdd(item))
            {
                eligibleItemIds.add(itemId);
            }
        }

        if (eligibleItemIds.isEmpty())
        {
            return null;
        }

        String rewardItemId = eligibleItemIds.get(rng.nextInt(eligibleItemIds.size()));
        return ItemCatalog.createItem(rewardItemId);
    }

    public RewardDrop getRewardDrop()
    {
        return rewardDrop;
    }

    public void clearRewardDrop()
    {
        rewardDrop = null;
    }

    public boolean isValidRewardSpawn(int coordX, int coordY)
    {
        if (coordX <= 0 || coordX >= width - 1 || coordY <= 0 || coordY >= height - 1)
        {
            return false;
        }

        FloorObjects floorObject = localFloorGrid[coordY][coordX];
        if (floorObject instanceof Pit)
        {
            return false;
        }

        StaticEntity staticEntity = localObjectGrid[coordY][coordX];
        return !(staticEntity instanceof Rock);
    }

    private int[] findRewardSpawnLocation()
    {
        int centerX = width / 2;
        int centerY = height / 2;
        int maxRadius = Math.max(width, height);

        for (int radius = 0; radius < maxRadius; radius++)
        {
            for (int offsetY = -radius; offsetY <= radius; offsetY++)
            {
                for (int offsetX = -radius; offsetX <= radius; offsetX++)
                {
                    if (Math.abs(offsetX) != radius && Math.abs(offsetY) != radius)
                    {
                        continue;
                    }

                    int candidateX = centerX + offsetX;
                    int candidateY = centerY + offsetY;

                    if (isValidRewardSpawn(candidateX, candidateY))
                    {
                        return new int[]{candidateX, candidateY};
                    }
                }
            }
        }

        return null;
    }

    public void openAllDoors()
    {
        for (Door door : placedDoors)
        {
            door.openDoor();
        }
    }

    private int randomWallType()
    {
        int randomNum = rng.nextInt(10) + 1;

        if (randomNum <= 5) {
            return 1;
        }
        if (randomNum <= 8) {
            return 2;
        }
        return 3;
    }

    private int randomTileType()
    {
        int randomNum = rng.nextInt(14) + 1;

        if (randomNum <= 8) {
            return 1;
        }
        if (randomNum <= 10) {
            return 2;
        }
        if (randomNum <= 13) {
            return 3;
        }
        return 4;
    }

    // we will render Tiles first, and then add Pits, and then the RoomObjects
    // first render placedFloorObjects, and then the placedRoomObjects on top.
    // to do this, render localFloorGrid firs and then the localObjectGrid.
}
