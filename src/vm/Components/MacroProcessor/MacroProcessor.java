package vm.Components.MacroProcessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import vm.Components.FileHandlers.Reader;
import vm.Components.FileHandlers.Writer;

public class MacroProcessor {
    ArrayList<Macro> macros = new ArrayList<>();
    ArrayList<String> originalData = new ArrayList<>();
    Reader reader;

    // MacroProcessor is a class to process macros
    // MacroProcessor reads a .ASM file and stores the instructions in a list of objects of type
    // Macro
    // MacroProcessor is responsible for replacing macros with their respective instructions
    // Macros in the .ASM file starts with a "MCDEFN" and ends with a "MCEND"
    // Macros are defined in the .ASM file as follows:
    // MCDEFN
    // <macro_name> <macro_parameters>
    // <macro_instructions>
    // MCEND
    // Macros can be defined in a nested way
    // As an example:
    // MCDEFN
    // <macro1_name> <macro1_parameters>
    // MCDEFN
    // <macro2_name> <macro2_parameters>
    // <macro2_instructions>
    // MCEND
    // <macro1_instructions>
    // MCEND
    // Macros can be called in a nested way
    // Macros can be called with parameters
    // All macros must be stored in a list of objects of type Macro
    // All macros must be replaced with their respective instructions replacing the parameters with
    // the values passed in the call
    // Macros will be defined before called
    // Macros can be called multiple times
    // MacroProcessor create a output .ASM file with the macros replaced with their respective
    // instructions

    public MacroProcessor(String fileName) throws FileNotFoundException, IOException {
        reader = new Reader(fileName);
        // Read line by line the .ASM file, detect macros, and store their instructions in macros
        // If a macro is found, read the next line to get the macro name and parameters
        // Read the next lines ans store then as instructions until a "MCEND" is found
        // If a "MCEND" is found, store the macro in the list of macros
        // If a "MCDEFN" is found, repeat the described process and store the macro as a nested
        // macro in the outer macro
        // If a "MCEND" is found after a second "MCDEFN", return to the outer macro
        storeMacros();
        replaceMacros();
        writeOutputFile("MASMAPRG.ASM");
    }

    private void storeMacros() {
        // Method to do the described process in the constructor
        String line = reader.readLine();

        while (line != null) {
            if (line.equals("MCDEFN")) {
                line = reader.readLine();
                // Split the line in " " and "," to get the macro name and parameters
                String[] macroNameAndParameters = line.split(" |,");
                String macroName = macroNameAndParameters[0];
                ArrayList<String> macroParameters = new ArrayList<>();
                for (int i = 1; i < macroNameAndParameters.length; i++) {
                    macroParameters.add(macroNameAndParameters[i]);
                }
                Macro macro = new Macro(macroName, macroParameters, new ArrayList<>());
                macros.add(macro);
                ArrayList<String> macroInstructions = new ArrayList<>();
                line = reader.readLine();
                while (!line.equals("MCEND")) {
                    if (line.equals("MCDEFN")) {
                        line = reader.readLine();
                        String[] nestedMacroNameAndParameters = line.split(" |,");
                        String nestedMacroName = nestedMacroNameAndParameters[0];
                        ArrayList<String> nestedMacroParameters = new ArrayList<>();
                        for (int i = 1; i < nestedMacroNameAndParameters.length; i++) {
                            nestedMacroParameters.add(nestedMacroNameAndParameters[i]);
                        }
                        ArrayList<String> nestedMacroInstructions = new ArrayList<>();
                        line = reader.readLine();
                        while (!line.equals("MCEND")) {
                            nestedMacroInstructions.add(line);
                            line = reader.readLine();
                        }
                        line = reader.readLine();
                        Macro nestedMacro = new Macro(nestedMacroName, nestedMacroParameters,
                                nestedMacroInstructions);
                        macros.get(macros.size() - 1).addNestedMacro(nestedMacro);
                    } else {
                        macroInstructions.add(line);
                        line = reader.readLine();
                    }
                }
                macros.get(macros.size() - 1).setInstructions(macroInstructions);
                line = reader.readLine();
            } else {
                originalData.add(line);
                line = reader.readLine();
            }
        }
    }

    private void replaceMacros() {
        // Method to rewrite the originalData with the macros replaced with their respective
        // instructions using the Macro's replaceParameters method
        // Nested macros will be moved to the end of the list of macros as soon as the outer macro
        // is called
        // Nested macros will be replaced with their respective instructions using the Macro's
        // replaceParameters method
        // If tried to convert from ArrayList<Macro> to ArrayList<String> java will throw a error
        // The Method replaceParameters only receave a ArrayList<String> as parameter



        for (int i = 0; i < originalData.size(); i++) {
            String line = originalData.get(i);
            String[] lineSplit = line.split(" ");
            int macrosSize = macros.size();
            for (int j = 0; j < macrosSize; j++) {
                if (lineSplit[0].equals(macros.get(j).getName())) {
                    ArrayList<String> macroParameters = new ArrayList<>();
                    for (int k = 1; k < lineSplit.length; k++) {
                        macroParameters.add(lineSplit[k]);
                    }
                    ArrayList<String> macroInstructions =
                            macros.get(j).replaceParameters(macroParameters);
                    // Use the Macro.expandNestedMacros method to add the possible outer macro
                    // parameters in the nested macros
                    // The nested macros will be added to the end of the list of macros so it can be
                    // called in the next "i" iterations
                    macros.addAll(macros.get(j).expandNestedMacros(macroParameters));
                    macrosSize += macros.get(j).getNestedMacros().size();

                    originalData.remove(i);
                    for (int k = 0; k < macroInstructions.size(); k++) {
                        originalData.add(i + k, macroInstructions.get(k));
                    }
                    i--;
                    break;
                }
            }
        }

    }

    // Method to write the originalData in a .ASM file
    // Each line will be written in a new line

    // private void writeOutputFile(String fileName) throws IOException {
    // BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
    // for (int i = 0; i < originalData.size(); i++) {
    // writer.write(originalData.get(i));
    // writer.newLine();
    // }
    // writer.close();
    // }

    public void writeOutputFile(String fileName) throws IOException {
        Writer writer = new Writer(fileName);
        for (String line : originalData) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }
}
