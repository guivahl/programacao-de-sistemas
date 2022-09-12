package Components;

import java.util.ArrayList;

public class Memory {
    ArrayList<String> values;
    int size = 100;
    private static final int WORD_SIZE = 16;

    public Memory(){
        this.values = new ArrayList<String>(this.size);
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
        } else {
            throw new IllegalArgumentException("Invalid arguments");
        }
    }

    public void pushValue(String value) throws IllegalArgumentException {
        if(this.validateStringSize(value)) this.values.add(value);
        else {
            throw new IllegalArgumentException("Invalid value size, size must be 16 bits");
        }
    }
}
