package pkg;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private int width;
    private int height;

    public ScorePanel() {
        this(200, 800);
    }

    public ScorePanel(int width, int height) {
        this.width = width;
        this.height = height;
//        this.setPreferredSize(new Dimension(width, height));
//        this.setSize(width, height);
        this.setBounds(Game.GAME_WIDTH, 0, width, height);
        this.setBackground(Color.LIGHT_GRAY);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.YELLOW);
        g.fill3DRect(10, 10, 20, 20, true);
//        g.fillOval(10, 10, 20, 20);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        g.drawString("Hello Player!", 10, 50);
        g.setFont(new Font("宋体", Font.BOLD, 16));
        g.drawString("WASD为方向键", 10, 80);
        g.drawString("按J键发射子弹", 10, 110);
        g.drawString("按R键复活", 10, 140);
    }
}
