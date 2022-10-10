package vm.Components.Assembler;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import vm.Components.FileHandlers.Reader;
import vm.Components.FileHandlers.Writer;

public class Assembler {
    private int addressCounter;
    private int lineCounter;
    private Map<String, InstructionsWrapper> instructionsMap;
    private Map<String, Integer> symbolTable;

    public Assembler() {
        addressCounter = 0;
        lineCounter = 1;
        instructionsMap = new HashMap<>();
        symbolTable = new HashMap<>();
        setOpcodeValues();
        assemble();
    }

    public void assemble() {
        try {
            firstPass();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Erro no processo do primeiro passo do montador!");
        }
    }

    public void firstPass() throws FileNotFoundException {
        Reader reader = new Reader("MASMAPRG.ASM");
        String line = reader.readLine();
        int address = 0; // zero caso desconhecido
        while (line != null) {
            String [] params = line.split(" ");
            for (String param : params) {
                param = param.trim();
                if (instructionsMap.containsKey(param)) {
                    addressCounter += instructionsMap.get(param).getInstructionSize();
                } else if (!instructionsMap.containsKey(param) && param.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                    // se segundo param nao for uma instrucao
                    if (param == params[0]) {
                        address = addressCounter;
                        if (!instructionsMap.containsKey(params[1])) {
                            addressCounter += 1;
                        }
                    } else {
                        address = 0;
                    }

                    if (symbolTable.get(param) == null || symbolTable.get(param) == 0) {
                        if (symbolTable.containsKey(param) && symbolTable.get(param) == 0) {
                            symbolTable.replace(param, address);
                        } else {
                            symbolTable.put(param, address);
                        }
                    }
                }
            }

            line = reader.readLine();
            lineCounter++;
        }

        System.out.println(symbolTable);
    }

    public void setOpcodeValues() {
        // key = str instruction
        // value = str binary, int instructionSize

        // instrucoes de maquina
        instructionsMap.put("ADD", new InstructionsWrapper("00010", 2));
        instructionsMap.put("BR", new InstructionsWrapper("00000", 2));
        instructionsMap.put("BRNEG", new InstructionsWrapper("00101", 2));
        instructionsMap.put("BRPOS", new InstructionsWrapper("00001", 2));
        instructionsMap.put("BRZERO", new InstructionsWrapper("00100", 2));
        instructionsMap.put("CALL", new InstructionsWrapper("01111", 2));
        instructionsMap.put("COPY", new InstructionsWrapper("01101", 3));
        instructionsMap.put("DIVIDE", new InstructionsWrapper("01010", 2));
        instructionsMap.put("LOAD", new InstructionsWrapper("00011", 2));
        instructionsMap.put("MULT", new InstructionsWrapper("01110", 2));
        instructionsMap.put("READ", new InstructionsWrapper("01100", 2));
        instructionsMap.put("RET", new InstructionsWrapper("10000", 1));
        instructionsMap.put("STOP", new InstructionsWrapper("01011", 1));
        instructionsMap.put("STORE", new InstructionsWrapper("00111", 2));
        instructionsMap.put("SUB", new InstructionsWrapper("00110", 2));
        instructionsMap.put("WRITE", new InstructionsWrapper("01000", 2));
    
        // instrucoes do montador
        instructionsMap.put("START", new InstructionsWrapper("", 0));
        instructionsMap.put("END", new InstructionsWrapper("", 0));
        instructionsMap.put("CONST", new InstructionsWrapper("", 1));
        instructionsMap.put("SPACE", new InstructionsWrapper("", 1));
        instructionsMap.put("STACK", new InstructionsWrapper("", 0));
    }
}