package vm.Components.Interface;

import java.util.Observable;
import java.util.Observer;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import vm.Components.Memory;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class StackPanel implements Observer {
    static int STACK_SIZE = 10;

    Observable memoryObs;
    JPanel panel;

    public StackPanel(Observable memory){
        this.memoryObs = memory;

        this.panel = new JPanel();
        this.panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.panel.setBackground(Color.BLACK);
        this.panel.setLayout(new GridLayout(STACK_SIZE, 1));

        this.setPanelValues();
    }

    private void setPanelValues(){
        if (memoryObs instanceof Memory) {
            Memory memory = (Memory) memoryObs;

            for(int i = 0; i < STACK_SIZE; i++){
                String value = i + ": " + memory.getValue(i);
                Heading valueLabel = new Heading(value, Color.WHITE, null, 50, 20, 1);
                this.panel.add(valueLabel.heading);
            }
        }
    }

    public void updateUI(){
        this.panel.updateUI();
    }


    @Override
    public void update(Observable obs, Object arg) {
        if (obs instanceof Memory) {
            this.memoryObs = obs;
            this.setPanelValues();
        }
    }
}
