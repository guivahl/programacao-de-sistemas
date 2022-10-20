package vm.Components.FileHandlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Reader {

    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private String filename;
    private Path path;

    public Reader(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.path = Path.of("src", "resources", filename);
        fileReader = new FileReader(path.toAbsolutePath().toString());
        bufferedReader = new BufferedReader(fileReader);
    }

    public String readLine() {
        String data = "";
        try {
            data = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String readFile() throws FileNotFoundException {
        String data = "";
        try {
            File file = new File(path.toAbsolutePath().toString());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data += scanner.nextLine();
            }
            scanner.close();
          } catch (FileNotFoundException error) {
            throw new FileNotFoundException("Invalid pathname, file not found.");
        }
        return data;
    }
    
}