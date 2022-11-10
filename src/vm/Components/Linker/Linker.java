package vm.Components.Linker;

import vm.App;
import vm.Components.FileHandlers.Reader;
import vm.Components.FileHandlers.Writer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Linker {
    private ArrayList<String> finalSourceCode = new ArrayList<String>();
    private HashMap<String, Integer> globalSymbolTable = new HashMap<>();
    private HashMap<Integer, String> globalReferenceTable = new HashMap<>();
    private int currentEndAddress = 0;
    static private final int WORD_SIZE = 16;

    public Linker(int baseAddress, int numFiles) throws Exception {
        Reader scReader;
        Reader stReader;
        Reader rtReader;
        Writer writer = new Writer("finalSourceCode.txt");

        for (int i = 0; i < numFiles;i++) {
                scReader = new Reader("source-code" + i + ".txt");
                stReader = new Reader("symbol-table" + i + ".txt");
                rtReader = new Reader("reference-table" + i + ".txt");
                firstPass(scReader,stReader,rtReader, i);
                secondPass();
        }

        finalSourceCode.forEach(word -> writer.write(word));
        writer.close();
    }

    private void firstPass(Reader sourceCode, Reader symbolTable, Reader referenceTable, int idFile) throws Exception {
        updateSymbolTable(symbolTable, idFile);
        updateReferenceTable(referenceTable, idFile);
        updateSourceCode(sourceCode);
    }

    private void secondPass() {
        String binaryAddress;
        for(int i = 0; i < currentEndAddress;i++) {
            if(globalReferenceTable.containsKey(i)) {
                binaryAddress = Integer.toBinaryString(globalSymbolTable.get(globalReferenceTable.get(i)));
                finalSourceCode.set(i, padString(binaryAddress, WORD_SIZE));
            }
        }
    }

    private String padString(String word, int padSize) {
        return String.format("%" + padSize + "s", word).replace(' ', '0');
    }

    private void updateSymbolTable(Reader symbolTable, int idFile) throws Exception {
        String line;
        String[] input;
        while((line = symbolTable.readLine()) != null){
            input = line.split(" ");
            String symbol = input[0];
            int address = Integer.parseInt(input[1]);
            // Para verificar se existe colisao de entrada na tabela
            if(!globalSymbolTable.containsKey(symbol)) {
                //Adiciona entrada na GST com deslocamento necessario
                globalSymbolTable.put(symbol, address+currentEndAddress);
            } else {
                throw new Exception("Erro GST - SIMBOLO JA EXISTA NA TABELA DE SIMBOLOS - ARQUIVO "+ idFile +" - END: "+ address + " SIMB: " + symbol +" \n");
            }
        }
    }

    private void updateSourceCode(Reader sourceCode) throws FileNotFoundException {
        List<String> sourceCodeArray = new ArrayList<String>();
        String input;
        input = sourceCode.readFile();
        //Criar um arraylist com todas linhas do codigo fonte atual
        sourceCodeArray = Arrays.asList(input.split("(?<=\\G.{16})"));
        //Adiciona codigo fonte atual ao codigo fonte final
        finalSourceCode.addAll(sourceCodeArray);
        //Atualiza endereço atual para calculo de deslocamento
        currentEndAddress += sourceCodeArray.size();
    }

    private void updateReferenceTable(Reader referenceTable, int idFile) throws Exception {
        String line;
        String[] input;
        while((line = referenceTable.readLine()) != null){
            input = line.split(" ");
            String symbol = input[1];
            int address = Integer.parseInt(input[0]);
            // Para verificar se existe colisao de entrada na tabela
            if(!globalReferenceTable.containsKey(address+currentEndAddress)) {
                //Adiciona entrada a GFT com deslocamento necessario
                globalReferenceTable.put(address+currentEndAddress,symbol);
            } else {
                throw new Exception("Erro RST - JA EXISTE UMA REALOCAO NESSE ENDEREÇO - ARQUIVO "+ idFile +" - END: "+ address + " SIMB: " + symbol +"  \n");
            }
        }
    }

}
