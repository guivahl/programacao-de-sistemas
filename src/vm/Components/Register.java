package vm.Components;

import java.util.Arrays;
import java.util.List;

public class Register {
    private final List<Integer> SIZE_OPTIONS = Arrays.asList(8, 16);
        
    public String identifier;
    private String value;
    private int size;

    public Register(String identifier, int size) {
        if (!SIZE_OPTIONS.contains(size)) {
            String excepctionMessage = String.format("Size of register %s is invalid!", identifier);
            
            throw new IllegalArgumentException(excepctionMessage);
        }

        String initialValue = this.createInitialValue(size);
        
        this.identifier = identifier;
        this.size = size;
        this.value = initialValue;
    }

    public String getValue(){
        return this.value;
    }
    
    private int getSize() {
        return this.size;
    }
        
    private String getIdentifier() {
        return this.identifier;
    }

    public void setValue(String value){
        int newValueSize = value.length();
        int registerSize = this.getSize();

        if (newValueSize != registerSize) {
            String excepctionMessage = String.format("The value of register %s should have size %2d!", this.getIdentifier(), this.getSize());
            
            throw new IllegalArgumentException(excepctionMessage);
        }

        this.value = value;
    }   

    private String createInitialValue(int size) {
        String BIT_VALUE_ZERO = "0";

        String initialMemoryValue = BIT_VALUE_ZERO.repeat(size);
        
        return initialMemoryValue;
    }
}
