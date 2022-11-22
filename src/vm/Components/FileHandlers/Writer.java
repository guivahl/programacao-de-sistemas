package vm.Components.FileHandlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Writer {

  private FileWriter fileWriter;
  private BufferedWriter bufferedWriter;
  private Path path;

  public Writer(String filename) throws IOException {
    path = Path.of("src", "resources", filename);
    fileWriter = new FileWriter(path.toAbsolutePath().toString(), false);
    bufferedWriter = new BufferedWriter(fileWriter);
  }

  public void write(String arg) {
    try {
      bufferedWriter.write(arg);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void newLine() {
    try {
      bufferedWriter.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
