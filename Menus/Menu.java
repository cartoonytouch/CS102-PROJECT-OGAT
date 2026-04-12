package Menus;

import javax.swing.*;

import MusicsandSounds.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

abstract class Menu extends JPanel {

    protected BufferedImage image;
    protected java.util.List<MenuButton> buttons = new ArrayList<>();

    protected Point mousePos = new Point(0, 0);

    Sound sound = new Sound();

    public Menu() {
        setLayout(null);
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

    protected BufferedImage loadImage(String relativePath) {
        String normalizedPath = relativePath.replace("\\", "/");

        try (InputStream stream = Menu.class.getResourceAsStream("/" + normalizedPath)) {
            if (stream != null) {
                return ImageIO.read(stream);
            }
        } catch (IOException e) {
            System.err.println("Failed to load image resource: " + normalizedPath);
            e.printStackTrace();
        }

        try {
            File file = new File(normalizedPath);
            if (file.isFile()) {
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            System.err.println("Failed to load image file: " + normalizedPath);
            e.printStackTrace();
        }

        System.err.println("Missing image: " + normalizedPath + " (cwd=" + System.getProperty("user.dir") + ")");
        return null;
    }

    private void handleClick(Point p) {
        for (MenuButton b : buttons) {
            if (b.bounds.contains(p)) {
                b.onClick.run();
                playSoundEffect(6);
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
    public void playMusic(int i)
    {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void playSoundEffect(int i)
    {
        sound.setFile(i);
        sound.play();
    }
    public void playDifferentMusic(int i)
    {
        System.out.println("Music changed");
        sound.stop();
        sound.close();
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
}
