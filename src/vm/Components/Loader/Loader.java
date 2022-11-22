package vm.Components.Loader;

import vm.Components.FileHandlers.Reader;
import vm.Components.Linker.Linker;
import vm.Components.Memory;
import vm.Components.Stack;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Loader {

    private final int WORD_SIZE = 16;
    private Stack stack;
    private int initialMemoryAddress;

    public Loader(Memory memory, String fileName, ArrayList<Boolean> referenceVector) throws FileNotFoundException {
        int stackSize;
        Reader reader = new Reader(fileName);
        List<String> objectCode = new ArrayList<>(Arrays.asList(reader.readFile().split("(?<=\\G.{16})")));


        stackSize = defineStackSize(objectCode);
        this.initialMemoryAddress = stackSize;

        // Initialize Stack
        this.stack = new Stack(memory, initialMemoryAddress);

        //Reallocate addresses based on reference vector
        reallocateAddress(objectCode, referenceVector);
        // Load to memory
        loadToMemory(memory, objectCode);

    }

    private void reallocateAddress(List<String> objectCode, ArrayList<Boolean> referenceVector) {
        int currentAddress;
        for (int i = 0; i < objectCode.size(); i++) {
            if(referenceVector.get(i)){
                currentAddress = Integer.parseInt(objectCode.get(i),2);
                objectCode.set(i, padString(Integer.toBinaryString(currentAddress+initialMemoryAddress), WORD_SIZE));
            }
        }
    }

    private int defineStackSize(List<String> objectCode) {
        int stackSize = Integer.parseInt(objectCode.get(objectCode.size() - 1), 2);
        objectCode.remove(objectCode.size() - 1);
        return stackSize;
    }

    private void loadToMemory(Memory memory, List<String> objectCode) {
        objectCode.forEach(word -> memory.pushValue(word));
    }

    private String padString(String word, int padSize) {
        return String.format("%" + padSize + "s", word).replace(' ', '0');
    }

    public Stack getStack(){
        return this.stack;
    }
}


