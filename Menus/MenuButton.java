//package Menus;

import java.awt.Rectangle;

public class MenuButton {
    Rectangle bounds;
    Runnable onClick;

    public MenuButton(int x, int y, int width, int height, Runnable action) {
        bounds = new Rectangle(x, y, width, height);
        onClick = action;
    }
}
