package vm.Components;

import java.util.ArrayList;
import java.util.Observable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import vm.Components.Interface.Button;
import vm.Components.Interface.Label;
import vm.Components.Interface.RegistersPanel;
import vm.Components.Interface.StackPanel;
import vm.Components.Interface.TerminalPanel;

public class GUI {
    Stack stack;
    ArrayList<Register> registers;
    Memory memory;

    private JPanel mainPanel;
    private JPanel stackPanel;
    private JPanel valuesPanel;
    private JPanel controlsPanel;
    private JPanel btnControlPanel;
    private JPanel userControlPanel;
    private JScrollPane terminalPanel;
    RegistersPanel registersPanel;

    public JButton runBtn;
    public JButton mountBtn;
    public JButton sendBtn;

    public JRadioButton modeStepBtn;
    public JRadioButton modeAllBtn;

    public JTextField userInput;

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

        this.bootstrapTextField();
        this.bootstrapButtons();

        this.controlsPanel = new JPanel();
        this.controlsPanel.setBackground(Color.DARK_GRAY);

        this.controlsPanel.add(this.btnControlPanel);
        this.controlsPanel.add(this.userControlPanel);

        valuesPanel.add(this.controlsPanel);

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

        this.runBtn = new Button("run",100, 100).button;
        this.runBtn.setEnabled(false);
        this.runBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               runBtn.setEnabled(false);
            };
        });

        this.mountBtn = new Button("mount", 100, 100).button;
        this.mountBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                runBtn.setEnabled(true);
            };
        });

        this.modeStepBtn = new JRadioButton("Run step", false);
        this.modeAllBtn = new JRadioButton("Run all", false);

        this.btnControlPanel.add(this.modeStepBtn, gbc);
        this.btnControlPanel.add(this.modeAllBtn, gbc);

        this.btnControlPanel.add(this.mountBtn, gbc);
        this.btnControlPanel.add(this.runBtn, gbc);
    }

    private void bootstrapTextField() {
        this.userInput = new JTextField(3);
        JLabel inputLabel = new Label("Input stream", Color.WHITE, Color.darkGray, 400, 40, 1).label;
        this.sendBtn = new Button("send", 40, 100).button;
        this.sendBtn.setEnabled(false);

        this.userControlPanel = new JPanel();
        this.userControlPanel.setBackground(Color.DARK_GRAY);
        this.userControlPanel.setLayout(new GridLayout(3, 1));

        this.userControlPanel.add(inputLabel);
        this.userControlPanel.add(this.userInput);
        this.userControlPanel.add(this.sendBtn);
    }

    public void updateUI(){
        this.mainPanel.updateUI();
    }
}
