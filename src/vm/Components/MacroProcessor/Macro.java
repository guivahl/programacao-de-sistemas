package vm.Components.MacroProcessor;

import java.util.ArrayList;

public class Macro {
    private String name;
    private ArrayList<String> parameters;
    private ArrayList<String> instructions;
    private ArrayList<Macro> nestedMacros = new ArrayList<>();

    public Macro(String name, ArrayList<String> parameters, ArrayList<String> instructions) {
        this.name = name;
        this.parameters = parameters;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public ArrayList<Macro> getNestedMacros() {
        return nestedMacros;
    }

    public void addNestedMacro(Macro nestedMacro) {
        nestedMacros.add(nestedMacro);
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    // Create a copy of the instructions of the macro and replace the parameters with the values
    // passed in the call
    // The parameters normally are letters
    // The instructions where the parameters are replaced are words
    // Example:
    // ADD A
    // In the above example, the "A" in "ADD" cannot be replaced with the value passed in the call
    // Return the copy of the instructions
    // Split the string of instructions by a space
    // For each word in the instructions, check if it is a parameter
    // If it is a parameter, replace it with the value passed in the call

    public ArrayList<String> replaceParameters(ArrayList<String> values) {
        ArrayList<String> newInstructions = new ArrayList<>();
        for (String instruction : instructions) {
            String[] words = instruction.split(" ");
            for (int i = 0; i < words.length; i++) {
                for (int j = 0; j < parameters.size(); j++) {
                    if (words[i].equals(parameters.get(j))) {
                        words[i] = values.get(j);
                    }
                }
            }
            String newInstruction = "";
            newInstruction += words[0];
            for (int i = 1; i < words.length; i++) {
                newInstruction += " " + words[i];
            }
            newInstructions.add(newInstruction);
        }
        return newInstructions;
    }

    public ArrayList<Macro> expandNestedMacros(ArrayList<String> values) {
        // Do the same thing as the replaceParameters method but for nested macros
        // Paramters in the outer macro will be replaced also in the nested macro if the respectives
        // paramters are present in the instructions of the nested macro

        ArrayList<Macro> macros = new ArrayList<>();
        for (Macro nestedMacro : nestedMacros) {
            ArrayList<String> newInstructions = new ArrayList<>();
            for (String instruction : nestedMacro.getInstructions()) {
                String[] words = instruction.split(" ");
                for (int i = 0; i < words.length; i++) {
                    for (int j = 0; j < parameters.size(); j++) {
                        if (words[i].equals(parameters.get(j))) {
                            words[i] = values.get(j);
                        }
                    }
                }
                String newInstruction = "";
                newInstruction += words[0];
                for (int i = 1; i < words.length; i++) {
                    newInstruction += " " + words[i];
                }
                newInstructions.add(newInstruction);
            }
            nestedMacro.setInstructions(newInstructions);
            macros.add(nestedMacro);
        }
        return macros;
    }


}
