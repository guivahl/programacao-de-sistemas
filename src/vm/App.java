package vm;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import vm.Components.Memory;
import vm.Components.Register;
import vm.Components.SourceCodeHandler;
import vm.Components.Stack;
import vm.Components.Assembler.Assembler;
import vm.Components.*;
import vm.Components.Logger.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App implements ActionListener {
    private Logger logger;
    private Memory memory;
    private Stack stack;
    private Register programCounter;
    private Register stackPointer;
    private Register accumulator;
    private Register operationMode;
    private Register instructionRegister;
    private Register memoryAddressRegister;
    private GUI gui;
    private Assembler assembler;

    public App(){
        this.logger = new Logger();
        this.memory = new Memory(logger);
        this.stack = new Stack(memory, 10);

        ArrayList<Register> registers = new ArrayList<Register>();
        this.programCounter = initializeRegister("PC", 16, registers);
        this.stackPointer = initializeRegister("SP", 16, registers);
        this.accumulator = initializeRegister("ACC", 16, registers);
        this.operationMode = initializeRegister("MOP", 8, registers);
        this.instructionRegister = initializeRegister("RI", 16, registers);
        this.memoryAddressRegister = initializeRegister("RE", 16, registers);

        gui = new GUI(stack, registers, memory, logger);
        logger.logMessage("machine components initialized successfully", Logger.SUCCESS_MESSAGE);

        gui.runButton.addActionListener(this);
    }

    public static void main(String[] args) throws Exception {
        new App();
    }

    private static Register initializeRegister(String identifier, int size, ArrayList<Register> registerArray){
        Register newRegister = new Register(identifier, size);
        registerArray.add(newRegister);

        return newRegister;
    }

    private void run() {
        this.gui.cleanTerminalPanel();
        this.logger.logMessage("starting running", Logger.SUCCESS_MESSAGE);

        this.assembler = new Assembler();

        // Lê o source code e inicializa a memória com o programa
        SourceCodeHandler reader = new SourceCodeHandler(logger);
        String data;
        try {
            data = reader.readFile("source-code.txt");
            ArrayList<String> words = reader.parseStringToWords(data);
            for(String word : words) {
                memory.pushValue(word);
            }
        } catch (FileNotFoundException e) {
            logger.logMessage("Source code not found", Logger.ERROR_MESSAGE);
        }

        // TODO: chamar a classe Operations para executar código
    }

    // listener pro botão de rodar programa
    public void actionPerformed(ActionEvent e) {
        this.run();
    }
}