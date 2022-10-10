package vm.Components.Interface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Heading {
    public JLabel heading;

    public Heading(String text, Color fontColor, Color borderColor, int width, int height, int fontSize){
        this.heading = new JLabel(text);
        this.heading.setForeground(fontColor);
        this.heading.setPreferredSize(new Dimension(width, height));

        // Set the label's font size to the newly determined size.
        Font labelFont = this.heading.getFont();
        this.heading.setFont(new Font(labelFont.getName(), Font.PLAIN, labelFont.getSize() * fontSize));
        this.heading.setBorder(BorderFactory.createLineBorder(borderColor));

        // align to the center of its own box
        this.heading.setVerticalAlignment(JLabel.CENTER);
        this.heading.setHorizontalAlignment(JLabel.CENTER);
    }
}
