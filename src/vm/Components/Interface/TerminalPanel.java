package vm.Components.Interface;

import java.util.Observable;
import java.util.Observer;

import java.awt.Color;
import java.awt.BorderLayout;  

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import vm.Components.Logger.Logger;
import vm.Components.Logger.Log;

public class TerminalPanel implements Observer {
    public JScrollPane panel;
    JPanel innerPanel;

    public TerminalPanel(Observable logger){
        logger.addObserver(this);

        this.panel = new JScrollPane();
        this.panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  
        this.panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
        this.panel.setLayout(new ScrollPaneLayout());

        this.innerPanel = new JPanel();
        this.innerPanel.setLayout(new BoxLayout(this.innerPanel, BoxLayout.Y_AXIS));
        this.innerPanel.setBackground(Color.BLACK);
        this.innerPanel.setOpaque(true);

        this.panel.setViewportView(innerPanel);
    }

    public void updateUI(){
        this.innerPanel.updateUI();
    }

    public void addLabelTerminalPanel(String message, Color color){
        Label log = new Label(message, color, Color.BLACK, 100, 50, 1);
        this.innerPanel.add(log.label);
        this.updateUI();
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
