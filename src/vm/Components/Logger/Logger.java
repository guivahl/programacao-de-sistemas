package vm.Components.Logger;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Logger extends Observable {
    public static final Color ATTENTION_MESSAGE = Color.YELLOW;
    public static final Color SUCCESS_MESSAGE = Color.GREEN;
    public static final Color ERROR_MESSAGE = Color.RED;
    
    private ArrayList<Log> logs;

    public Logger(){
        this.logs = new ArrayList<Log>();
    }

    public void logMessage(String message, Color messageType){
        this.logs.add(new Log(message, messageType));
        setChanged();
        notifyObservers();
    }

    public ArrayList<Log> getLogs() {
		return this.logs;
	}

    public Log getLastLog(){
        return this.logs.get(this.logs.size() - 1);
    }
}
