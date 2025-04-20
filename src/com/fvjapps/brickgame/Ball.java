package src.com.fvjapps.brickgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Ball {
    private int x, y;
    private int xDir, yDir;
    private final int diameter = 10;
    private double ballSpeed;
    private Color ballColor;

    public Ball(int x, int y, int xDir, int yDir, double speed, Color color) {
        this.x = x;
        this.y = y;
        this.xDir = xDir;
        this.yDir = yDir;
        this.ballSpeed = speed;
        this.ballColor = color;
    }

    public Ball(int x, int y, int xDir, int yDir, double speed) {
        this.x = x;
        this.y = y;
        this.xDir = xDir;
        this.yDir = yDir;
        this.ballSpeed = speed;
        this.ballColor = Color.yellow;
    }

    public void move() {
        x -= xDir * ballSpeed;
        y -= yDir * ballSpeed;
    }

    public void reflectX() {
        xDir = -xDir;
    }

    public void reflectY() {
        yDir = -yDir;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void draw(Graphics2D g) {
        g.setColor(ballColor);
        g.fillOval(x, y, diameter, diameter);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getxDir() {
        return xDir;
    }

    public int getyDir() {
        return yDir;
    }

    public int getDiameter() {
        return diameter;
    }

    public double getBallSpeed() {
        return ballSpeed;
    }

    public void setxDir(int xDir) {
        this.xDir = xDir;
    }

    public void setyDir(int yDir) {
        this.yDir = yDir;
    }

    public void setBallSpeed(double ballSpeed) {
        this.ballSpeed = ballSpeed;
    }

    public void reset(int x, int y, int xDir, int yDir, double speed) {
        this.x = x;
        this.y = y;
        this.xDir = xDir;
        this.yDir = yDir;
        this.ballSpeed = speed;
    }

}
