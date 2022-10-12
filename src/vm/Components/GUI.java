package vm.Components;

import java.util.ArrayList;
import java.util.Observable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import vm.Components.Interface.Button;
import vm.Components.Interface.Label;
import vm.Components.Interface.RegistersPanel;
import vm.Components.Interface.StackPanel;
import vm.Components.Interface.TerminalPanel;
import vm.Components.Logger.Logger;

public class GUI {
    Stack stack;
    ArrayList<Register> registers;
    Memory memory;
    Observable logger;

    private JPanel terminalPanel;
    private JPanel mainPanel;
    private JPanel stackPanel;
    private JPanel valuesPanel;
    private JPanel btnControlPanel;
    RegistersPanel registersPanel;

    public JButton runBtn;
    public JButton mountBtn;

    public GUI(ArrayList<Register> registers, Memory memory, Observable logger) {
        JFrame frame = this.initiateFrame();
        this.mainPanel = new JPanel();

        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        this.mainPanel.setBackground(Color.DARK_GRAY);
        this.mainPanel.setBorder(BorderFactory.createDashedBorder(Color.DARK_GRAY));
        this.mainPanel.setPreferredSize(new Dimension(800, 800));

        Label heading = new Label("Calingaert  Computer", Color.WHITE, Color.darkGray, 800, 50, 2);
        heading.label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.mainPanel.add(heading.label);

        this.valuesPanel = new JPanel();
        this.valuesPanel.setBorder(BorderFactory.createLineBorder(null));
        this.valuesPanel.setBackground(Color.DARK_GRAY);
        this.valuesPanel.setLayout(new GridLayout(2,3, 15, 15));

        this.bootstrapValuePanels(registers, memory, logger);
        this.bootstrapButtons();

        this.mainPanel.add(valuesPanel);

        frame.add(this.mainPanel);
        frame.pack();
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

    private GridBagConstraints getGBCConstraints(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 0;

        return gbc;
    }

    private void bootstrapValuePanels(ArrayList<Register> registers, Memory memory, Observable logger){
        this.registersPanel = new RegistersPanel(registers);
        this.stackPanel = new StackPanel(memory).panel;
        this.terminalPanel = new TerminalPanel(logger).panel;

        this.valuesPanel.add(stackPanel);
        this.valuesPanel.add(registersPanel.panel);
        this.valuesPanel.add(terminalPanel);
    }

    private void bootstrapButtons(){
        this.btnControlPanel = new JPanel();
        this.btnControlPanel.setBackground(Color.DARK_GRAY);
        this.btnControlPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = getGBCConstraints();

        this.runBtn = new Button("run").button;
        this.runBtn.setEnabled(false);
        this.runBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               runBtn.setEnabled(false);
            };
        });

        this.mountBtn = new Button("mount").button;
        this.mountBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                runBtn.setEnabled(true);
            };
        });

        this.btnControlPanel.add(this.mountBtn, gbc);
        this.btnControlPanel.add(this.runBtn, gbc);

        valuesPanel.add(this.btnControlPanel);
    }

    public void updateUI(){
        this.mainPanel.updateUI();
    }
}
