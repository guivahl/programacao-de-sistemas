package vm.Components.Interface;

import java.util.Observable;
import java.util.Observer;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import vm.Components.Memory;

import javax.swing.BorderFactory;

public class StackPanel implements Observer {
    static int STACK_SIZE = 10;

    public JPanel panel;

    public StackPanel(Observable memory){
        memory.addObserver(this);
        this.panel = new JPanel();
        this.panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.panel.setBackground(Color.BLACK);
        this.panel.setLayout(new GridLayout(STACK_SIZE, 1));

        this.setPanelValues(memory);
    }

    private void setPanelValues(Observable memoryObs){
        if (memoryObs instanceof Memory) {
            Memory memory = (Memory) memoryObs;

            for(int i = 0; i < STACK_SIZE; i++){
                String value = i + ": " + memory.getValue(i);
                Label valueLabel = new Label(value, Color.WHITE, null, 50, 20, 1);
                this.panel.add(valueLabel.label);
            }
        }
    }

    public void updateUI(){
        this.panel.updateUI();
    }

    @Override
    public void update(Observable obs, Object arg) {
        System.out.println("chegou na atualização");
        if (obs instanceof Memory) {
            this.panel.removeAll();
            this.setPanelValues(obs);
            this.panel.updateUI();
        }
    }
}
