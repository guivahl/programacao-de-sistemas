package vm.Components.Assembler;

import java.util.HashMap;
import java.util.Map;

public class Assembler {
    private int addressCounter;
    private int lineCounter;
    private Map<String, Integer> symbolTable;

    public Assembler() {
        addressCounter = 0;
        lineCounter = 1;
        symbolTable = new HashMap<>();
    }
}