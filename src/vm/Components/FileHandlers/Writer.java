package vm.Components.FileHandlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {

  public void write(String arg) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./src/resources/source-code.txt", true))) { 
      bufferedWriter.write(arg);
    } catch(IOException e) {
        e.printStackTrace();
    }
  }
    
}
