package vm.Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import vm.Components.Logger.*;

public class GUI implements Observer {
    Stack stack;
    ArrayList<Register> registers;
    Memory memory;
    Observable logger;

    private JPanel terminalPanel;
    private JPanel mainPanel;
    private JPanel stackPanel;
    private JPanel valuesPanel;
    private JPanel registersPanel;

    public JButton runButton;

    public GUI(Stack stack, ArrayList<Register> registers, Memory memory, Observable logger) {
        this.stack = stack;
        this.registers = registers;
        this.memory = memory;
        this.logger = logger;
        logger.addObserver(this);

        JFrame frame = this.initiateFrame();
        this.mainPanel = new JPanel();

        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        this.mainPanel.setBackground(Color.DARK_GRAY);
        this.mainPanel.setBorder(BorderFactory.createDashedBorder(Color.DARK_GRAY));
        this.mainPanel.setPreferredSize(new Dimension(800, 800));

        JLabel heading = creteHeading("Calingaert  Computer", Color.WHITE, Color.darkGray, 800, 50, 2);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.mainPanel.add(heading);

        this.valuesPanel = new JPanel();
        this.valuesPanel.setBorder(BorderFactory.createLineBorder(null));
        this.valuesPanel.setBackground(Color.DARK_GRAY);
        this.valuesPanel.setLayout(new GridLayout(2,3, 15, 15));

        this.stackPanel = this.createTablePanel(this.stack.getSize());
        this.setStackValues();
        valuesPanel.add(stackPanel);

        this.registersPanel = this.createTablePanel(this.registers.size());
        this.setRegistersValues();
        valuesPanel.add(registersPanel);

        this.terminalPanel = this.initiateTerminalPanel();
        valuesPanel.add(terminalPanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        controlPanel.setLayout(new GridBagLayout());
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 0;
        this.runButton = this.createButton("run");

        controlPanel.add(this.runButton, gbc);
        valuesPanel.add(controlPanel);

        this.mainPanel.add(valuesPanel);

        frame.add(this.mainPanel);
        frame.pack();
    }

    public void updateUI(){
        this.mainPanel.updateUI();
    }

    public void cleanTerminalPanel(){
        this.terminalPanel.removeAll();
    }

    public void addLabelTerminalPanel(String message, Color color){
        JLabel logLabel = this.creteHeading(message, color, Color.BLACK, 100, 50, 1);
        this.terminalPanel.add(logLabel);
        this.updateUI();
    }

    private JButton createButton(String label){
        JButton button = new JButton(label);
        button.setBackground(Color.WHITE);
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        button.setAlignmentY(JButton.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(100, 100));

        return button;
    }

    private JPanel createTablePanel(int tableSize){
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(tableSize, 1));

        return panel;
    }

    private void setStackValues(){
        for(int i = 0; i < this.stack.getSize(); i++){
            String value = i + ": " + this.memory.getValue(i);
            JLabel valueLabel = this.creteHeading(value, Color.WHITE, null, 50, 20, 1);
            this.stackPanel.add(valueLabel);
        }
    }

    private void setRegistersValues(){
        for(int i = 0; i < this.registers.size(); i++){
            String value = this.registers.get(i).identifier + ": " + this.registers.get(i).getValue();
            JLabel valueLabel = this.creteHeading(value, Color.WHITE, null, 50, 50, 1);
            this.registersPanel.add(valueLabel);
        }
    }

    public void updateStackPanel(){
        this.stackPanel.removeAll();
        this.setStackValues();;
        this.updateUI();
    }

    public void updateRegisterPanel(){
        this.registersPanel.removeAll();
        this.setRegistersValues();
        this.updateUI();
    }

    private JPanel initiateTerminalPanel(){
        JPanel terminalPanel = new JPanel();
        terminalPanel.setLayout(new BoxLayout (terminalPanel, BoxLayout.Y_AXIS));
        terminalPanel.setBackground(Color.BLACK);
        terminalPanel.setOpaque(true);
        
        return terminalPanel;
    }

    private JFrame initiateFrame(){
        JFrame frame = new JFrame();
        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Virtual Machine");
        frame.setVisible(true);

        return frame;
    }

    JLabel creteHeading(String text, Color fontColor, Color borderColor, int width, int height, int fontSize){
        JLabel heading = new JLabel(text);
        heading.setForeground(fontColor);
        heading.setPreferredSize(new Dimension(width, height));

        // Set the label's font size to the newly determined size.
        Font labelFont = heading.getFont();
        heading.setFont(new Font(labelFont.getName(), Font.PLAIN, labelFont.getSize() * fontSize));
        heading.setBorder(BorderFactory.createLineBorder(borderColor));

        // align to the center of its own box
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setHorizontalAlignment(JLabel.CENTER);

        return heading;
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Logger) {
            Logger logger = (Logger) observable;
            Log newLog = logger.getLastLog();
            this.addLabelTerminalPanel(newLog.message, newLog.type);
        }
    }
}
