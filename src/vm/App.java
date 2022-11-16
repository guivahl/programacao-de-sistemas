package vm;

import java.util.ArrayList;

import vm.Components.Memory;
import vm.Components.Register;
import vm.Components.SourceCodeHandler;
import vm.Components.Stack;
import vm.Components.MacroProcessor.MacroProcessor;
import vm.Components.Assembler.*;
import vm.Components.*;
import vm.Components.Logger.*;
import vm.Components.CPU.Operations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class App implements ActionListener {
    private Logger logger;
    private Memory memory;
    private Stack stack;
    private GUI gui;
    private Register programCounter;
    private Register stackPointer;
    private Register accumulator;
    private Register operationMode;
    private Register instructionRegister;
    private Register memoryAddressRegister;
    private Assembler assembler;
    private MacroProcessor macroProcessor;
    private Operations operations;

    public App() {
        this.logger = new Logger();
        this.memory = new Memory(logger);
        this.stack = new Stack(memory, 10);
        this.assembler = new Assembler(this.logger);

        this.macroProcessor = new MacroProcessor("INPUTFILE.ASM");

        ArrayList<Register> registers = new ArrayList<Register>();
        this.programCounter = initializeRegister("PC", 16, registers);
        this.stackPointer = initializeRegister("SP", 16, registers);
        this.accumulator = initializeRegister("ACC", 16, registers);
        this.operationMode = initializeRegister("MOP", 8, registers);
        this.instructionRegister = initializeRegister("RI", 16, registers);
        this.memoryAddressRegister = initializeRegister("RE", 16, registers);

        gui = new GUI(registers, memory, logger);
        logger.logMessage("machine components initialized successfully", Logger.SUCCESS_MESSAGE);

        gui.runBtn.addActionListener(this);
        gui.mountBtn.addActionListener(this);

        //TODO: The interface must ask for the execution mode on the operationMode register.
        this.operations = new Operations(memory, stack, programCounter, stackPointer, accumulator, operationMode, instructionRegister, memoryAddressRegister, logger);
    }

    public static void main(String[] args) throws Exception {
        new App();
    }

    private static Register initializeRegister(String identifier, int size,
            ArrayList<Register> registerArray) {
        Register newRegister = new Register(identifier, size);
        registerArray.add(newRegister);

        return newRegister;
    }


    private void run() {
        this.logger.logMessage("starting execution", Logger.SUCCESS_MESSAGE);

        // Lê o source code e inicializa a memória com o programa
        SourceCodeHandler reader = new SourceCodeHandler(logger);
        String data;
        data = reader.readFile("source-code.txt");
        ArrayList<String> words = reader.parseStringToWords(data);
        if (data.length() > 0) {
            for (String word : words) {
                memory.pushValue(word);
            }
        } else {
            logger.logMessage("source code is empty", Logger.ATTENTION_MESSAGE);
        }

        operations.execute(10);
    }

    private void mount() {
        try {
            this.macroProcessor.run();
        } catch (FileNotFoundException e) {
            logger.logMessage("Arquivo de entrada não encontrado.", Logger.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.assembler.assemble();
    }

    // listener pro botão de rodar programa
    public void actionPerformed(ActionEvent event) {
        if(event.getActionCommand() == "run") this.run();
        if(event.getActionCommand() == "mount") this.mount();

    }
}
