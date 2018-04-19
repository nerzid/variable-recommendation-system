package com.nerzid;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by @author nerzid on 18.04.2018.
 */
public class VariableModel {
    public static HashMap<String, Integer> variablesWithFreqs = new HashMap<>();
    public static HashMap<String, Integer> tokensWithFreqs = new HashMap<>();
    public static int numberOfVariables = 0;
    public static int numberOfTokens = 0;
    public static int numberOfMethods = 0;

    public static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public static void writeContentsToFile(){
        variablesWithFreqs = sortByValues(variablesWithFreqs);
        tokensWithFreqs = sortByValues(tokensWithFreqs);

        File variablesWithFreqsFile = new File("variables_with_freqs.txt");
        String variablesWithFreqsFileContent = "";

        File tokensWithFreqsFile = new File("tokens_with_freqs.txt");
        String tokensWithFreqsFileContent = "";


        for (Map.Entry entry:
             variablesWithFreqs.entrySet()) {
            variablesWithFreqsFileContent += entry.getKey().toString() + ": " + entry.getValue().toString() + "\n";
        }

        for (Map.Entry entry:
                tokensWithFreqs.entrySet()) {
            tokensWithFreqsFileContent += entry.getKey().toString() + ": " + entry.getValue().toString() + "\n";
        }

        try {
            FileUtils.writeStringToFile(variablesWithFreqsFile, variablesWithFreqsFileContent);
            FileUtils.writeStringToFile(tokensWithFreqsFile, tokensWithFreqsFileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
