package vm.Components.FileHandlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Writer {

  private String filename;
  private Path path;

  public Writer(String filename) {
    Path path = Path.of("src", "resources", filename);
    this.path = path;
    this.filename = filename;
  }

  public void write(String arg) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toAbsolutePath().toString(), true))) { 
      bufferedWriter.write(arg);
    } catch(IOException e) {
        e.printStackTrace();
    }
  }
    
}
