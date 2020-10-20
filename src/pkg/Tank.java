package pkg;

import java.awt.*;
import java.util.Random;
import java.util.Vector;


public class Tank {
    public static final int WIDTH = 38;         // 坦克的宽度
    public static final int HEIGHT = 38;        // 坦克的高度
    private int x, y;           // 坦克的x, y坐标
    private int oldX, oldY;     // 坦克移动之前的x, y坐标，用于碰撞墙壁后返回原来的位置
    private Direction direction;           // 坦克移动的方向
    private Direction tDirection;    // 坦克朝着的方向(画坦克时根据这个方向来画，因为静止时的方向可能为上、下、左、右中的任意一个方向)
    private int speed;          // 坦克速度
    private boolean isGood;             // 坦克类型(0. 玩家坦克  1. 电脑坦克)
    private Color color;        // 坦克颜色
    private Vector<Bullet> bullets;         // 坦克子弹
    private boolean isAlive;        // 坦克是否活着
    private boolean bU = false, bL = false, bD = false, bR = false;     // 用来判断哪个方向键被按下，分别是上、左、下、右，没被按下则为false
    private Random random = new Random();
    private int step = random.nextInt(15) + 1;           // 敌方坦克最多走多少步之后改变一次方向

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

    public Direction gettDirection() {
        return tDirection;
    }

    public void settDirection(Direction tDirection) {
        this.tDirection = tDirection;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(Vector<Bullet> bullets) {
        this.bullets = bullets;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isbU() {
        return bU;
    }

    public void setbU(boolean bU) {
        this.bU = bU;
    }

    public boolean isbL() {
        return bL;
    }

    public void setbL(boolean bL) {
        this.bL = bL;
    }

    public boolean isbD() {
        return bD;
    }

    public void setbD(boolean bD) {
        this.bD = bD;
    }

    public boolean isbR() {
        return bR;
    }

    public void setbR(boolean bR) {
        this.bR = bR;
    }

    public Tank() {
        this(10, 10, Direction.UP, true, Color.YELLOW);
    }

    public Tank(int x, int y, Direction direction, boolean isGood, Color color) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.direction = direction;
        this.tDirection = Direction.UP;
        this.speed = 5;
        this.isGood = isGood;
        this.color = color;
        this.isAlive = true;
        bullets = new Vector<>();
    }

    // 坦克移动
    public void move() {
        this.oldX = x;
        this.oldY = y;
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
            case STOP:
                break;
            default:
                break;
        }

        // 调整坦克的朝向
        if (this.direction != Direction.STOP) {
            this.tDirection = this.direction;
        }

        // 检测坦克是否出界
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (x > Game.GAME_WIDTH - Tank.WIDTH)
            x = Game.GAME_WIDTH - Tank.WIDTH;
        if (y > Game.GAME_HEIGHT - Tank.HEIGHT)
            y = Game.GAME_HEIGHT - Tank.HEIGHT;

        // 敌方坦克走step步之后随机改变方向
        if (!isGood) {
            if (step == 0) {
                Direction[] dir = Direction.values();
                this.direction = dir[random.nextInt(5)];
                step = random.nextInt(15) + 1;
            }
            --step;
            // 敌方坦克开火
            if (random.nextInt(35) > 33) {
                this.fire();
            }
        }
    }

    public void locateDirection() {
        if (bU && !bL && !bD && !bR)
            direction = Direction.UP;
        else if (!bU && bL && !bD && !bR)
            direction = Direction.LEFT;
        else if (!bU && !bL && bD && !bR)
            direction = Direction.DOWN;
        else if (!bU && !bL && !bD && bR)
            direction = Direction.RIGHT;
        else
            direction = Direction.STOP;
    }

    // 坦克开火射击
    public void fire() {
        switch (this.tDirection) {      // 根据坦克朝着的方向开火
            case UP:
                bullets.add(new Bullet(x + 18, y, Direction.UP));
                break;
            case LEFT:
                bullets.add(new Bullet(x, y + 18, Direction.LEFT));
                break;
            case DOWN:
                bullets.add(new Bullet(x + 18, y + 38, Direction.DOWN));
                break;
            case RIGHT:
                bullets.add(new Bullet(x + 38, y + 18, Direction.RIGHT));
                break;
            default:
                break;
        }
    }

    // 检测坦克是否碰到坦克
    public boolean collideTank(Tank tank) {
        if (this != tank && this.getRectangle().intersects(tank.getRectangle()) && tank.isAlive) {
            this.x = this.oldX;
            this.y = this.oldY;
            return true;
        }
        return false;
    }

    // 检测坦克是否碰到墙壁
    public boolean collideWall(Wall wall) {
        if (this.getRectangle().intersects(wall.getRectangle())) {
            this.x = this.oldX;
            this.y = this.oldY;
            return true;
        }
        return false;
    }

    // 得到坦克的矩形(用于碰撞检测)
    public Rectangle getRectangle() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
