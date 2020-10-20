package pkg;

import java.awt.*;

public class Wall {
    private int x, y;           // 墙壁的x, y坐标
    private int width, height;          // 墙壁的宽度, 高度

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Wall() {
        this(0, 0, 0, 0);
    }

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // 得到墙壁的矩形(用于碰撞检测)
    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }
}
