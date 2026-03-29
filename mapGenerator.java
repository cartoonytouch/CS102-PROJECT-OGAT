import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class mapGenerator {
    private Room[][] grid;
    private List<Room> placedRooms;
    public Random rng;
    private final int MAP_SIZE = 13;

    public mapGenerator(String mapSeed) {
        // seeded rng
        long seed = (long) mapSeed.hashCode();
        this.rng = new Random(seed);
        this.grid = new Room[MAP_SIZE][MAP_SIZE];
        this.placedRooms = new ArrayList<>();
    }

    public void generate(int targetRoomCount) {
        // place start room
        int startX = MAP_SIZE / 2 - 1;
        int startY = MAP_SIZE / 2 - 1;
        Room start = new Room("Start", startX, startY,rng);
        grid[startX][startY] = start;
        placedRooms.add(start);

        // random walk expansion
        while (placedRooms.size() < targetRoomCount)
        {
            // pick a random room to expand from
            Room parent = placedRooms.get(rng.nextInt(placedRooms.size()));
            
            // pick a direction 0=N, 1=E, 2=S, 3=W
            int dir = rng.nextInt(4);
            int nx = parent.gridX + (dir == 1 ? 1 : (dir == 3 ? -1 : 0));
            int ny = parent.gridY + (dir == 2 ? 1 : (dir == 0 ? -1 : 0));

            // validation
            if (nx >= 0 && nx < MAP_SIZE && ny >= 0 && ny < MAP_SIZE && grid[nx][ny] == null)
            {
                int enemy = rng.nextInt(5);
                if (enemy >= 1)
                {
                    Room next = new Room("Enemy", nx, ny, rng);
                    grid[nx][ny] = next;
                    placedRooms.add(next);
                }
                else if (enemy == 0)
                {
                    Room next = new Room("Empty", nx, ny, rng);
                    grid[nx][ny] = next;
                    placedRooms.add(next);
                }
            }
            
        }
        // add shop and boss room
        assignSpecialRooms();

        // add the doors
        addAllDoors();
    }

    private void assignSpecialRooms() {
        if (placedRooms.size() < 3) return;

        // bfs (calculate distances)
        int[][] distances = new int[MAP_SIZE][MAP_SIZE];
        for (int i = 0; i < MAP_SIZE; i++)
        {
            for (int j = 0; j < MAP_SIZE; j++)
            {
                // -1 represents unvisited
                distances[i][j] = -1;
            }
        }

        Room startRoom = placedRooms.get(0);
        distances[startRoom.gridX][startRoom.gridY] = 0;

        List<Room> queue = new ArrayList<>();
        queue.add(startRoom);
        int head = 0;

        int maxDist = 0;

        while (head < queue.size())
        {
            Room current = queue.get(head++);
            int cx = current.gridX;
            int cy = current.gridY;
            int currentDist = distances[cx][cy];

            int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
            for (int[] dir : dirs)
            {
                int nx = cx + dir[0];
                int ny = cy + dir[1];

                if (nx >= 0 && nx < MAP_SIZE && ny >= 0 && ny < MAP_SIZE && grid[nx][ny] != null)
                {
                    if (distances[nx][ny] == -1)
                    {
                        distances[nx][ny] = currentDist + 1;
                        queue.add(grid[nx][ny]);

                        if (distances[nx][ny] > maxDist)
                        {
                            maxDist = distances[nx][ny];
                        }
                    }
                }
            }
        }

        // add boss to furthes available room
        boolean bossPlaced = false;
        
        // reverse bfs traversal
        for (int i = queue.size() - 1; i >= 0; i--)
        {
            Room candidate = queue.get(i);
            
            int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
            for (int[] dir : dirs)
            {
                int nx = candidate.gridX + dir[0];
                int ny = candidate.gridY + dir[1];

                // validate boundaries and vacancy
                if (nx >= 0 && nx < MAP_SIZE && ny >= 0 && ny < MAP_SIZE && grid[nx][ny] == null)
                {
                    Room bossRoom = new Room("Boss", nx, ny, rng);
                    grid[nx][ny] = bossRoom;
                    placedRooms.add(bossRoom);
                    bossPlaced = true;
                    // terminate checking further
                    break; 
                }
            }
            
            if (bossPlaced)
            {
                // etrminate becasue boss is placed
                break;
            }
        }
        
        if (!bossPlaced)
        {
            throw new RuntimeException("wasnt able to place room");
        }

        // add shop logic
        for (int i = 0; i < 50; i++)
        { 
            Room potentialParent = placedRooms.get(rng.nextInt(placedRooms.size() - 1));
            
            if (potentialParent.type.equals("Boss") || potentialParent.type.equals("Shop")) continue;

            int dir = rng.nextInt(4);
            int sx = potentialParent.gridX + (dir == 1 ? 1 : (dir == 3 ? -1 : 0));
            int sy = potentialParent.gridY + (dir == 2 ? 1 : (dir == 0 ? -1 : 0));

            if (sx >= 0 && sx < MAP_SIZE && sy >= 0 && sy < MAP_SIZE && grid[sx][sy] == null)
            {
                Room shop = new Room("Shop", sx, sy, rng); 
                grid[sx][sy] = shop;
                placedRooms.add(shop);
                break; 
            }
        }
    }

    public void addAllDoors()
    {
        // N=0, E=1, S=2, W=3
        int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        for (Room r : placedRooms)
        {
            int cx = r.gridX;
            int cy = r.gridY;

            for (int i = 0; i < 4; i++)
            {
                int nx = cx + dirs[i][0];
                int ny = cy + dirs[i][1];

                // validate map bounds
                if (nx >= 0 && nx < MAP_SIZE && ny >= 0 && ny < MAP_SIZE)
                {
                    // if a room exists in this direction, carve a door
                    if (grid[nx][ny] != null) 
                    {
                        r.addDoor(i); 
                    }
                }
            }
        }
    }

    public Room[][] getGrid() { return grid; }
}
