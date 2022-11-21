package vm.Components.Interface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Label {
    public JLabel label;

    public Label(String text, Color fontColor, Color borderColor, int width, int height, int fontSize){
        this.label = new JLabel(text);
        this.label.setForeground(fontColor);
        this.label.setPreferredSize(new Dimension(width, height));

        // Ajusta o tamanho da fonte para o tamanho especificado nos paramêtros
        Font labelFont = this.label.getFont();
        this.label.setFont(new Font(labelFont.getName(), Font.PLAIN, labelFont.getSize() * fontSize));
        this.label.setBorder(BorderFactory.createLineBorder(borderColor));

        // Alinha o texto no centro da seu próprio eixo, da sua própria caixa
        this.label.setVerticalAlignment(JLabel.CENTER);
        this.label.setHorizontalAlignment(JLabel.CENTER);
    }
}
