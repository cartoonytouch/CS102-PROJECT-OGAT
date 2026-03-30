import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu extends Menu{

    @Override
    protected void setButtons() {
        try {
            image = ImageIO.read(new File("assets/ui/mainmenu.png"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        buttons.add(new MenuButton(550, 390, 480, 70, () -> {
            Game.switchMenu(new PauseMenu());
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
