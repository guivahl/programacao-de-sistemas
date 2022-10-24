package vm.Components.Assembler;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import vm.Components.FileHandlers.Reader;
import vm.Components.FileHandlers.Writer;

public class Assembler {
    private static final int LINE_MAX_PARAMS = 4;
    private static final int LINE_MAX_LENGTH = 80;
    private static final int PARAM_MAX_LENGTH = 8;
    private static final int PARAM_MAX_VALUE = 65536;
    private static final String REGEX_STARTS_WITH_LETTER = "[a-zA-Z_][a-zA-Z0-9_]*";

    private int addressCounter;
    private int lineCounter;
    private Map<String, InstructionWrapper> instructionsMap;
    private Map<String, Integer> symbolTable;

    public Assembler() {
        addressCounter = 0;
        lineCounter = 1;
        instructionsMap = new HashMap<>();
        symbolTable = new HashMap<>();
        setInstructionData();
        assemble();
    }

    public void assemble() {
        try {
            firstPass();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Erro no processo do primeiro passo do montador!");
        } catch (AssemblerException e) {
            return;
        } finally {
            System.out.println(symbolTable);
        }

        try {
            secondPass();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Erro no processo do segundo passo do montador!");
        }
    }

    private void firstPass() throws FileNotFoundException, AssemblerException {
        Reader reader = new Reader("MASMAPRG.ASM");
        String line = reader.readLine();

        // retira comentarios
        line = line.split(";")[0];

        // endereco sera zero enquanto desconhecido
        int address = 0;
        while (line != null) {

            if (line.length() > LINE_MAX_LENGTH) {
                throw new AssemblerException("Assembler Exception: Invalid input at line " + lineCounter + ". Lines should have a maximum length of 80 characters.");
            }

            String [] params = line.split(" ");
            if (params.length > LINE_MAX_PARAMS) {
                throw new AssemblerException("Assembler Exception: Syntax error at line " + lineCounter + ". Too many parameters.");
            }

            for (String param : params) {

                param = param.trim();

                // caso seja constante
                if (param.replace("@", "").matches("^\\d+$")) {
                    // testa se pode ser representado com 16 bits
                    if (Long.valueOf(param) > PARAM_MAX_VALUE) {
                        throw new AssemblerException("Assembler Exception: Value (" + param + ") out of bounds at line " + lineCounter + ". Constant is larger than 16 bits.");
                    }
                // caso nao seja constante, testa se o simbolo comeca com uma letra
                } else if (!param.matches(REGEX_STARTS_WITH_LETTER)) {
                    throw new AssemblerException("Assembler Exception: Invalid argument (" + param + ") at line " + lineCounter + ". Symbols should start with a letter.");
                }

                // caso o parametro seja uma instrucao
                if (instructionsMap.containsKey(param)) {
                    addressCounter += instructionsMap.get(param).getInstructionSize();
                // caso o parametro seja um simbolo
                } else if (!instructionsMap.containsKey(param) && param.matches(REGEX_STARTS_WITH_LETTER)) {

                    if (param.length() > PARAM_MAX_LENGTH) {
                        throw new AssemblerException("Assembler Exception: Invalid argument (" + param + ") at line " + lineCounter + ". Symbol should have a maximum length of 8 characters.");
                    }

                    // caso o simbolo esteja sendo definido, address recebe o contador de endereco da definicao
                    if (param == params[0]) {
                        address = addressCounter;
                        // se o segundo parametro nao contem uma instrucao, incrementa o contador de endereco em apenas um
                        if (!instructionsMap.containsKey(params[1])) {
                            addressCounter += 1;
                        }
                    // caso o simbolo seja um operando, address recebe zero pois ainda nao se sabe o endereco real
                    } else {
                        address = 0;
                    }

                    // o teste aqui esta duplicado pois o teste com null antes previne que crashe a clausula
                    // caso o simbolo nao esteja na tabela de simbolos ou esteja sem endereco setado
                    if (symbolTable.get(param) == null || symbolTable.get(param) == 0) {
                        if (symbolTable.containsKey(param) && symbolTable.get(param) == 0) {
                            symbolTable.replace(param, address);
                        } else {
                            symbolTable.put(param, address);
                        }
                    // caso o simbolo ja esteja na tabela de simbolos com um endereco setado
                    } else if (symbolTable.get(param) != 0) {
                        // se o simbolo estiver sendo redefinido, joga uma exception
                        if (param == params[0]) {
                            throw new AssemblerException("Assembler Exception: Invalid argument (" + param + ") at line " + lineCounter + ". Symbol is already defined.");
                        }
                    }

                }

            }

            line = reader.readLine();
            lineCounter++;
        }

        // testa se algum simbolo nao foi definido ao final do primeiro passo
        for (String key : symbolTable.keySet()) {
            if (symbolTable.get(key) == 0) {
                throw new AssemblerException("Assembler Exception: Missing arguments at input. Found undefined symbols at the end of the first pass.");
            }
        }
    }

