package Components;

import java.util.Arrays;

public class Stack {
    Memory memory;
    int size;
    int currentPosition;
    String initialValue;

    public Stack(Memory memory, int size) {
        this.memory = memory;
        this.size = size;
        this.initialValue = this.generateInitialValue();
        this.initializeStack();
    }

    private String generateInitialValue(){
        char INITIAL_VALUE = '0';
        char[] charArray = new char[15];
        Arrays.fill(charArray, INITIAL_VALUE);
        String initialValue = new String(charArray);
        return initialValue;
    }

    // inicializa as posições da stack no inicio da memória com o valor zero
    // ex: se a stack tem tamanho 5, as 5 primeiras posições da memória [0:4] são preenchidas por 000000000000000
    private void initializeStack() {
        for(int i = 0; i < this.size; i++) {
            this.memory.pushValue(this.initialValue);
        }

        this.currentPosition = 0;
    }

    // adiciona valores na pilha até a posição limite
    public void push(String value) throws Exception {
        if(currentPosition < this.size){
            this.memory.setValue(currentPosition, value);
            currentPosition++;
        } else {
            throw new Exception("Stack overflow");
        }
    }

    // remove valores da pilha até chegar na base da pilha
    public String pop() throws Exception {
        if(currentPosition != 0){
            this.currentPosition--;
            String data = this.memory.getValue(currentPosition);
            this.memory.setValue(currentPosition, this.initialValue);
            return data;
        } else {
            throw new Exception("Invalid stack access");
        }
    }
}
