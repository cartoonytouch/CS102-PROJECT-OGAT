package Renderers;

import java.util.function.Consumer;

public final class MenuBridge {

    private static Consumer<DynamicOverlay> pauseMenuOpener;

    private MenuBridge()
    {
    }

    public static void registerPauseMenuOpener(Consumer<DynamicOverlay> opener)
    {
        pauseMenuOpener = opener;
    }

    public static void openPauseMenu(DynamicOverlay gamePanel)
    {
        if (pauseMenuOpener == null)
        {
            throw new IllegalStateException("Pause menu opener has not been registered.");
        }

        pauseMenuOpener.accept(gamePanel);
    }
}
