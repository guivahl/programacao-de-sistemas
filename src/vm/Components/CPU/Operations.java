package vm.Components.CPU;

import vm.Components.InputStream;
import vm.Components.Memory;
import vm.Components.Stack;
import vm.Components.Logger.Logger;
import vm.Components.Register;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

public class Operations {
    private Register programCounter;
    private Register accumulator;
    private Register operationMode;
    private Register instructionRegister;
    private Register memoryAddressRegister;
    private Memory memory;
    private Stack stack;
    private Map<String, Runnable> operationsMap;
    private boolean stopCondition;
    private Scanner in;
    private Logger logger;
    private int mode;
    private InputStream userInputStream;
    private int initialAddress;

    private int memoryIncr = 10;

    public Operations(Memory memory, Stack stack, Register programCounter,
                        Register accumulator, Register operationMode, Register instructionRegister,
                        Register memoryAddressRegister, Logger logger, InputStream input, int initialAddress)
    {
        this.memory = memory;
        this.stack = stack;
        this.programCounter = programCounter;
        this.accumulator = accumulator;
        this.operationMode = operationMode;
        this.instructionRegister = instructionRegister;
        this.memoryAddressRegister = memoryAddressRegister;
        this.stopCondition = true;
        this.operationsMap = new HashMap<>();
        this.logger = logger;
        this.userInputStream = input;
        this.initialAddress = initialAddress;
        setOperationData();
    }

    public void execute(){ 
        in = new Scanner(System.in);
        this.programCounter.setValue(parseIntToBinarySixteenBits(this.initialAddress));
        this.stopCondition = true;
        while (stopCondition){
            System.out.println("Instrução = " + memory.getValue(Integer.parseInt(this.programCounter.getValue(), 2)));
            if (Integer.parseInt(this.operationMode.getValue(), 2) == 1){
                in.nextLine();
            }
            operationsMap.get(memory.getValue(Integer.parseInt(this.programCounter.getValue(), 2))).run();
        }
        in.close();
    }

