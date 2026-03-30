import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class Menu extends JPanel {

    protected BufferedImage image;
    protected java.util.List<MenuButton> buttons = new ArrayList<>();

    protected Point mousePos = new Point(0, 0);

    public Menu() {
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getPoint());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
                handleHover(mousePos);
                repaint();
            }
        });

        setButtons();
    }

    protected abstract void setButtons();

    private void handleClick(Point p) {
        for (MenuButton b : buttons) {
            if (b.bounds.contains(p)) {
                b.onClick.run();
            }
        }
    }

    private void handleHover(Point p) {
        boolean hovering = false;

        for (MenuButton b : buttons) {
            if (b.bounds.contains(p)) {
                hovering = true;
                break;
            }
        }

        if (hovering) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(255, 255, 255, 60));

        for (MenuButton b : buttons) {
            if (b.bounds.contains(mousePos)) {
                g2.fillRect(b.bounds.x, b.bounds.y, b.bounds.width, b.bounds.height);
            }
        }
    }
}