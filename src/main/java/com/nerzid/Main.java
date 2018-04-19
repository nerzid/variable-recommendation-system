package com.nerzid;

import com.nerzid.processor.MethodProcessor;
import org.apache.commons.io.FileUtils;
import spoon.Launcher;
import spoon.compiler.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.text.similarity.JaccardDistance;
import org.apache.commons.text.similarity.HammingDistance;
import org.apache.commons.text.similarity.FuzzyScore;

import static com.nerzid.VariableModel.*;

/**
 * Created by @author nerzid on 28.03.2018.
 */
public class Main {
    public static List<File> files_list;
    public static HashMap<String,Integer> mergedTokens = new HashMap<>();
    public static int corruptedFiles = 0;

    public static void main(String[] args) {
//        start(true);
        loadMergedTokens();
//        System.out.println(removeVowels("current"));
//        System.out.println(removeVowels("curr"));
//        System.out.println(removeVowels("after"));
        String word = "curr";
        recommendToken(word);
    }

    private static String removeVowels(String word){
        return word.replaceAll("a", "")
                .replaceAll("e", "")
                .replaceAll("i", "")
                .replaceAll("o", "")
                .replaceAll("u", "");
    }

    private static void recommendToken(String word){
        JaccardDistance distance = new JaccardDistance();
        String lowestDistancedKey = "";
        double lowestDistance = Double.MAX_VALUE;
        for (Map.Entry<String,Integer> entry:
                mergedTokens.entrySet()) {
            if (entry.getValue() > 50) {
                double newDistance = distance.apply(removeVowels(word), removeVowels(entry.getKey()));
                if (lowestDistance > newDistance) {
                    lowestDistance = newDistance;
                    lowestDistancedKey = entry.getKey();
                }
            }
        }

        System.out.println("Word: " + word);
        System.out.println("Recommended word: " + lowestDistancedKey);
        System.out.println("Distance: " + lowestDistance);
    }

    private static void loadMergedTokens() {
        File mergedTokensFile = new File("tokens_with_freqs_merged.txt");
        try {
            for (Object line :
                    FileUtils.readLines(mergedTokensFile)) {
                String lineStr = line.toString();
                String token = lineStr.trim().split(":")[0];
                int freq = Integer.parseInt(lineStr.trim().split(":")[1].trim());
                mergedTokens.put(token, freq);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void start(boolean useMultipleFiles) {
        try {
            if (useMultipleFiles) {
                files_list = FilePicker.chooseDirAndGetJavaFiles();
            } else {
//                files_list = Collections.singletonList(FilePicker.chooseFile());
                files_list = new ArrayList<>();
                files_list.add(new File("C:/Users/nerzid/Desktop/variable-test/hello4.java"));
            }
        } catch (FileNotSelected ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (File f : files_list) {
            // Will be deleted in the future
//            f.setWritable(true);
//            System.out.println(f.getPath());
            // Launcher is to use Spoon.
            Launcher l = new Launcher();
            l.addInputResource(f.getPath());

            // Set Environment instance to configure Spoon
            // This is necessary, if you want to see "nice" result
            Environment env = l.getEnvironment();

            // To use uncompilable java files, this needs to be enabled
            env.setNoClasspath(true);

            // This processor handles output file.
            // Without this line, file's contents won't change/updated.
//            JavaOutputProcessor jop = l.createOutputWriter(f.getParentFile(), env);

            // To see comments on result
            env.setCommentEnabled(true);

            // To see short-simple names instead of long ones
            // eg. "System.out.println()" instead of "java.lang.System.out.println()"
            // Also sets imports and deletes all unused imports.
            env.setAutoImports(true);

            // Add Processors to Spoon Launcher
            // WARNING: Priority is important DO NOT CHANGE
            // JavaOutputProcessor must be at LOWERMOST to get all differences
            // and write them
            l.addProcessor(new MethodProcessor());

            // Uncomment this if you want to insert comment into code
//            l.addProcessor(jop);

            // Debuglevel
//            env.setLevel("0");


            try {
                // Run the Launcher
                l.run();
            } catch (Exception e) {
                corruptedFiles++;
            }
//            System.out.println(VariableModel.variableSet);
            System.out.println();
        }
        for (Map.Entry variable :
                VariableModel.variablesWithFreqs.entrySet()) {
            for (String token :
                    Tokenizer.split(variable.getKey().toString())) {
                token = token.toLowerCase();
                if (token.length() > 2) {
                    if (tokensWithFreqs.containsKey(token))
                        tokensWithFreqs.put(token, tokensWithFreqs.get(token) + 1);
                    else
                        tokensWithFreqs.put(token, 1);
                    numberOfTokens++;
                }
            }
        }
        writeContentsToFile();

        System.out.println("# of Methods: " + numberOfMethods);
        System.out.println("# of Variables: " + numberOfVariables);
        System.out.println("# of Tokens: " + numberOfTokens);
        System.out.println(corruptedFiles);
    }

    public static void createMergedTokenFreqsFile(){
        File tokens_with_freqs = new File("tokens_with_freqs.txt");
        File tokens_with_freqs_merged = new File("tokens_with_freqs_merged.txt");
        HashMap<String, Integer> merged_tokens = new HashMap<>();
        try {
            for (Object line :
                    FileUtils.readLines(tokens_with_freqs)) {
                String lineStr = line.toString();
                String token = lineStr.trim().split(":")[0];
                int freq = Integer.parseInt(lineStr.trim().split(":")[1].trim());

                String lineRemovedNumbers = "";
                for (Character ch :
                        token.toCharArray()) {
                    if (Character.isLetter(ch))
                        lineRemovedNumbers += ch;
                }
                if (merged_tokens.containsKey(lineRemovedNumbers))
                    merged_tokens.put(lineRemovedNumbers, merged_tokens.get(lineRemovedNumbers) + freq);
                else
                    merged_tokens.put(lineRemovedNumbers, freq);
            }

            String fileContent = "";
            merged_tokens = sortByValues(merged_tokens);
            for (Map.Entry entry :
                    merged_tokens.entrySet()) {
                fileContent += entry.getKey().toString() + ": " + entry.getValue().toString() + "\n";
            }
            FileUtils.writeStringToFile(tokens_with_freqs_merged, fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
