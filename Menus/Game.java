import javax.swing.*;
import Map.mapGenerator;
import Map.Room.Room;
import Renderers.DynamicOverlay;

public class Game {

    public static JFrame frame;
    public static Room[][] mapGrid;
    public static String testSeed = "0";

    public static void main(String[] args) {

        frame = new JFrame("Cursed Crown");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        switchMenu(new MainMenu());

        frame.setVisible(true);
    }

    public static void switchMenu(JPanel panel) {
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();
    }

    public static void startGame() {
        System.out.println("Generating map...");
        mapGenerator gen1 = new mapGenerator(testSeed);
        gen1.generate(12);
        mapGrid = gen1.getGrid();
        
        printMapSummary(mapGrid);

        Room startRoom = null;


        for (int row = 0; row < mapGrid.length; row++) {
            for (int col = 0; col < mapGrid[row].length; col++) {
                if (mapGrid[row][col] != null && mapGrid[row][col].type.equals("Start")) {
                    startRoom = mapGrid[row][col];
                    break;
                }
            }
            if (startRoom != null) break;
        }

        if (startRoom != null) {
            DynamicOverlay gamePanel = new DynamicOverlay(mapGrid, startRoom);
            
            switchMenu(gamePanel);
            
            gamePanel.startGameThread();
        } else {
            System.err.println("Failed map render: No start room found.");
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