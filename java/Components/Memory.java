package Components;

import java.util.ArrayList;

public class Memory {
    ArrayList<String> values;
    int size;

    public Memory(int size){
        this.size = size;
        this.values = new ArrayList<String>();
    }

    public String getValue(int position){
        return this.values.get(position);
    }

    public void setValue(int position, String value){
        if(position < size){
            this.values.set(position, value);
        } else {
            throw new IllegalArgumentException("Invalid position, out of memory range");
        }
    }

    public void pushValue(String value){
        this.values.add(value);
    }
}
