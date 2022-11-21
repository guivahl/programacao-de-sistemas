package vm.Components.Interface;

import vm.Components.Register;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;

public class RegistersPanel {
    ArrayList<Register> registers;
    public JPanel panel;

    public RegistersPanel(ArrayList<Register> registers){
        this.registers = registers;

        this.panel = new JPanel();
        this.panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.panel.setBackground(Color.BLACK);
        this.panel.setLayout(new GridLayout(registers.size(), 1));

        this.setRegistersValues();
    }

    private void setRegistersValues(){
        for(int i = 0; i < this.registers.size(); i++){
            String value = this.registers.get(i).identifier + ": " + this.registers.get(i).getValue();
            Label valueLabel = new Label(value, Color.WHITE, null, 50, 50, 1);
            this.panel.add(valueLabel.label);
        }
    }

    public void updateUI(){
        this.panel.updateUI();
    }

    public void updatePanelValues(){
        this.panel.removeAll();
        this.setRegistersValues();
        this.updateUI();
    }
}
