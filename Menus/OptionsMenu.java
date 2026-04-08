//package Menus;

class OptionsMenu extends Menu {

    @Override
    protected void setButtons() {
        image = loadImage("Assets/MenuAssets/mainmenu.png");

        buttons.add(new MenuButton(540, 600, 300, 70, () -> {
            Game.switchMenu(new MainMenu());
        }));
    }
}
