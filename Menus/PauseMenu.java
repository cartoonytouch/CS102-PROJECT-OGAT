import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PauseMenu extends Menu{

    @Override
    protected void setButtons() {
        try {
            image = ImageIO.read(new File("assets/ui/pausemenu.png"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        buttons.add(new MenuButton(550, 350, 480, 70, () -> {
            System.out.println("Continue");
        }));

        buttons.add(new MenuButton(550, 480, 480, 70, () -> {
            Game.switchMenu(new OptionsMenu());
        }));

        buttons.add(new MenuButton(550, 610, 480, 70, () -> {
            Game.switchMenu(new MainMenu());
        }));
    }
}
