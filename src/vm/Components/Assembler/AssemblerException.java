package vm.Components.Assembler;

import vm.Components.Logger.Logger;

public class AssemblerException extends Exception {
  public AssemblerException(String e, Logger logger) {
    super(e);
    logger.logMessage(e, Logger.ERROR_MESSAGE);
  }
}
