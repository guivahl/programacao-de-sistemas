package vm.Components;

import java.util.Arrays;
import java.util.List;

public class Register {
    private final List<Integer> SIZE_OPTIONS = Arrays.asList(8, 16);
        
    String identifier;
    String value;
    int size;

    public Register(String identifier, int size) {
        if (!SIZE_OPTIONS.contains(size)) {
            String expectionMessage = String.format("Tamanho do registrador %s inválido!", identifier);
            
            throw new IllegalArgumentException(expectionMessage);
        }

        String initialValue = this.createInitialValue(size);
        
        this.identifier = identifier;
        this.size = size;
        this.value = initialValue;
    }

    public String getValue(){
        return this.value;
    }

    public void setValue(String value){
        int newValueSize = value.length();
        int registerSize = this.getSize();

        if (newValueSize != registerSize) {
            String expectionMessage = String.format("O valor do registrador %s deve possuir tamanho %2d!", this.getIdentifier(), this.getSize());
            
            throw new IllegalArgumentException(expectionMessage);
        }

        this.value = value;
    }   
    
    private int getSize() {
        return this.size;
    }
        
    private String getIdentifier() {
        return this.identifier;
    }

    private String createInitialValue(int size) {
        char INITIAL_VALUE = '0';

        char[] charArray = new char[size];
        
        Arrays.fill(charArray, INITIAL_VALUE);

        String initialValue = new String(charArray);

        return initialValue;
    }
}
