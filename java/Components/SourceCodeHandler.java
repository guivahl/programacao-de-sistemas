package Components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SourceCodeHandler {
    public String readFile(String pathname){
        String data = "";
        try {
            File file = new File("resources/" + pathname);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }

    // transforma o source code em um array de string de 16 bits
    // ex: ["000000000000010", "000000000000100"]
    public ArrayList<String> parseStringToWords(String data){
        String[] bits = data.split("");
        int count = 0;
        ArrayList<String> words = new ArrayList<String>();
        String currentWord = "";
        for(String bit: bits){
            if(count < 15){
                currentWord += bit;
                count++;
            } else {
                count = 0;
                words.add(currentWord);
                currentWord = "";
            }
        }
        return words;
    }
}
