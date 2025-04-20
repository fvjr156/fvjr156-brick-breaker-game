
import javax.swing.JFrame;

public class Start {
    public static void main(String[] args) {
        Bounds bounds = new Bounds(10, 10, 710, 610);
        JFrame jFrame = new JFrameBuilder(bounds, "Brick Breaker", false, JFrame.EXIT_ON_CLOSE);
        Game gameInstance = new Game(bounds);
        jFrame.add(gameInstance);
        jFrame.pack();
        jFrame.setVisible(true);
        
    }
}
