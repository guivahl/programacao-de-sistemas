package vm.Components.Assembler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vm.Components.FileHandlers.Reader;
import vm.Components.FileHandlers.Writer;
import vm.Components.Logger.Logger;

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
    private Map<Integer, String> useTable;
    private Logger logger;

    public Assembler(Logger logger) {
        addressCounter = 0;
        lineCounter = 1;
        instructionsMap = new HashMap<>();
        symbolTable = new HashMap<>();
        useTable = new HashMap<>();
        this.logger = logger;
        setInstructionData();
    }

    public void assemble(int i) {
        try {
            firstPass();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.logMessage("Error on first step of assembler!", Logger.ERROR_MESSAGE);
        } catch (AssemblerException e) {
            return;
        } finally {
            symbolTable.forEach((key, value) ->  logger.logMessage(key + ":" + value, Logger.SUCCESS_MESSAGE));
        }

        try {
            secondPass(i);
        } catch (IOException e) {
            e.printStackTrace();
            logger.logMessage("Error on second step of assembler!", Logger.ERROR_MESSAGE);
        }

        try {
            createSymbolTableTextFile(i);
            createUseTableTextFile(i);
        } catch (IOException e) {
            e.printStackTrace();
            logger.logMessage("Error trying to create symbol and use table text files!", Logger.ERROR_MESSAGE);
        }
    }

    private void firstPass() throws FileNotFoundException, AssemblerException {
        Reader reader = new Reader("MASMAPRG.ASM");
        String line = reader.readLine();
        int useTableAddress = 0;

        // retira comentarios
        line = line.split(";")[0];

        // endereco sera zero enquanto desconhecido
        int address = 0;
        while (line != null) {

            if (line.length() > LINE_MAX_LENGTH) {
                throw new AssemblerException("Assembler Exception: Invalid input at line " + lineCounter + ". Lines should have a maximum length of 80 characters.", this.logger);
            }

            String [] params = line.split(" ");
            if (params.length > LINE_MAX_PARAMS) {
                throw new AssemblerException("Assembler Exception: Syntax error at line " + lineCounter + ". Too many parameters.", this.logger);
            }

            for (String param : params) {

                param = param.trim();

                // caso seja constante
                if (param.replace("@", "").matches("^\\d+$")) {
                    // testa se pode ser representado com 16 bits
                    if (Long.valueOf(param) > PARAM_MAX_VALUE) {
                        throw new AssemblerException("Assembler Exception: Value (" + param + ") out of bounds at line " + lineCounter + ". Constant is larger than 16 bits.", this.logger);
                    }
                // caso nao seja constante, testa se o simbolo comeca com uma letra
                } else if (!param.matches(REGEX_STARTS_WITH_LETTER)) {
                    throw new AssemblerException("Assembler Exception: Invalid argument (" + param + ") at line " + lineCounter + ". Symbols should start with a letter.", this.logger);
                }

                // caso o parametro seja uma instrucao
                if (instructionsMap.containsKey(param)) {
                    if (instructionsMap.get(param).getInstructionSize() != 0) {
                        useTableAddress += 1;
                    }
                    addressCounter += instructionsMap.get(param).getInstructionSize();
                // caso o parametro seja um simbolo
                } else if (!instructionsMap.containsKey(param) && param.matches(REGEX_STARTS_WITH_LETTER)) {
                    // coloca na tabela de uso
                    if (param != params[0] || !instructionsMap.containsKey(params[1])) {
                        useTable.put(useTableAddress, param);
                        useTableAddress += 1;
                    }
                    
                    if (param.length() > PARAM_MAX_LENGTH) {
                        throw new AssemblerException("Assembler Exception: Invalid argument (" + param + ") at line " + lineCounter + ". Symbol should have a maximum length of 8 characters.", this.logger);
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
                            throw new AssemblerException("Assembler Exception: Invalid argument (" + param + ") at line " + lineCounter + ". Symbol is already defined.", this.logger);
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
                System.out.println("Assembler Exception: Missing arguments at input. Found undefined symbols at the end of the first pass.");
                //throw new AssemblerException("Assembler Exception: Missing arguments at input. Found undefined symbols at the end of the first pass.", this.logger);
            }
        }
    }

    private void secondPass(int i) throws IOException {
        Reader reader = new Reader("MASMAPRG.ASM");
        Writer writer = new Writer("source-code" + i + ".txt");
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
        writer.close();
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

    private void createSymbolTableTextFile(int i) throws IOException {
        Writer writer = new Writer("symbol-table" + i + ".txt");
        for (String symbol : symbolTable.keySet()) {
            writer.write(symbolTable.get(symbol) + " " + symbol + "\n");
        }
        writer.close();
    }

    private void createUseTableTextFile(int i) throws IOException {
        Writer writer = new Writer("use-table" + i + ".txt");
        for (int address : useTable.keySet()) {
            writer.write(address + " " + useTable.get(address) + "\n");
        }
        writer.close();
    }

    /**
     * O metodo setInstructionData popula um HashMap de instrucoes.
     * 
     * Cada instancia de um novo InstructionWrapper permite a chamada de 
     * dois metodos a partir do value do HashMap de instrucoes:
     * - getBinary: representacao da instrucao em binario (String)
     * - getInstructionSize: tamanho da instrucao (int)
     */
    private void setInstructionData() {

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