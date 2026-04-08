//package Menus;

import Renderers.DynamicOverlay;

public class PauseMenu extends Menu{

    private DynamicOverlay gamePanel;

    // Update constructor to accept the game panel
    public PauseMenu(DynamicOverlay gamePanel) {
        this.gamePanel = gamePanel;
    }


    @Override
    protected void setButtons() {
        image = loadImage("Assets/MenuAssets/pausemenu.png");

        buttons.add(new MenuButton(550, 350, 480, 70, () -> {
            gamePanel.resumeGame();
            Game.switchMenu(gamePanel);
        }));

        buttons.add(new MenuButton(550, 480, 480, 70, () -> {
            Game.switchMenu(new OptionsMenu());
        }));

        buttons.add(new MenuButton(550, 610, 480, 70, () -> {
            Game.switchMenu(new MainMenu());
        }));
    }
}
