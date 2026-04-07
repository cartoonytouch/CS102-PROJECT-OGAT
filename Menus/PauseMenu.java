import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import Renderers.DynamicOverlay;

public class PauseMenu extends Menu{

    private DynamicOverlay gamePanel;

    // Update constructor to accept the game panel
    public PauseMenu(DynamicOverlay gamePanel) {
        this.gamePanel = gamePanel;
        setButtons(); // Call this here if your Menu class requires it
    }


    @Override
    protected void setButtons() {
        try {
            image = ImageIO.read(new File("Assets\\MenuAssets\\pausemenu.png"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

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
