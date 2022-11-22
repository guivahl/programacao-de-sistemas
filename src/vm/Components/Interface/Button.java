package vm.Components.Interface;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Color;

public class Button {
    public JButton button;

    public Button(String label, int height, int width) {
        this.button = new JButton(label);
        this.button.setBackground(Color.WHITE);
        this.button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        this.button.setAlignmentY(JButton.CENTER_ALIGNMENT);
        this.button.setPreferredSize(new Dimension(width, height));
    }
}
