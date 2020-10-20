package pkg;

import java.awt.*;

public class Bullet {
    public static final int WIDTH = 1;      // 子弹的宽度
    public static final int HEIGHT = 1;     // 子弹的高度
    private int x, y;                               // 子弹的x, y坐标
    private Direction direction;            // 子弹的方向
    private int speed;                           // 子弹的速度
    private boolean isAlive;                  // 子弹是否还活着

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Bullet() {
        this(0, 0, Direction.UP);
    }

    public Bullet(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 10;
        this.isAlive = true;        // 子弹存活
    }

    public void move() {
        switch (this.direction) {
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
        }
        // 检测子弹是否出界
        if (x < 0 || y < 0 || x > Game.GAME_WIDTH || y > Game.GAME_HEIGHT)
            isAlive = false;
    }

    // 检测子弹是否打到坦克
    public boolean hitTank(Tank tank) {
        if (this.getRectangle().intersects(tank.getRectangle()) && tank.isAlive()) {
            tank.setAlive(false);
            isAlive = false;
            return true;
        }
        return false;
    }

    // 检测子弹是否打到墙壁
    public boolean hitWall(Wall wall) {
        if (this.getRectangle().intersects(wall.getRectangle())) {
            isAlive = false;
            return true;
        }
        return false;
    }

    // 得到子弹的矩形(用于碰撞检测)
    public Rectangle getRectangle() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
