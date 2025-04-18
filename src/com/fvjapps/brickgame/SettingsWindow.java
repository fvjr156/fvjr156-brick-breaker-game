package src.com.fvjapps.brickgame;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SettingsWindow extends JFrame {
    public SettingsWindow(Game game) {
        setTitle("Settings");
        setSize(300, 200);
        setLayout(new GridLayout(5, 2, 5, 5));
        setLocationRelativeTo(null); // center window

        // where user selects language
        add(new JLabel("Language:"));
        String[] languages = {"EN", "JP"};
        JComboBox<String> langBox = new JComboBox<>(languages);
        langBox.setSelectedItem(game.getLanguageCode());
        add(langBox);

        // rows input
        add(new JLabel("Brick Rows:"));
        JTextField rowsField = new JTextField(String.valueOf(game.getMapRows()));
        add(rowsField);

        // columns input
        add(new JLabel("Brick Columns:"));
        JTextField colsField = new JTextField(String.valueOf(game.getMapCols()));
        add(colsField);
        add(new JLabel(""));

        // apply changes
        JButton applyBtn = new JButton("Save & Close");
        applyBtn.addActionListener(e -> {
            String lang = (String) langBox.getSelectedItem();
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());

            game.applySettings(lang, rows, cols);
            dispose();
        });
        add(applyBtn);

        setVisible(true);
    }
}