    private void secondPass() throws FileNotFoundException {
        Reader reader = new Reader("MASMAPRG.ASM");
        Writer writer = new Writer("source-code.txt");
        String line = reader.readLine();
        String data;
        while (line != null) {
            String [] params = line.split(" ");
            for (String param : params) {
                // caso seja instrucao
                if (instructionsMap.containsKey(param)) {
                    data = instructionsMap.get(param).getBinary();
                // caso seja simbolo (ignora se estiver na frente)
                } else if (param != params[0] && symbolTable.containsKey(param)) {
                    data = Integer.toBinaryString(symbolTable.get(param));
                // caso seja constante/literal
                } else if (param.replace("@", "").matches("^\\d+$")) {
                    data = Long.toBinaryString(Long.valueOf(param));
                // caso nao deva adicionar nada
                } else {
                    data = "";
                }

                if (data != "") {
                    data = parseStringToBinarySixteenBits(data);
                    writer.write(data);
                    
                }
            }
            line = reader.readLine();
        }
    }

    private String parseStringToBinarySixteenBits(String binary) {
        String fullStr;
        String zeroes = "";

        // completa a string com 0s ate fechar 16
        for (int i = 0; i < 16 - binary.length(); i++) {
            zeroes += "0";
        }

        fullStr = zeroes + binary;
        return fullStr;
    }

    private void setInstructionData() {
        // key = str instruction
        // value = str binary, int instructionSize

        // instrucoes de maquina
        instructionsMap.put("ADD", new InstructionWrapper("00010", 2));
        instructionsMap.put("BR", new InstructionWrapper("00000", 2));
        instructionsMap.put("BRNEG", new InstructionWrapper("00101", 2));
        instructionsMap.put("BRPOS", new InstructionWrapper("00001", 2));
        instructionsMap.put("BRZERO", new InstructionWrapper("00100", 2));
        instructionsMap.put("CALL", new InstructionWrapper("01111", 2));
        instructionsMap.put("COPY", new InstructionWrapper("01101", 3));
        instructionsMap.put("DIVIDE", new InstructionWrapper("01010", 2));
        instructionsMap.put("LOAD", new InstructionWrapper("00011", 2));
        instructionsMap.put("MULT", new InstructionWrapper("01110", 2));
        instructionsMap.put("READ", new InstructionWrapper("01100", 2));
        instructionsMap.put("RET", new InstructionWrapper("10000", 1));
        instructionsMap.put("STOP", new InstructionWrapper("01011", 1));
        instructionsMap.put("STORE", new InstructionWrapper("00111", 2));
        instructionsMap.put("SUB", new InstructionWrapper("00110", 2));
        instructionsMap.put("WRITE", new InstructionWrapper("01000", 2));

        // instrucoes do montador
        instructionsMap.put("START", new InstructionWrapper("", 0));
        instructionsMap.put("END", new InstructionWrapper("", 0));
        instructionsMap.put("CONST", new InstructionWrapper("", 1));
        instructionsMap.put("SPACE", new InstructionWrapper("00000", 1));
        instructionsMap.put("STACK", new InstructionWrapper("", 0));
    }
}