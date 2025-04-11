package src.com.fvjapps.brickgame;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Game extends JPanel implements KeyListener, ActionListener {

    private final Bounds bounds;
    private int ballSpeed = 3;

    private boolean isPlay = false;
    private int gameScore = 0;

    private int totalBricks = 21;
    private Timer time;
    private int delay = 9;

    private int playerX = 310;

    private BallPos ballPos = new BallPos(120, 350, -1, -2);

    private boolean moveLeft = false;
    private boolean moveRight = false;

    public Game(Bounds bounds) {
        this.bounds = bounds;
        setPreferredSize(new Dimension(bounds.width(), bounds.height()));
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        time = new Timer(delay, this);
        time.start();
    }

    @Override
    protected void paintComponent(Graphics gameGfx) {
        super.paintComponent(gameGfx);

        //draws the background
        gameGfx.setColor(Color.black);
        gameGfx.fillRect(1, 1, bounds.width() - 2, bounds.height() - 2);

        //draws the borders
        gameGfx.setColor(Color.yellow);
        gameGfx.fillRect(0, 0, 3, bounds.height());
        gameGfx.fillRect(0, 0, bounds.width(), 3);
        gameGfx.fillRect(bounds.width() - 3, 0, 3, bounds.height());

        //draws the green board
        gameGfx.setColor(Color.green);
        gameGfx.fillRect(playerX, bounds.height() - 60, 110, 8);

        //draws the yellow ball
        gameGfx.setColor(Color.yellow);
        gameGfx.fillOval(ballPos.x, ballPos.y, 10, 10);

        //the game over message
        if (!isPlay) {
            gameGfx.setColor(Color.RED);
            gameGfx.setFont(new Font("Serif", Font.BOLD, 40));
            gameGfx.drawString("Game Over", bounds.width() / 2 - 90, bounds.height() / 2);

            gameGfx.setFont(new Font("Serif", Font.PLAIN, 20));
            gameGfx.drawString("Press Enter to Restart", bounds.width() / 2 - 110, bounds.height() / 2 + 40);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlay) {
            //move diagonally to bottom-right of screen as soon as enter is pressed
            ballPos.x += ballPos.xDir * ballSpeed;
            ballPos.y += ballPos.yDir * ballSpeed;

            //if it hits left and right borders, bounce
            if (ballPos.x < 0 || ballPos.x > bounds.width() - 10) {
                ballPos.xDir = -ballPos.xDir;
            }

            //if it hits top border, bounce
            if (ballPos.y < 0) {
                ballPos.yDir = -ballPos.yDir;
            }

            //if it hits the board, bounce
            if (new Rectangle(ballPos.x, ballPos.y, 20, 20)
                    .intersects(new Rectangle(playerX, bounds.height() - 60, 110, 8))) {
                ballPos.yDir = -ballPos.yDir;
            }

            //the game over, when ball goes below the board
            if (ballPos.y > bounds.height() - 10) {
                isPlay = false;
                ballPos.xDir = 0;
                ballPos.yDir = 0;
            }

        }

        if (moveLeft && playerX > 10)
            playerX -= 5;
        if (moveRight && playerX < 580)
            playerX += 5;

        repaint();
    }

    //when enter is pressed, restart game
    public void restartGame() {
        isPlay = true;
        ballPos = new BallPos(120, 350, -1, -2);
        playerX = 310;
        gameScore = 0;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("DEBUG: Unimplemented method 'keyTyped'");
    }

    //key bindings: LEFT. RIGHT. ENTER/RETURN
    //moves the board left or right
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !isPlay) {
            restartGame();
        }
    }

    //stops the movement if key released
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
    }

}
