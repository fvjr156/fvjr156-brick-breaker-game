package src.com.fvjapps.brickgame;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SettingsWindow extends JFrame {
    public SettingsWindow(Game game) {
        
        String langCode = game.getLanguageCode();
        String[] strings = Strings.getStrings(langCode);

        setTitle(strings[8]); // Settings window title
        setSize((game.getLanguageCode().equals("JP") ? 550 : 380), 200);
        setResizable(false);
        setLayout(new GridLayout(6, 2, 5, 5));
        setLocationRelativeTo(null); // center window

        // Language label and combo box
        add(new JLabel(strings[9] + ":"));
        String[] languages = { "EN", "JP", "CN" };
        JComboBox<String> langBox = new JComboBox<>(languages);
        langBox.setSelectedItem(langCode);
        add(langBox);

        // Brick Rows label and text field
        add(new JLabel(strings[10] + ":"));
        JTextField rowsField = new JTextField(String.valueOf(game.getMapRows()));
        add(rowsField);

        // Brick Columns label and text field
        add(new JLabel(strings[11] + ":"));
        JTextField colsField = new JTextField(String.valueOf(game.getMapCols()));
        add(colsField);

        // Ball Count label and text field
        add(new JLabel(strings[12] + ":"));
        JTextField ballCountField = new JTextField(String.valueOf(game.getBallCount()));
        add(ballCountField);

        // Empty label for spacing
        add(new JLabel(""));

        // Save & Close button
        JButton applyBtn = new JButton(strings[13]); 
        applyBtn.addActionListener(e -> {
            String selectedLang = (String) langBox.getSelectedItem();
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());
            int ballCount = Integer.parseInt(ballCountField.getText());

            game.applySettings(selectedLang, rows, cols, ballCount);
            dispose();
        });
        add(applyBtn);

        // CRAZY Mode button
        JButton funModeButton = new JButton(strings[14]); 
        funModeButton.addActionListener(e -> {
            String selectedLang = (String) langBox.getSelectedItem();
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());
            int ballCount = Integer.parseInt(ballCountField.getText());

            game.applySettings(selectedLang, rows, cols, ballCount);
            game.enableFunMode();
            dispose();
        });
        add(funModeButton);
        add(new JLabel(strings[15]));

        setVisible(true);
    }
}
