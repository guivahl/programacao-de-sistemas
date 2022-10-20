package vm.Components.Assembler;

public class InstructionWrapper {
    private String binary;
    private int instructionSize;
    public InstructionWrapper (String binary, int instructionSize) {
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
