package vm.Components;

import java.util.ArrayList;
import java.util.Observable;

import vm.Components.Logger.Logger;

public class Memory extends Observable {
    private ArrayList<String> values;
    private int size = 100;
    private static final int WORD_SIZE = 16;
    private Logger logger;

    public Memory(Logger logger){
        this.values = new ArrayList<String>(this.size);
        this.logger = logger;
    }

    private boolean validateStringSize(String data){
        return data.length() == WORD_SIZE;
    }

    public String getValue(int position){
        return this.values.get(position);
    }

    public void setValue(int position, String value) throws IllegalArgumentException {
        if(position < size & this.validateStringSize(value)){
            this.values.set(position, value);
            setChanged();
            notifyObservers();
        } else {
            logger.logMessage("STACK - invalid arguments " + value, Logger.ERROR_MESSAGE);
            throw new IllegalArgumentException("Invalid arguments");
        }
    }

    public void pushValue(String value) throws IllegalArgumentException {
        if(this.validateStringSize(value)) {
            logger.logMessage(value, Logger.SUCCESS_MESSAGE);
            this.values.add(value);
            setChanged();
            notifyObservers();
        }
        else {
            logger.logMessage("MEMORY - invalid argument size: " + value, Logger.ERROR_MESSAGE);
            throw new IllegalArgumentException("Invalid value size, size must be 16 bits");
        }
    }
}
