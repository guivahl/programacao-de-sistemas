import java.util.ArrayList;

import Components.*;

public class App {
    public static void main(String[] args) throws Exception {
        Memory memory = new Memory(100);
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
        stack.push("00000000000000101");
        System.out.println(stack.pop());
    }
}