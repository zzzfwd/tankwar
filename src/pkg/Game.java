package pkg;

import javax.swing.*;
import java.awt.*;

// 用js的键盘事件控制一个div移动，当按下一个方向键不放，div会先停顿一下，然后才开始持续移动。（原因：系统要区分用户是否连续输入，第一个到第二个之间有一个停顿时间）
// java编写坦克大战按方向键使坦克移动的时候先会停顿一下然后在开始跑怎么解决？
// 用线程来调用移动的方法，不要在按键事件处理中调用移动的方法。
public class Game extends JFrame {
    public static final int FRAME_WIDTH = 1000;         // 整个游戏窗口的宽度
    public static final int FRAME_HEIGHT = 829;         // 整个游戏窗口的高度
    public static final int GAME_WIDTH = 800;            // 游戏面板的宽度
    public static final int GAME_HEIGHT = 800;          // 游戏面板的高度

    private GamePanel gamePanel;
    private ScorePanel scorePanel;

    public Game() {
        gamePanel = new GamePanel(Game.GAME_WIDTH, Game.GAME_HEIGHT);
        scorePanel = new ScorePanel(
                Game.FRAME_WIDTH - Game.GAME_WIDTH, Game.GAME_HEIGHT);

        this.setLayout(null);
        this.add(this.gamePanel);
        this.add(this.scorePanel);

        this.addKeyListener(gamePanel);
        new Thread(gamePanel).start();
        this.setTitle("TankWar2.0");
        this.setLocation(500, 100);
        this.setSize(Game.FRAME_WIDTH, Game.FRAME_HEIGHT);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Game();
    }
}
