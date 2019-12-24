/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Soufiane.sel
 */
public class PrepareData {

    public PrepareData(String filename1, String filename2) throws IOException {
        List<String> messages_file1 = new ArrayList<>();
        List<String> messages_file2 = new ArrayList<>();
        messages_file1 = this.CsvToArray(filename1, messages_file1);
        messages_file2 = this.CsvToArray(filename2, messages_file2);
        messages_file1.addAll(messages_file2);
        this.exportToCsv("src/result/data_to_train.csv", messages_file1, "");
    }

    public PrepareData(String filename1) throws IOException {
        List<String> messages_file1 = new ArrayList<>();
        messages_file1 = this.CsvToArray(filename1, messages_file1);
        this.exportToCsv("src/result/data_to_test.csv", messages_file1, "test");
    }
    

    public List<String> CsvToArray(String filename, List<String> messages) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                messages.add(line);
            }
        }
        return messages;
    }
    
    public void exportToCsv(String filename, List<String> messages, String type) throws IOException {
        File file = new File(filename);
        Files.deleteIfExists(file.toPath());
        int count = 0;
        try {
            FileWriter fstream = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(fstream);
            for (String temp : messages) {
                count++;
                String str1 = Integer.toString(count);
                if("test".equals(type)){
                    out.write(str1+"\t"+temp+"\t"+"objective");
                }else {
                    out.write(str1+"\t"+temp);
                }
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
