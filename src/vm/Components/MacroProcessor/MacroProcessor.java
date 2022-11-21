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
    String filename;

    public MacroProcessor(String fileName) {
        this.filename = fileName;
    }

    public void run() throws FileNotFoundException, IOException {
        reader = new Reader(this.filename);
        storeMacros();
        replaceMacros();
        writeOutputFile("MASMAPRG.ASM");
    }

    private void storeMacros() {
        String line = reader.readLine();

        while (line != null) {
            if (line.equals("MCDEFN")) {
                line = reader.readLine();
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

    public void writeOutputFile(String fileName) throws IOException {
        Writer writer = new Writer(fileName);
        for (String line : originalData) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }
}
