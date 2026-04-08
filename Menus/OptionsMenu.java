package Menus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;

import Renderers.DynamicOverlay;
import Renderers.GameSettings;

class OptionsMenu extends Menu {

    private DynamicOverlay gamePanel;
    private JTextField brightnessField;
    private String brightnessHint = "Enter a value between 0 and 100.";

    public OptionsMenu()
    {
        super();
    }

    public OptionsMenu(DynamicOverlay gamePanel)
    {
        this();
        this.gamePanel = gamePanel;
    }

    @Override
    protected void setButtons() {
        image = loadImage("Assets/MenuAssets/mainmenu.png");

        brightnessField = new JTextField(String.valueOf(GameSettings.getBrightnessPercent()));
        brightnessField.setBounds(1020, 210, 150, 42);
        brightnessField.setFont(new Font("Arial", Font.BOLD, 20));
        add(brightnessField);

        buttons.add(new MenuButton(1195, 210, 150, 42, this::applyBrightnessFromField));

        buttons.add(new MenuButton(540, 760, 300, 70, () -> {
            applyBrightnessFromField();
            if (gamePanel != null)
            {
                Game.switchMenu(new PauseMenu(gamePanel));
            }
            else
            {
                Game.switchMenu(new MainMenu());
            }
        }));
    }

    private void applyBrightnessFromField()
    {
        if (brightnessField == null)
        {
            return;
        }

        try
        {
            int brightness = Integer.parseInt(brightnessField.getText().trim());
            GameSettings.setBrightnessPercent(brightness);
            brightnessField.setText(String.valueOf(GameSettings.getBrightnessPercent()));
            brightnessHint = "Brightness updated.";
        }
        catch (NumberFormatException e)
        {
            brightnessField.setText(String.valueOf(GameSettings.getBrightnessPercent()));
            brightnessHint = "Brightness must be a whole number.";
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(20, 20, 20, 220));
        g2.fillRoundRect(120, 120, 1360, 720, 28, 28);

        g2.setColor(new Color(210, 210, 210, 220));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(120, 120, 1360, 720, 28, 28);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 42));
        g2.drawString("Options", 160, 185);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Brightness", 1020, 185);
        g2.setFont(new Font("Arial", Font.PLAIN, 17));
        g2.drawString("0 is darkest, 100 is brightest.", 1020, 275);
        g2.drawString(brightnessHint, 1020, 305);

        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Apply", 1245, 237);

        g2.setFont(new Font("Serif", Font.BOLD, 30));
        g2.drawString("Controls", 160, 245);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 22));
        int textY = 300;
        int lineHeight = 42;
        String[] controls = {
            "W A S D : Move",
            "Space   : Dash",
            "J       : Attack",
            "K       : Parry",
            "E       : Interact / open station",
            "C       : Use consumable",
            "L       : Switch weapon",
            "ESC     : Pause",
            "R       : Restart after death",
            "Mouse   : Click station menu buttons"
        };

        for (String control : controls)
        {
            g2.drawString(control, 170, textY);
            textY += lineHeight;
        }

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Back", 655, 804);
        g2.dispose();
    }
}
