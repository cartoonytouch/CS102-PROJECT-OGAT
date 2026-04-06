import javax.swing.JFrame;

import Map.mapGenerator;
import Map.Room.Room;
import Renderers.DynamicOverlay;

public class mainFile {
    public static Room[][] mapGrid;
    public static String testSeed = "0";

    public static void main(String[] args) {
        
        System.out.println("Map generating");
        mapGenerator gen1 = new mapGenerator(testSeed);
        gen1.generate(12);
        printMapSummary(gen1.getGrid());

        Room startRoom = null;
        Room[][] currentGrid = gen1.getGrid();

        for(int row=0; row < currentGrid.length; row++ ){

            for(int col=0; col < currentGrid[row].length; col++){
                if(currentGrid[row][col] != null && currentGrid[row][col].type.equals("Start")){
                    startRoom = currentGrid[row][col];
                    break;
                }

            }
            if(startRoom != null) break;
        }

        // for (int y = 0; y < currentGrid.length; y++)
        // {
        //     for (int x = 0; x < currentGrid[y].length; x++)
        //     {
        //         if (currentGrid[x][y] != null)
        //         {
        //             testRoom = currentGrid[x][y];
        //             if (testRoom != null)
        //             {
        //                 // render all the rooms
        //                 if (testRoom != null)
        //                 {
        //                     JFrame frame = new JFrame("Room Renderer Test");
        //                     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //                     frame.setResizable(false);
                            
        //                     RoomRenderPanel panel = new RoomRenderPanel(testRoom);
        //                     frame.add(panel);
                            
        //                     frame.pack();
        //                     frame.setLocationRelativeTo(null);
        //                     frame.setVisible(true);
        //                 } 
        //             }
        //         }
        //     }
            
        // }

        // render map
        // useless condition leftover
        if (startRoom != null) {
            JFrame frame = new JFrame("Cursed Crown");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            // int startX = 0;
            // int startY = 0;

            // for(int y=0; y < currentGrid.length; y++){
            //     for(int x=0; x < currentGrid[y].length; x++){
            //         if(currentGrid[x][y] == startRoom){
            //             startX = x;
            //             startY = y;
            //         }
            //     }
            // }
            DynamicOverlay gamePanel = new DynamicOverlay(currentGrid, startRoom);

            // gamePanel.player.xCoord = gamePanel.screenWidth/2 - (gamePanel.tileSize/2);
            // gamePanel.player.yCoord = gamePanel.screenHeight/2 - (gamePanel.tileSize/2);

            //MapRenderPanel panel = new MapRenderPanel(currentGrid);
            frame.add(gamePanel);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            gamePanel.startGameThread();
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
