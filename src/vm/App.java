package vm;

import java.util.ArrayList;

import vm.Components.*;

public class App {
    public static void main(String[] args) throws Exception {
        Memory memory = new Memory();
        Stack stack = new Stack(memory, 10);
        Register pc = new Register();
        Register acc = new Register();

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