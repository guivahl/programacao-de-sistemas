package vm.Components;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class SourceCodeHandler {
    private static final int WORD_SIZE = 16;
    public String readFile(String filename) throws FileNotFoundException{
        String data = "";
        Path path = Path.of("src", "resources", filename);
        try {
            File file = new File(path.toAbsolutePath().toString());
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException error) {
            throw new FileNotFoundException("Invalid pathname, file not found.");
        }
        return data;
    }

    // transforma o source code em um ArrayList de string de 16 bits
    // ex: ["000000000000010", "000000000000100"]
    public ArrayList<String> parseStringToWords(String data){
        String[] raw = data.split("(?<=\\G.{" + 16 + "})");
        ArrayList<String> words = new ArrayList<String>();
        for(String word: raw){
            words.add(word);
        }

        validateSourceCode(words);

        return words;
    }


    private void validateSourceCode(ArrayList<String> words) {
        Set<String> sizeOneOperation = Set.of("1001","1011");
        Set<String> sizeTwoOperation = Set.of("0010","0000","0101","0001","0100","1111","1010","0011","1110","1100","0111","0110","1000");
        Set<String> sizeThreeOperation = Set.of("1101");
        final int START_INDEX = 0;
        final int OP_CODE_START_INDEX = 12;


        for (int index = 0; index < words.size();) {

            // Verify word size
            if (words.get(index).length() != WORD_SIZE) throw new IllegalArgumentException("Source Code: Wrong OPCODE size. Index: " + index );

            // Verify beginning of OPCODE
            if (!words.get(index).substring(START_INDEX,OP_CODE_START_INDEX).matches("^[0]{12}")) throw new IllegalArgumentException("Source Code: Wrong OPCODE format(beginning). Index: " + index);

            // Manage different sized OPCODES
            if (sizeOneOperation.contains(words.get(index).substring(OP_CODE_START_INDEX))) {
                index++;
            } else if (sizeTwoOperation.contains(words.get(index).substring(OP_CODE_START_INDEX))) {
                validateSizeTwoOperation(words.get(index+1), index);
                index += 2;
            } else if (sizeThreeOperation.contains(words.get(index).substring(OP_CODE_START_INDEX))) {
                validateSizeThreeOperation(words.get(index+1), words.get(index+2), index);
                index += 3;
            } else {
                throw new IllegalArgumentException("Source Code: Wrong OPCODE format(end). Index: " + index );
            }
        }
    }

    private void validateSizeTwoOperation(String word, int index) {
        // 0's and 1's
        if(!word.matches("^[01]+$")) throw new IllegalArgumentException("Source Code: Not 0's and 1's. Index: " + index);

        // size
        if(word.length() != WORD_SIZE) throw new IllegalArgumentException("Source Code: Wrong word size. Index: " + index);

    }

    private void validateSizeThreeOperation(String wordOne, String wordTwo, int index) {
        // 0's and 1's
        if(!wordOne.matches("^[01]+$")) throw new IllegalArgumentException("Source Code: Not 0's and 1's. Index: " + index);
        if(!wordTwo.matches("^[01]+$")) throw new IllegalArgumentException("Source Code: Not 0's and 1's. Index: " + index);
        // size
        if(wordOne.length() != WORD_SIZE) throw new IllegalArgumentException("Source Code: Wrong word size. Index: " + index);
        if(wordTwo.length() != WORD_SIZE) throw new IllegalArgumentException("Source Code: Wrong word size. Index: " + index);
    }
}



