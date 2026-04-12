package Menus;

import java.awt.*;

import javax.swing.JTextField;
import Renderers.DifficultyProgress;

class NewGameMenu extends Menu {

    private JTextField seedField;

    @Override
    protected void setButtons() {
        image = loadImage("Assets/MenuAssets/newgamemenu.png");

        seedField = new JTextField(Game.createRandomSeed());
        seedField.setBounds(610, 610, 380, 42);
        seedField.setFont(new Font("Arial", Font.BOLD, 20));
        add(seedField);

        buttons.add(new MenuButton(140, 390, 480, 50, () -> {
            System.out.println("Easy");
            //GameEngine.setDifficulty("Easy");
        }));

        buttons.add(new MenuButton(140, 480, 480, 50, () -> {
            System.out.println("Normal");
            //GameEngine.setDifficulty("Normal");
        }));

        buttons.add(new MenuButton(140, 570, 480, 50, () -> {
            System.out.println("Hard");
            //GameEngine.setDifficulty("Hard");
        }));

        buttons.add(new MenuButton(1010, 390, 480, 50, () -> {
            Game.setSelectedPlayerClass("Swordsman");
        }));

        buttons.add(new MenuButton(1010, 480, 480, 50, () -> {
            Game.setSelectedPlayerClass("Spearman");
        }));

        buttons.add(new MenuButton(1010, 570, 480, 50, () -> {
            Game.setSelectedPlayerClass("Smasher");
        }));
        
        buttons.add(new MenuButton(560, 770, 480, 70, () -> {
            Game.switchMenu(new MainMenu());
        }));

        buttons.add(new MenuButton(560, 670, 480, 70, () -> {
            applySeedSelection();
            Game.switchMenu(new StoryScreen());
        }));
    }

    private void applySeedSelection()
    {
        if (seedField == null)
        {
            Game.setSeed(Game.createRandomSeed());
            return;
        }

        String normalizedSeed = Game.normalizeSeed(seedField.getText());
        seedField.setText(normalizedSeed);
        Game.setSeed(normalizedSeed);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;

        if (!DifficultyProgress.isMediumBeaten)
        {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(140, 570, 480, 50);
        }
        
        g2.setStroke(new BasicStroke(5));
        g2.setColor(new Color(255, 215, 0, 150));

        String playerClass = Game.getSelectedPlayerClass();
        if ("Swordsman".equals(playerClass)) g2.drawRect(1010, 390, 480, 50);
        else if ("Spearman".equals(playerClass)) g2.drawRect(1010, 480, 480, 50);
        else if ("Smasher".equals(playerClass)) g2.drawRect(1010, 570, 480, 50);

        g2.setColor(new Color(255, 255, 255, 220));
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Seed", 610, 600);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString("Leave it as-is for a fresh random run, or type your own number.", 610, 665);
    }
}
