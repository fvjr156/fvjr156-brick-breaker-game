package src.com.fvjapps.brickgame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Game extends JPanel implements KeyListener, ActionListener {

    private Timer gameTimer;
    private int delay = 12;

    private List<Ball> balls = new ArrayList<>();
    private int ballCount = 1;
    private final Bounds gameScreenBounds;
    private double ballSpeed = 3.0;

    private int paddleWidth = 115;
    private int playerX = 310;

    private boolean isPlay = false;
    private int gameScore = 0;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean shiftPressed = false;

    private enum GameMode {
        EASY,
        NORMAL,
        EXPERT
    };

    private boolean funMode = false;

    private GameMode mode = GameMode.NORMAL;

    private String languageCode = "EN";
    private String[] strings = Strings.getStrings(languageCode);
    private int mapRows = 19, mapCols = 18;

    private MapGenerator mapGen;
    private Game self;

    public Game(Bounds bounds) {
        this.self = this;
        this.gameScreenBounds = bounds;
        setPreferredSize(new Dimension(bounds.width(), bounds.height()));
        mapGen = new MapGenerator(19, 18);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        gameTimer = new Timer(delay, this);
        gameTimer.start();
        Sound.ballHitSound(self);
    }

    @Override
    protected void paintComponent(Graphics gameGfx) {
        super.paintComponent(gameGfx);

        // draws the background
        gameGfx.setColor(Color.black);
        gameGfx.fillRect(1, 1, gameScreenBounds.width() - 2, gameScreenBounds.height() - 2);

        // draws the borders
        gameGfx.setColor(Color.yellow);
        gameGfx.fillRect(0, 0, 3, gameScreenBounds.height());
        gameGfx.fillRect(0, 0, gameScreenBounds.width(), 3);
        gameGfx.fillRect(gameScreenBounds.width() - 3, 0, 3, gameScreenBounds.height());

        // drawing the bricks (mapGen)
        mapGen.draw((Graphics2D) gameGfx);

        // score
        String scoreText = strings[7] + gameScore;
        Font scoreFont = new Font("Dialog", Font.BOLD, 25);
        gameGfx.setFont(scoreFont);
        FontMetrics scoreMetrics = gameGfx.getFontMetrics(scoreFont);
        gameGfx.setColor(Color.red);
        gameGfx.drawString(scoreText, gameScreenBounds.width() - scoreMetrics.stringWidth(scoreText) - 20, 30);

        // game mode
        String modeLabel = strings[4]; // default EASY
        if (mode == GameMode.NORMAL)
            modeLabel = strings[5];
        else if (mode == GameMode.EXPERT)
            modeLabel = strings[6];
        gameGfx.drawString(modeLabel, 20, 30);

        // draws the green paddle
        gameGfx.setColor(Color.green);
        gameGfx.fillRect(playerX, gameScreenBounds.height() - 60, paddleWidth, 8);

        // draws balls
        for (Ball ball : balls) {
            ball.draw((Graphics2D) gameGfx);
        }

        // the game over message
        if (!isPlay) {
            gameGfx.setColor(Color.red);
            Font titleFont = new Font("Dialog", Font.BOLD, 40);
            gameGfx.setFont(titleFont);
            FontMetrics metrics = gameGfx.getFontMetrics(titleFont);

            gameGfx.drawString(strings[0], ((gameScreenBounds.width() - metrics.stringWidth(strings[0])) / 2),
                    (gameScreenBounds.height() / 2));

            gameGfx.setFont(new Font("Serif", Font.ITALIC, 20));
            gameGfx.drawString(strings[1], gameScreenBounds.width() / 2 - 110, gameScreenBounds.height() / 2 + 40);
            gameGfx.setFont(new Font("Serif", Font.PLAIN, 16));
            gameGfx.drawString(strings[2], 20, gameScreenBounds.height() - 20);
            gameGfx.drawString(strings[3], 20, gameScreenBounds.height() - 40);
        }

        gameGfx.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (funMode) {
            balls.add(new Ball(210, 340, -1, -2, ballSpeed));
        }

        int paddleMvtSpeed = (mode == GameMode.EXPERT ? (shiftPressed ? 30 : 20) : (shiftPressed ? 20 : 12));

        if (isPlay) {

            List<Ball> toRemove = new ArrayList<>();

            for (Ball ball : balls) {
                ball.move();

                // Bounce off walls
                if (ball.getX() < 0 || ball.getX() > gameScreenBounds.width() - ball.getDiameter()) {
                    ball.reflectX();
                }

                if (ball.getY() < 0) {
                    ball.reflectY();
                }

                // Paddle collision
                if (ball.getBounds()
                        .intersects(new Rectangle(playerX, gameScreenBounds.height() - 60, paddleWidth, 8))) {
                    int paddleCenter = playerX + paddleWidth / 2;
                    int ballCenter = ball.getX() + ball.getDiameter() / 2;
                    int dx = ballCenter - paddleCenter;

                    double relativeIntersect = (double) dx / (paddleWidth / 2);
                    double maxAngle = Math.toRadians(60);
                    double bounceAngle = relativeIntersect * maxAngle;

                    double speed = (mode == GameMode.EXPERT)
                            ? Math.sqrt(ballSpeed * ballSpeed * 2) * (1.0 + 0.4 * Math.abs(relativeIntersect))
                            : ballSpeed;

                    ball.setxDir((int) Math.round(speed * Math.sin(bounceAngle)));
                    ball.setyDir((int) Math.round(speed * Math.cos(bounceAngle)));
                }

                // Ball below screen — mark for removal
                if (ball.getY() > gameScreenBounds.height()) {
                    toRemove.add(ball);
                }

                // Brick collision
                outer: for (int i = 0; i < mapGen.map.length; i++) {
                    for (int j = 0; j < mapGen.map[0].length; j++) {
                        if (mapGen.map[i][j] > 0) {
                            int brickWidth = mapGen.brickWidth;
                            int brickHeight = mapGen.brickHeight;
                            int brickX = j * mapGen.brickWidth + 80;
                            int brickY = i * mapGen.brickHeight + 50;

                            Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                            Rectangle ballRect = ball.getBounds();

                            if (ballRect.intersects(brickRect)) {
                                mapGen.setBrickValue(0, i, j);
                                gameScore += 5;

                                if (ball.getX() + 19 <= brickRect.x
                                        || ball.getX() + 1 >= brickRect.x + brickRect.width) {
                                    ball.reflectX();
                                } else {
                                    ball.reflectY();
                                }

                                Sound.ballHitSound(self);
                                break outer;
                            }
                        }
                    }
                }
            }

            balls.removeAll(toRemove);

            // Game over if no balls left
            if (balls.isEmpty()) {
                isPlay = false;
            }

            // Check if all bricks are cleared
            boolean allBricksCleared = true;
            for (int i = 0; i < mapGen.map.length; i++) {
                for (int j = 0; j < mapGen.map[0].length; j++) {
                    if (mapGen.map[i][j] > 0) {
                        allBricksCleared = false;
                        break;
                    }
                }
                if (!allBricksCleared)
                    break;
            }

            if (allBricksCleared) {
                playerX = 310;
                mapGen.reset();
                balls.clear();
                for (int i = 0; i < ballCount; i++) {
                    balls.add(new Ball(190 + (i + 1 * 100), 300 + (i + 1 * 100), -1, -2, ballSpeed));
                }
                isPlay = true;
                repaint();
            }

        }

        // paddle movement
        if (moveLeft && playerX > 5)
            playerX -= paddleMvtSpeed;
        if (moveRight && playerX < 595)
            playerX += paddleMvtSpeed;

        repaint();
    }

    // when enter is pressed, restart game
    public void restartGame() {
        isPlay = true;
        playerX = 310;
        gameScore = 0;
        mapGen.reset();
        balls.clear();
        for (int i = 0; i < ballCount; i++) {
            balls.add(new Ball(110 + (i + 1 * 100), 240 + (i + 1 * 100), -1, -2, ballSpeed));
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
        // throw new UnsupportedOperationException("DEBUG: Unimplemented method
        // 'keyTyped'");
    }

    // key bindings: LEFT. RIGHT. ENTER/RETURN
    // moves the paddle left or right
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
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
            if (!isPlay) {
                mode = GameMode.EASY;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            if (!isPlay) {
                mode = GameMode.NORMAL;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_3) {
            if (!isPlay) {
                mode = GameMode.EXPERT;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_0) {
            if (!isPlay) {
                // open settings window
                new SettingsWindow(this);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (funMode) {
                isPlay = false;
                funMode = false;
            }
        }
    }

    // stops the movement if key released
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public int getMapRows() {
        return mapRows;
    }

    public int getMapCols() {
        return mapCols;
    }

    public int getBallCount() {
        return ballCount;
    }

    public void applySettings(String lang, int rows, int cols, int ballCount) {
        this.languageCode = lang;
        this.strings = Strings.getStrings(lang);

        this.mapRows = rows;
        this.mapCols = cols;

        this.ballCount = ballCount;

        this.mapGen = new MapGenerator(rows, cols);
        repaint();
    }

    public void enableFunMode() {
        funMode = true;
        repaint();
    }

}
