import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.KeyListener;
import java.net.URL;
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

    private Timer time;
    private BallPos ballPos = new BallPos(220, 350, -1, -2);
    private final Bounds bounds;

    private int ballSpeed = 3;
    private int paddleWidth = 110;
    private int gameScore = 0;
    private int delay = 8;
    private int playerX = 310;

    private boolean isPlay = false;
    private boolean shiftPressed = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    private enum GameMode {
        EASY,
        NORMAL,
        EXPERT
    };

    private GameMode mode = GameMode.NORMAL;

    private String languageCode = "EN";
    private String[] strings = Strings.getStrings(languageCode);
    private int mapRows = 19, mapCols = 18;

    private MapGenerator mapGen;

    public Game(Bounds bounds) {
        this.bounds = bounds;
        setPreferredSize(new Dimension(bounds.width(), bounds.height()));
        mapGen = new MapGenerator(19, 18);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        time = new Timer(delay, this);
        time.start();
        ballHitSound();
    }

    @Override
    protected void paintComponent(Graphics gameGfx) {
        super.paintComponent(gameGfx);

        // draws the background
        gameGfx.setColor(Color.black);
        gameGfx.fillRect(1, 1, bounds.width() - 2, bounds.height() - 2);

        // drawing the bricks (mapGen)
        mapGen.draw((Graphics2D) gameGfx);

        // draws the borders
        gameGfx.setColor(Color.yellow);
        gameGfx.fillRect(0, 0, 3, bounds.height());
        gameGfx.fillRect(0, 0, bounds.width(), 3);
        gameGfx.fillRect(bounds.width() - 3, 0, 3, bounds.height());

        // score
        String scoreText = strings[7] + gameScore;
        Font scoreFont = new Font("Dialog", Font.BOLD, 25);
        gameGfx.setFont(scoreFont);
        FontMetrics scoreMetrics = gameGfx.getFontMetrics(scoreFont);
        gameGfx.setColor(Color.red);
        gameGfx.drawString(scoreText, bounds.width() - scoreMetrics.stringWidth(scoreText) - 20, 30);

        // game mode
        String modeLabel = strings[4]; // default EASY
        if (mode == GameMode.NORMAL)
            modeLabel = strings[5];
        else if (mode == GameMode.EXPERT)
            modeLabel = strings[6];
        gameGfx.drawString(modeLabel, 20, 30);

        // draws the green paddle
        gameGfx.setColor(Color.green);
        gameGfx.fillRect(playerX, bounds.height() - 60, paddleWidth, 8);

        // draws the yellow ball
        gameGfx.setColor(Color.yellow);
        gameGfx.fillOval(ballPos.x, ballPos.y, 10, 10);

        // the game over message
        if (!isPlay) {
            gameGfx.setColor(Color.red);
            Font titleFont = new Font("Dialog", Font.BOLD, 40);
            gameGfx.setFont(titleFont);
            FontMetrics metrics = gameGfx.getFontMetrics(titleFont);

            gameGfx.drawString(strings[0], ((bounds.width() - metrics.stringWidth(strings[0])) / 2),
                    (bounds.height() / 2));

            gameGfx.setFont(new Font("Serif", Font.ITALIC, 20));
            gameGfx.drawString(strings[1], bounds.width() / 2 - 110, bounds.height() / 2 + 40);
            gameGfx.setFont(new Font("Serif", Font.PLAIN, 16));
            gameGfx.drawString(strings[2], 20, bounds.height() - 20);
            gameGfx.drawString(strings[3], 20, bounds.height() - 40);
        }

        gameGfx.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int paddleMvtSpeed = (mode == GameMode.EXPERT ? (shiftPressed ? 30 : 18) : (shiftPressed ? 15 : 5));

        if (isPlay) {

            // move diagonally to bottom-right of screen as soon as enter is pressed
            ballPos.x -= ballPos.xDir * ballSpeed;
            ballPos.y -= ballPos.yDir * ballSpeed;

            // if it hits left and right borders, bounce
            if (ballPos.x < 0 || ballPos.x > bounds.width() - 10) {
                ballPos.xDir = -ballPos.xDir;
            }

            // if it hits top border, bounce
            if (ballPos.y < 0) {
                ballPos.yDir = -ballPos.yDir;
            }

            // if it hits the paddle, bounce
            if (new Rectangle(ballPos.x, ballPos.y, 20, 20)
                    .intersects(new Rectangle(playerX, bounds.height() - 60, paddleWidth, 8))) {

                int paddleCenter = playerX + paddleWidth / 2;
                int ballCenter = ballPos.x + 10 / 2; // ball diameter = 10
                int dx = ballCenter - paddleCenter;

                // Normalized value in [-1, 1]
                double relativeIntersect = (double) dx / (paddleWidth / 2);
                double maxAngle = Math.toRadians(60); // max reflection angle from vertical
                double bounceAngle = relativeIntersect * maxAngle;

                if (mode == GameMode.EXPERT) {
                    double speed = Math.sqrt(ballSpeed * ballSpeed * 2);
                    // Optional: vary speed by angle — faster at shallow angles
                    // speed multiplier (between 1.0 and ~1.4)
                    double speedFactor = 1.0 + 0.4 * Math.abs(relativeIntersect);
                    speed *= speedFactor;

                    ballPos.xDir = (int) Math.round(speed * Math.sin(bounceAngle));
                    ballPos.yDir = (int) Math.round(speed * Math.cos(bounceAngle));
                } else if (mode == GameMode.NORMAL) {
                    ballPos.xDir = (int) Math.round(ballSpeed * Math.sin(bounceAngle));
                    ballPos.yDir = (int) Math.round(ballSpeed * Math.cos(bounceAngle));
                } else {
                    ballPos.yDir = -ballPos.yDir; // classic bounce

                }

            }

            // the game over, when ball goes below the paddle
            if (ballPos.y > bounds.height() - 10) {
                isPlay = false;
                ballPos.xDir = 0;
                ballPos.yDir = 0;
            }

            outer: for (int i = 0; i < mapGen.map.length; i++) {
                for (int j = 0; j < mapGen.map[0].length; j++) {
                    if (mapGen.map[i][j] > 0) {
                        int brickWidth = mapGen.brickWidth;
                        int brickHeight = mapGen.brickHeight;
                        int brickX = j * mapGen.brickWidth + 80;
                        int brickY = i * mapGen.brickHeight + 50;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPos.x, ballPos.y, 20, 20);

                        // if hits a brick, remove brick and bounce
                        if (ballRect.intersects(brickRect)) {
                            mapGen.setBrickValue(0, i, j);
                            gameScore += 5;

                            if (ballPos.x + 19 <= brickRect.x || ballPos.x + 1 >= brickRect.x + brickRect.width) {
                                ballPos.xDir = -ballPos.xDir;
                            } else {
                                ballPos.yDir = -ballPos.yDir;
                            }

                            ballHitSound();

                            break outer;
                        }
                    }
                }
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
                ballPos = new BallPos(220, 350, -1, -2);
                playerX = 310;
                mapGen.reset();
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
        ballPos = new BallPos(220, 350, -1, -2);
        playerX = 310;
        gameScore = 0;
        mapGen.reset();
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

    private void ballHitSound() {
        try {
            URL soundURL = getClass().getResource("ballhit.wav"); // no leading slash
            if (soundURL == null) {
                System.err.println("Sound file not found");
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
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

    public void applySettings(String lang, int rows, int cols) {
        this.languageCode = lang;
        this.strings = Strings.getStrings(lang);
    
        this.mapRows = rows;
        this.mapCols = cols;
    
        this.mapGen = new MapGenerator(rows, cols);
        repaint();
    }

}
