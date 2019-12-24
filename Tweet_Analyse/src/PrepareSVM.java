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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author Soufiane.sel
 */
public class PrepareSVM {
    
    private String filename_Train;
    private String filename_Test;
    private List<String> data_train;
    private List<String> data_test;
    private Map<String, Integer> wordDictionary ;
    
    public PrepareSVM(String filename_Train,String filename_Test) throws IOException {
        this.filename_Train = filename_Train;
        this.filename_Test = filename_Test;
        this.data_train = new ArrayList<>();
        this.data_test = new ArrayList<>();
        this.wordDictionary = new HashMap<>();
        this.SetSVM(filename_Train, data_train);
        this.SetSVM(filename_Test, data_test);
        this.ExportSVM("src/result/data_train.svm", data_train);
        this.ExportSVM("src/result/data_test.svm", data_test);
    }
    
    public void SetSVM (String filename, List<String> data) throws FileNotFoundException, IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int index = 1, indexWord;
            while ((line = br.readLine()) != null) {
                String WordsLine = "";
                String polarity = "";
                String[] values = line.split("\\t");
                if("negative".equals(values[2])){
                    polarity = "1";
                }else if ("positive".equals(values[2])){
                    polarity = "2";
                }else if ("mixte".equals(values[2])){
                    polarity = "3";
                }else{
                    polarity = "0";
                }
                Map<Integer, String> wordLine = new HashMap<>();
                String[] words = values[1].split(" ");
                for(int i =0; i<words.length; i++){
                    int occurance = 0;
                    if(this.wordDictionary.containsKey(words[i])){
                        indexWord = this.wordDictionary.get(words[i]);
                    }else{
                        this.wordDictionary.put(words[i], index);
                        indexWord = index;
                        index++;
                    }
                    for(int j =0; j<words.length; j++){
                        if(words[i].equals(words[j])){
                            occurance++;
                        }
                    }
                    WordsLine = indexWord+":"+occurance;
                    wordLine.put(indexWord, WordsLine);
                }
                String s ="";
                Map<Integer, String> treeMap = new TreeMap<Integer, String>(wordLine);
                for (Entry<Integer, String> entry : treeMap.entrySet()) {
                    s = s+" "+entry.getValue();
                }
                data.add(polarity+s);
            }
        }
    }
    
    public void ExportSVM (String filename, List<String> data) throws IOException {
        File file = new File(filename);
        Files.deleteIfExists(file.toPath());
        try {
            FileWriter fstream = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(fstream);
            for (String temp : data) {
                out.write(temp);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
