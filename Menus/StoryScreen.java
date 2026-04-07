import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StoryScreen extends Menu {
    private float alpha = 0.0f;
    private int fadeState = 0;
    private int ticks = 0;
    private final int MAX_TICKS = 200;
    private Timer timer;
    
    private String storyText = "The kingdom has fallen"; 

    public StoryScreen() {
        super();
        setBackground(Color.BLACK);
        
        // 30 times a second
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFade();
                repaint();
            }
        });
        timer.start();
    }

    private void updateFade() {
        if (fadeState == 0) { // Fading In
            alpha += 0.01f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                fadeState = 1;
            }
        } else if (fadeState == 1) { // Holding
            ticks++;
            if (ticks >= MAX_TICKS) {
                fadeState = 2;
            }
        } else if (fadeState == 2) { // Fading Out
            alpha -= 0.02f;
            if (alpha <= 0.0f) {
                alpha = 0.0f;
                timer.stop();
                completeStory();
            }
        }
    }

    private void completeStory() {
        timer.stop();

        Game.startGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.ITALIC, 36));
        
        FontMetrics metrics = g2.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(storyText)) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        
        g2.drawString(storyText, x, y);
    }

    @Override
    protected void setButtons() {
    }
}
