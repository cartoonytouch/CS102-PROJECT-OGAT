package Menus;

import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;

import Map.mapGenerator;
import Map.Room.Room;
import Renderers.DynamicOverlay;
import Renderers.MenuBridge;
import Renderers.GameData;
import Renderers.SaveSystem;

public class Game {

    public static JFrame frame;
    public static Room[][] mapGrid;
    public static String testSeed = "0";
    private static String selectedPlayerClass = "Swordsman";

    public static void main(String[] args) {
        MenuBridge.registerPauseMenuOpener(gamePanel -> Game.switchMenu(new PauseMenu(gamePanel)));

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
        SaveSystem.clearSave();
        launchFreshRun(testSeed, selectedPlayerClass);
    }

    public static void continueGame()
    {
        if (!SaveSystem.hasSaveFile())
        {
            JOptionPane.showMessageDialog(frame, "No saved game was found.");
            return;
        }

        GameData saveData = SaveSystem.readSaveData();
        if (saveData == null)
        {
            JOptionPane.showMessageDialog(frame, "The save file could not be loaded.");
            return;
        }

        String savedSeed = normalizeSeed(saveData.mapSeed);
        String savedClass = (saveData.playerClass == null || saveData.playerClass.isBlank())
            ? selectedPlayerClass
            : saveData.playerClass;

        testSeed = savedSeed;
        selectedPlayerClass = savedClass;

        DynamicOverlay gamePanel = buildGamePanel(savedSeed, savedClass);
        if (gamePanel == null)
        {
            JOptionPane.showMessageDialog(frame, "Failed to build the saved map.");
            return;
        }

        gamePanel.loadGame();
        switchMenu(gamePanel);
        gamePanel.startGameThread();
    }

    private static void launchFreshRun(String seed, String playerClass)
    {
        testSeed = normalizeSeed(seed);
        selectedPlayerClass = (playerClass == null || playerClass.isBlank()) ? "Swordsman" : playerClass;

        DynamicOverlay gamePanel = buildGamePanel(testSeed, selectedPlayerClass);
        if (gamePanel == null)
        {
            System.err.println("Failed map render: No start room found.");
            return;
        }

        switchMenu(gamePanel);
        gamePanel.startGameThread();
    }

    private static DynamicOverlay buildGamePanel(String seed, String playerClass)
    {
        System.out.println("Generating map...");
        mapGenerator gen1 = new mapGenerator(seed);
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
            return new DynamicOverlay(mapGrid, startRoom, seed, playerClass);
        }

        return null;
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

    public static void setSelectedPlayerClass(String playerClass)
    {
        if (playerClass != null && !playerClass.isBlank())
        {
            selectedPlayerClass = playerClass;
        }
    }

    public static void setSeed(String seed)
    {
        testSeed = normalizeSeed(seed);
    }

    public static String getSeed()
    {
        return testSeed;
    }

    public static String normalizeSeed(String seed)
    {
        if (seed == null)
        {
            return createRandomSeed();
        }

        String numericSeed = seed.replaceAll("[^0-9]", "");
        if (numericSeed.isBlank())
        {
            return createRandomSeed();
        }

        return numericSeed;
    }

    public static String createRandomSeed()
    {
        return String.valueOf(ThreadLocalRandom.current().nextLong(100000L, 1_000_000_000L));
    }

    public static String getSelectedPlayerClass()
    {
        return selectedPlayerClass;
    }

    public static void showNewGameMenu()
    {
        switchMenu(new NewGameMenu());
    }

    public static void quitApplication()
    {
        System.exit(0);
    }
}
