package vm.Components.Assembler;

public class AssemblerException extends Exception {
  public AssemblerException(String e) {
    super(e);
    System.out.println(e);
  }
}
