package vm.Components.Linker;
import vm.Components.Logger.Logger;

public class LinkerException extends Exception {
    public LinkerException(String e, Logger logger) {
        super(e);
        logger.logMessage(e, Logger.ERROR_MESSAGE);
    }
}

