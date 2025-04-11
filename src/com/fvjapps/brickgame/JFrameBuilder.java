package src.com.fvjapps.brickgame;

import javax.swing.JFrame;

public class JFrameBuilder extends JFrame {

    public JFrameBuilder(Bounds bounds, String title, boolean isResizable, int defaultCloseOper) {
        super(title);
        this.setBounds(bounds.x(), bounds.y(), bounds.width(), bounds.height());
        this.setResizable(isResizable);
        this.setDefaultCloseOperation(defaultCloseOper);
    }
    
}