    private Operations addInstruction(int mode){
         this.instructionRegister.setValue("0000000000000010");
         int index = Integer.parseInt(this.programCounter.getValue(), 2);
         int opd1;
         index++;
         this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2)), 2);
        } else if (mode == 3) {
            opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2);
        } else {
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2) + this.memoryIncr), 2);
        }
        this.accumulator.setValue(parseIntToBinarySixteenBits(Integer.parseInt(this.accumulator.getValue(), 2) + opd1));
        index++;
        this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        return null;
    }

    private Operations brInstruction(int mode){
        this.instructionRegister.setValue("0000000000000000");
        int index = Integer.parseInt(this.programCounter.getValue(), 2);
        int opd1;
        index++;
        this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
        } else {
            opd1 = Integer.parseInt(memory.getValue(index), 2) + this.memoryIncr;
        }
        this.programCounter.setValue(parseIntToBinarySixteenBits(opd1));
        return null;
    }

    private Operations brnegInstruction(int mode){
        this.instructionRegister.setValue("0000000000000101");
        int index = Integer.parseInt(this.programCounter.getValue(), 2);
        int opd1;
        if (Integer.parseInt(this.accumulator.getValue(), 2) < 0){
            index++;
            this.memoryAddressRegister.setValue(memory.getValue(index));
            if (mode == 2){
                opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
            } else {
                opd1 = Integer.parseInt(memory.getValue(index), 2) + this.memoryIncr;
            }
            this.programCounter.setValue(parseIntToBinarySixteenBits(opd1));
        } else {
            index = index + 2;
            this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        }
        return null;
    }

    private Operations brposInstruction(int mode){
        this.instructionRegister.setValue("0000000000000100");
        int index = Integer.parseInt(this.programCounter.getValue(), 2);
        int opd1;
        if (Integer.parseInt(this.accumulator.getValue(), 2) > 0){
            index++;
            this.memoryAddressRegister.setValue(memory.getValue(index));
            if (mode == 2){
                opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
            } else {
                opd1 = Integer.parseInt(memory.getValue(index), 2) + this.memoryIncr;
            }
            this.programCounter.setValue(parseIntToBinarySixteenBits(opd1));
        } else {
            index = index + 2;
            this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        }
        return null;
    }

    private Operations brzeroInstruction(int mode){
        this.instructionRegister.setValue("0000000000000001");
        int index = Integer.parseInt(this.programCounter.getValue(), 2);
        int opd1;
        if (Integer.parseInt(this.accumulator.getValue(), 2) == 0){
            index++;
            this.memoryAddressRegister.setValue(memory.getValue(index));
            if (mode == 2){
                opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
            } else {
                opd1 = Integer.parseInt(memory.getValue(index), 2);
            }
            this.programCounter.setValue(parseIntToBinarySixteenBits(opd1));
        } else {
            index = index + 2;
            this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        }
        return null;
    }
    
    private Operations callInstruction(int mode) throws Exception{
        this.instructionRegister.setValue("0000000000001111");
        int index = Integer.parseInt(this.programCounter.getValue(), 2);
        this.stack.push(this.programCounter.getValue());
        int opd1;
        index++;
        this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
        } else {
            opd1 = Integer.parseInt(memory.getValue(index), 2);
        }
        this.programCounter.setValue(parseIntToBinarySixteenBits(opd1));
        return null;
    }

    private Operations copyInstruction(int mode1, int mode2){
        this.instructionRegister.setValue("0000000000001101");
        int index = Integer.parseInt(this.programCounter.getValue(), 2);
        int opd1, opd2;
        index++;
        this.memoryAddressRegister.setValue(memory.getValue(index));

        if (mode1 == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2)), 2);
        } else if (mode1 == 3) {
            opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2);
        } else {
            opd1 = Integer.parseInt(this.memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue() ,2) + this.memoryIncr), 2);
        }
        index++;
        this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode2 == 2){
            opd2 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
        } else {
            opd2 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2) + this.memoryIncr;
        }

        memory.setValue(opd2, parseIntToBinarySixteenBits(opd1));
        index++;
        this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        return null;
    }

    private Operations divideInstruction(int mode){
        this.instructionRegister.setValue("0000000000001010");
         int index = Integer.parseInt(this.programCounter.getValue(), 2);
         int opd1;
         index++;
         this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2)), 2);
        } else if (mode == 3) {
            opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2);
        } else {
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
        }
        this.accumulator.setValue(parseIntToBinarySixteenBits((int)(Integer.parseInt(this.accumulator.getValue(), 2) / opd1)));
        index++;
        this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        return null;
    }

    private Operations loadInstruction(int mode){
        this.instructionRegister.setValue("0000000000000011");
         int index = Integer.parseInt(this.programCounter.getValue(), 2);
         int opd1;
         index++;
         this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2)), 2);
        } else if (mode == 3) {
            opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2);
        } else {
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2) + this.memoryIncr), 2);
        }
        this.accumulator.setValue(parseIntToBinarySixteenBits(opd1));
        index++;
        this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        return null;
    }
    
    private Operations multInstruction(int mode){
        this.instructionRegister.setValue("0000000000001110");
         int index = Integer.parseInt(this.programCounter.getValue(), 2);
         int opd1;
         index++;
         this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2)), 2);
        } else if (mode == 3) {
            opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2);
        } else {
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
        }
        this.accumulator.setValue(parseIntToBinarySixteenBits(Integer.parseInt(this.accumulator.getValue(), 2) * opd1));
        index++;
        this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        return null;
    }

    private Operations readInstruction(int mode){
        logger.logMessage("Inform the input stream value: ", Logger.ATTENTION_MESSAGE);

        if(this.userInputStream.value.length() == 0){
            this.mode = mode;
            this.stopCondition = false;
            return null;
        }

        return continueReadInstruction();
    }

    public Operations continueReadInstruction(){
        int inputStream = Integer.parseInt(this.userInputStream.value);

        this.instructionRegister.setValue("0000000000001100");
        int index = Integer.parseInt(this.programCounter.getValue(), 2);
        int opd1;
        index++;
        this.memoryAddressRegister.setValue(memory.getValue(index));

        if (mode == 2){
           opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
       } else {
           opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2) + this.memoryIncr;
       }

       memory.setValue(opd1, parseIntToBinarySixteenBits(inputStream));
       index++;
       this.programCounter.setValue(parseIntToBinarySixteenBits(index));

        return null;
    }

    private Operations retInstruction() throws Exception{
        this.instructionRegister.setValue("0000000000001001");
        this.programCounter.setValue(this.stack.pop());
        
        return null;
    }

    private Operations stopInstruction(){
        this.stopCondition = false;
        return null;
    }

    private Operations storeInstruction(int mode){
        this.instructionRegister.setValue("0000000000000111");
         int index = Integer.parseInt(this.programCounter.getValue(), 2);
         int opd1;
         index++;
         this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2);
        } else {
            opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2) + this.memoryIncr;
        }
        this.memory.setValue(opd1, this.accumulator.getValue());
        index++;
        this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        return null;
    }

    private Operations subInstruction(int mode){
        this.instructionRegister.setValue("0000000000000110");
         int index = Integer.parseInt(this.programCounter.getValue(), 2);
         int opd1;
         index++;
         this.memoryAddressRegister.setValue(memory.getValue(index));
        if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2)), 2);
        } else if (mode == 3) {
            opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2);
        } else {
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2) + this.memoryIncr), 2);
        }
        this.accumulator.setValue(parseIntToBinarySixteenBits(Integer.parseInt(this.accumulator.getValue(), 2) - opd1));
        index++;
        this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        return null;
    }

    private Operations writeInstruction(int mode){
        if (Integer.parseInt(this.operationMode.getValue(), 2) == 2){
            in.nextLine();
        }
        this.instructionRegister.setValue("0000000000000110");
         int index = Integer.parseInt(this.programCounter.getValue(), 2);
         int opd1;
         index++;
         this.memoryAddressRegister.setValue(memory.getValue(index));
         if (mode == 2){
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2)), 2)), 2);
        } else if (mode == 3) {
            opd1 = Integer.parseInt(this.memoryAddressRegister.getValue(), 2);
        } else {
            opd1 = Integer.parseInt(memory.getValue(Integer.parseInt(this.memoryAddressRegister.getValue(), 2) + this.memoryIncr), 2);
        }
        logger.logMessage("Output stream " + opd1, Logger.SUCCESS_MESSAGE);
        index++;
        this.programCounter.setValue(parseIntToBinarySixteenBits(index));
        return null;
    }

    /**
     * The method setOperationtionData fill a HashMap with functions call.
     * 
     * Each key is hitched to a function call from this same class, this is
     * made with the Runnable class that allows that kind of operation.
     */

    private void setOperationData(){
        operationsMap.put("0000000000000010", () -> addInstruction(1));
        operationsMap.put("0000000000010010", () -> addInstruction(2));
        operationsMap.put("0000000001000010", () -> addInstruction(3));
        operationsMap.put("0000000000000000", () -> brInstruction(1));
        operationsMap.put("0000000000010000", () -> brInstruction(2));
        operationsMap.put("0000000000000101", () -> brnegInstruction(1));
        operationsMap.put("0000000000010101", () -> brnegInstruction(2));
        operationsMap.put("0000000000000001", () -> brposInstruction(1));
        operationsMap.put("0000000000010001", () -> brposInstruction(2));
        operationsMap.put("0000000000000100", () -> brzeroInstruction(1));
        operationsMap.put("0000000000010100", () -> brzeroInstruction(2));
        operationsMap.put("0000000000001111", () -> {
            try {
                callInstruction(1);//Tentei muito e não consegui lançar a exceção de outra forma, vou esperar a ajuda de vocês no PR
            } catch (Exception e) {
                e.printStackTrace();
                this.logger.logMessage("Stack overflow, while operating", Logger.ERROR_MESSAGE);
            }
        });
        operationsMap.put("0000000000011111", () -> {
            try {
                callInstruction(2);
            } catch (Exception e) {
                e.printStackTrace();
                this.logger.logMessage("Stack overflow, while operating", Logger.ERROR_MESSAGE);
            }
        });
        operationsMap.put("0000000000001101", () -> copyInstruction(1, 1));
        operationsMap.put("0000000000101101", () -> copyInstruction(2, 1));
        operationsMap.put("0000000001001101", () -> copyInstruction(3, 1));
        operationsMap.put("0000000000011101", () -> copyInstruction(1, 2));
        operationsMap.put("0000000000111101", () -> copyInstruction(2, 2));
        operationsMap.put("0000000001011101", () -> copyInstruction(3, 2));
        operationsMap.put("0000000000001010", () -> divideInstruction(1));
        operationsMap.put("0000000000011010", () -> divideInstruction(2));
        operationsMap.put("0000000001001010", () -> divideInstruction(3));
        operationsMap.put("0000000000000011", () -> loadInstruction(1));
        operationsMap.put("0000000000010011", () -> loadInstruction(2));
        operationsMap.put("0000000001000011", () -> loadInstruction(3));
        operationsMap.put("0000000000001110", () -> multInstruction(1));
        operationsMap.put("0000000000011110", () -> multInstruction(2));
        operationsMap.put("0000000001001110", () -> multInstruction(3));
        operationsMap.put("0000000000001100", () -> readInstruction(1));
        operationsMap.put("0000000000011100", () -> readInstruction(2));
        operationsMap.put("0000000000001001", () -> {
            try {
                retInstruction();
            } catch (Exception e) {
                e.printStackTrace();
                this.logger.logMessage("Empty stack", Logger.ERROR_MESSAGE);
            }
        });
        operationsMap.put("0000000000001011", () -> stopInstruction());
        operationsMap.put("0000000000000111", () -> storeInstruction(1));
        operationsMap.put("0000000000010111", () -> storeInstruction(2));
        operationsMap.put("0000000000000110", () -> subInstruction(1));
        operationsMap.put("0000000000010110", () -> subInstruction(2));
        operationsMap.put("0000000001000110", () -> subInstruction(3));
        operationsMap.put("0000000000001000", () -> writeInstruction(1));
        operationsMap.put("0000000000011000", () -> writeInstruction(2));
        operationsMap.put("0000000001001000", () -> writeInstruction(3));
    }

    /*
     * Return a String with sixteen bits with the binary number of the equivalent
     * integer in base 10 passed as argument.
     * PERSONAL NOTE: Integer.parseInt(String, 2) does the opposite. (Sorry if i forgotten to erase this).
     */

    private String parseIntToBinarySixteenBits(int transform) {
        String binary;
        String fullStr;
        String zeroes = "";
        String signal;

        if (transform < 0){
            signal = "-";
            transform *= (-1);
        } else {
            signal = "0";
        }

        binary = Integer.toBinaryString(transform);
    
        for (int i = 0; i < 15 - binary.length(); i++) {
            zeroes += "0";
        }
    
        fullStr = signal + zeroes + binary;
        return fullStr;
    }
}