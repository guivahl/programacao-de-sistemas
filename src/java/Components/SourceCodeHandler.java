package Components;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class SourceCodeHandler {
    public String readFile(String filename) throws FileNotFoundException{
        String data = "";
        Path path = Path.of("src","resources", filename);
        try {
            File file = new File(path.toString());
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
        return words;
    }
}
