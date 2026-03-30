import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class OptionsMenu extends Menu {

    @Override
    protected void setButtons() {
        try {
            image = ImageIO.read(new File("assets/ui/mainmenu.png"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        buttons.add(new MenuButton(540, 600, 300, 70, () -> {
            Game.switchMenu(new MainMenu());
        }));
    }
}