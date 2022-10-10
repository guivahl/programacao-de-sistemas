package vm.Components.FileHandlers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private String file;
    private FileReader fileReader;
    BufferedReader bufferedReader;

    public Reader() throws FileNotFoundException {
        file =  "src/resources/MASMAPRG.ASM";
        fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
    }

    public String readLine() {
        String line;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return line;
    }
}