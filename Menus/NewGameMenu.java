//package Menus;

import java.awt.*;

class NewGameMenu extends Menu {

    @Override
    protected void setButtons() {
        image = loadImage("Assets/MenuAssets/newgamemenu3.png");

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
            System.out.println("Swordsman");
            //GameEngine.setClass("Swordsman");
        }));

        buttons.add(new MenuButton(1010, 480, 480, 50, () -> {
            System.out.println("Spearman");
            //GameEngine.setClass("Spearman");
        }));

        buttons.add(new MenuButton(1010, 570, 480, 50, () -> {
            System.out.println("Smasher");
            //GameEngine.setClass("Smasher");
        }));
        
        buttons.add(new MenuButton(550, 780, 480, 70, () -> {
            Game.switchMenu(new MainMenu());
        }));

        buttons.add(new MenuButton(550, 680, 480, 70, () -> {
            Game.switchMenu(new StoryScreen());
        }));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g2.setColor(new Color(255, 215, 0, 150));

        /*
        String diff = GameEngine.getDifficulty();
        if (diff != null) {
            if (diff.equals("Easy")) g2.drawRect(140, 390, 480, 50);
            else if (diff.equals("Normal")) g2.drawRect(140, 480, 480, 50);
            else if (diff.equals("Hard")) g2.drawRect(140, 570, 480, 50);
        }

        String pClass = Game.engine.getSelectedClass();
        if (pClass != null) {
            if (pClass.equals("Swordsman")) g2.drawRect(1010, 390, 480, 50);
            else if (pClass.equals("Spearman")) g2.drawRect(1010, 480, 480, 50);
            else if (pClass.equals("Smasher")) g2.drawRect(1010, 570, 480, 50);
        }
        */
    }
}
