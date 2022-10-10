package vm.Components.Assembler;

public class InstructionsWrapper {
    private String binary;
    private int instructionSize;
    public InstructionsWrapper(String binary, int instructionSize) {
        this.binary = binary;
        this.instructionSize = instructionSize;
    }

    public String getBinary() {
        return this.binary;
    }

    public int getInstructionSize() {
        return this.instructionSize;
    }
}
