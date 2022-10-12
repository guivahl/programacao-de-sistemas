package vm.Components.Interface;

import java.util.Observable;
import java.util.Observer;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import vm.Components.Logger.Logger;
import vm.Components.Logger.Log;

public class TerminalPanel implements Observer {
    public JPanel panel;

    public TerminalPanel(Observable logger){
        logger.addObserver(this);

        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
        this.panel.setBackground(Color.BLACK);
        this.panel.setOpaque(true);
    }

    public void updateUI(){
        this.panel.updateUI();
    }

    public void addLabelTerminalPanel(String message, Color color){
        Label log = new Label(message, color, Color.BLACK, 100, 50, 1);
        this.panel.add(log.label);
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
