package com.roche.connect.htp.adapter.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class JsonFileReaderAsString {

    static String line = null;
    static String ls = System.getProperty("line.separator");

    public static String getJsonfromFile(String fileName) throws IOException {
        BufferedReader readCorrect = new BufferedReader(new FileReader(new File(fileName)));
        StringBuilder stringBuilderCorrect = new StringBuilder();
        while ((line = readCorrect.readLine()) != null) {
            stringBuilderCorrect.append(line);
            stringBuilderCorrect.append(ls);
        }
        stringBuilderCorrect.deleteCharAt(stringBuilderCorrect.length() - 1);
        readCorrect.close();

        return stringBuilderCorrect.toString();

    }

}