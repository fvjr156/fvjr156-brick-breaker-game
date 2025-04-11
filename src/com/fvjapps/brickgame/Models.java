package src.com.fvjapps.brickgame;

//defining mutable struct BallPos and immutable struct Bounds

class BallPos {
    int x, y;
    int xDir, yDir;

    BallPos(int x, int y, int xDir, int yDir) {
        this.x = x;
        this.y = y;
        this.xDir = xDir;
        this.yDir = yDir;
    }
}

record Bounds(int x, int y, int width, int height) {}
