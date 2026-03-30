import javax.swing.*;

public class Game {

    public static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("Cursed Crown");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);
        frame.setLocationRelativeTo(null);

        switchMenu(new MainMenu());

        frame.setVisible(true);
    }

    public static void switchMenu(Menu menu) {
        frame.setContentPane(menu);
        frame.revalidate();
        frame.repaint();
    }
}