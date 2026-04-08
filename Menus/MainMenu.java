package Menus;

import Renderers.SaveSystem;

public class MainMenu extends Menu{

    @Override
    protected void setButtons() {
        image = loadImage("Assets/MenuAssets/mainmenu.png");

        buttons.add(new MenuButton(550, 390, 480, 70, () -> {
            if (SaveSystem.hasSaveFile())
            {
                Game.continueGame();
            }
        }));

        buttons.add(new MenuButton(550, 520, 480, 70, () -> {
            Game.switchMenu(new NewGameMenu());
        }));

        buttons.add(new MenuButton(550, 650, 480, 70, () -> {
            Game.switchMenu(new OptionsMenu());
        }));

        buttons.add(new MenuButton(550, 780, 480, 70, () -> {
            System.exit(0);
        }));
    }
}
