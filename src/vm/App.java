package vm;

import java.util.ArrayList;

import vm.Components.Memory;
import vm.Components.Register;
import vm.Components.SourceCodeHandler;
import vm.Components.Stack;
import vm.Components.Assembler.Assembler;

public class App {
    public static void main(String[] args) throws Exception {
        Memory memory = new Memory();
        Stack stack = new Stack(memory, 10);

        Register programCounter = new Register("PC", 16);
        Register stackPointer = new Register("SP", 16);
        Register accumulator = new Register("ACC", 16);
        Register operationMode = new Register("MOP", 8);
        Register instructionRegister = new Register("RI", 16);
        Register memoryAddressRegister = new Register("RE", 16);

        Assembler assembler = new Assembler();

        // Lê o source code e inicializa a memória com o programa
        SourceCodeHandler reader = new SourceCodeHandler();
        String data = reader.readFile("source-code.txt");
        ArrayList<String> words = reader.parseStringToWords(data);
        for(String word : words) {
            memory.pushValue(word);
        }

        // testando
        stack.push("0000000000000101");
        System.out.println(memory.getValue(0));
        System.out.println(stack.pop());
        System.out.println(memory.getValue(0));
    }
}