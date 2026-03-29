import javax.swing.JFrame;

public class mainFile {
    public static Room[][] mapGrid;
    public static String testSeed = "3";

    public static void main(String[] args) {
        
        System.out.println("Map 1");
        mapGenerator gen1 = new mapGenerator(testSeed);
        gen1.generate(12);
        printMapSummary(gen1.getGrid());

        Room testRoom = null;
        Room[][] currentGrid = gen1.getGrid();

        for (int y = 0; y < currentGrid.length; y++)
        {
            for (int x = 0; x < currentGrid[y].length; x++)
            {
                if (currentGrid[x][y] != null)
                {
                    testRoom = currentGrid[x][y];
                    if (testRoom != null)
                    {
                        // render all the rooms
                        if (testRoom != null)
                        {
                            JFrame frame = new JFrame("Room Renderer Test");
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.setResizable(false);
                            
                            RoomRenderPanel panel = new RoomRenderPanel(testRoom);
                            frame.add(panel);
                            
                            frame.pack();
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);
                        } 
                    }
                }
            }
            
        }

        // render map
        // useless condition leftover
        if (testRoom != null) {
            JFrame frame = new JFrame("Map Renderer Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            MapRenderPanel panel = new MapRenderPanel(currentGrid);
            frame.add(panel);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } else {
            System.out.println("failed map render");
        }
    }

    public static void printMapSummary(Room[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[x][y] == null) {
                    System.out.print("[ ] ");
                } else {
                    String type = grid[x][y].type;
                    if (type.equals("Shop")) System.out.print("[$] ");
                    else if (type.equals("Empty")) System.out.print("[0] ");
                    else System.out.print("[" + type.charAt(0) + "] ");
                }
            }
            System.out.println();
        }
    }

    public static Room[][] getMapGrid() {
        return mapGrid;
    }
}

