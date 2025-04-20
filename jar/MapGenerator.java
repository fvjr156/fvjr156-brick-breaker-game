
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        map = new int[row][col];

        // fill array with 1's (all bricks are present)
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }

        // calculate brick height and width so every single one fits on the screen
        brickWidth = 540 / col;
        brickHeight = 190 / row;
    }

    public void draw(Graphics2D gameGfx) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {

                // if not 0, draw rectangles with border
                if (map[i][j] > 0) {
                    gameGfx.setColor(Color.white);
                    gameGfx.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    gameGfx.setStroke(new BasicStroke(5));
                    gameGfx.setColor(Color.black);
                    gameGfx.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    // for Game class: set a specific brick's value
    // set brick to value = 0, draw() will not draw the rectangle
    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }

    public void reset() {
        // Refill array with 1's (all bricks are present again)
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }
    }
}
