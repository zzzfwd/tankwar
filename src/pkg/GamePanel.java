package pkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private int width;
    private int height;
    private Image offScreenImage = null;            // 双缓冲，解决闪烁问题
    private Tank myTank;
    private Vector<Tank> enemyTanks;
    private Vector<Bullet> myBullets;
    private Vector<Bullet> enemyBullets;
    private Wall wall;

    public GamePanel() {
        this(800, 800);
    }

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        myTank = new Tank(20, 200, Direction.STOP, true, Color.YELLOW);
        enemyTanks = new Vector<>();
        for (int i = 0; i < 5; ++i) {
            enemyTanks.add(new Tank(20 + i * 80, 40, Direction.DOWN, false, Color.CYAN));
        }
        wall = new Wall(375, 220, 50, 360);
//        this.setPreferredSize(new Dimension(width, height));
//        this.setSize(width, height);
        this.setBounds(0, 0, width, height);
        this.setBackground(Color.BLACK);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
        if (offScreenImage == null) {
            offScreenImage = this.createImage(width, height);
            System.out.println("width = " + width + ", height = " + height);
        }
        Graphics gOffScreenImage = offScreenImage.getGraphics();
        paint(gOffScreenImage);
        System.out.println("width = " + width + ", height = " + height);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 墙
        this.drawWall(g, wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        // 玩家坦克
        if (myTank.isAlive()) {
            this.drawTank(g, myTank.getX(), myTank.getY(), myTank.gettDirection(), myTank.getColor());
            myTank.move();
            myTank.collideWall(wall);
            for (int i = 0; i < enemyTanks.size(); ++i) {
                myTank.collideTank(enemyTanks.get(i));
            }
        }
        // 玩家子弹
        myBullets = myTank.getBullets();
        if (myBullets != null) {
//            System.out.println("bullet count: " + myBullets.size());
            for (int i = 0; i < myBullets.size(); ++i) {
                Bullet bullet = myBullets.get(i);
                for (int j = 0; j < enemyTanks.size(); ++j) {
                    Tank enemyTank = enemyTanks.get(j);
                    bullet.hitTank(enemyTank);
                    bullet.hitWall(wall);
                }
                if (bullet.isAlive()) {
                    this.drawBullet(g, bullet.getX(), bullet.getY());
                    bullet.move();
                } else {
                    myBullets.remove(bullet);
//                    System.out.println("size = " + myBullets.size());
                }
            }
        }
        for (int i = 0; i < enemyTanks.size(); ++i) {
            // 敌方坦克
            Tank enemyTank = enemyTanks.get(i);
            if (enemyTank.isAlive()) {
                this.drawTank(g, enemyTank.getX(), enemyTank.getY(), enemyTank.gettDirection(), enemyTank.getColor());
                enemyTank.move();
                enemyTank.collideWall(wall);
                enemyTank.collideTank(myTank);
                for (int j = 0; j < enemyTanks.size(); ++j) {
                    enemyTank.collideTank(enemyTanks.get(j));
                }
            }
            // 敌方子弹
            enemyBullets = enemyTank.getBullets();
//            System.out.println("enemyBullet count: " + enemyBullets.size());
            if (enemyBullets != null) {
                for (int j = 0; j < enemyBullets.size(); ++j) {
                    Bullet enemyBullet = enemyBullets.get(j);
                    enemyBullet.hitTank(myTank);
                    enemyBullet.hitWall(wall);
                    if (enemyBullet.isAlive()) {
                        this.drawBullet(g, enemyBullet.getX(), enemyBullet.getY());
                        enemyBullet.move();
                    } else {
                        enemyBullets.remove(enemyBullet);
                    }
                }
            }
        }
    }

    public void drawTank(Graphics g, int x, int y, Direction tDirection, Color color) {
        g.setColor(color);
        switch (tDirection) {
            // 向上
            case UP:
                g.fill3DRect(x, y, 8, 38, true);
                g.fill3DRect(x + 30, y, 8, 38, true);
                g.fill3DRect(x + 8, y + 9, 22, 22, false);
                g.fillOval(x + 8, y + 9, 19, 19);
                g.drawLine(x + 18, y, x + 18, y + 18);
                break;
            // 向左
            case LEFT:
                g.fill3DRect(x, y, 38, 8, true);
                g.fill3DRect(x, y + 30, 38, 8, true);
                g.fill3DRect(x + 9, y + 8, 22, 22, false);
                g.fillOval(x + 9, y + 8, 19, 19);
                g.drawLine(x, y + 18, x + 18, y + 18);
                break;
            // 向下
            case DOWN:
                g.fill3DRect(x, y, 8, 38, true);
                g.fill3DRect(x + 30, y, 8, 38, true);
                g.fill3DRect(x + 8, y + 7, 22, 22, false);
                g.fillOval(x + 8, y + 7, 19, 19);
                g.drawLine(x + 18, y + 38, x + 18, y + 20);
                break;
            // 向右
            case RIGHT:
                g.fill3DRect(x, y, 38, 8, true);
                g.fill3DRect(x, y + 30, 38, 8, true);
                g.fill3DRect(x + 7, y + 8, 22, 22, false);
                g.fillOval(x + 7, y + 8, 19, 19);
                g.drawLine(x + 38, y + 18, x + 20, y + 18);
                break;
            default:
                break;
        }
    }

    public void drawBullet(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, Bullet.WIDTH, Bullet.HEIGHT);
    }

    public void drawWall(Graphics g, int x, int y, int width, int height) {
        g.setColor(new Color(160, 82, 45));
        g.fillRect(x, y, width, height);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                myTank.setbU(true);
                break;
            case KeyEvent.VK_A:
                myTank.setbL(true);
                break;
            case KeyEvent.VK_S:
                myTank.setbD(true);
                break;
            case KeyEvent.VK_D:
                myTank.setbR(true);
                break;
            case KeyEvent.VK_J:
                if (myTank.isAlive())
                    myTank.fire();
                break;
            case KeyEvent.VK_R:
                myTank.setAlive(true);
            default:
                break;
        }
        myTank.locateDirection();
//        System.out.println("x = " + myTank.getX() + ", y = " + myTank.getY());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                myTank.setbU(false);
                break;
            case KeyEvent.VK_A:
                myTank.setbL(false);
                break;
            case KeyEvent.VK_S:
                myTank.setbD(false);
                break;
            case KeyEvent.VK_D:
                myTank.setbR(false);
                break;
            default:
                break;
        }
        myTank.locateDirection();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println("run()");
            this.repaint();
        }
    }
}
