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
public class Predect {
    
    private String filename_test;
    private String filename_predect;
    private List<String> tweets;
    private List<String> result;
    private List<String> data_final;

    public Predect(String filename_test, String filename_predect) throws IOException {
        this.filename_test = filename_test;
        this.filename_predect = filename_predect;
        this.tweets = new ArrayList<>();
        this.result = new ArrayList<>();
        this.data_final = new ArrayList<>();
        this.CsvTestToObject(filename_test, tweets);
        this.CsvTestToObject(filename_predect, result);
        this.SetPolarityForTestTweet();
        this.exportToCsv("src/result/SELLAMI_TALIBI.txt");
    }
    
    public void CsvTestToObject(String filename, List<String> data) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        }
        
    }
    public void SetPolarityForTestTweet() {
        for (int i = 0; i < this.tweets.size(); i++) {
            String[] values = this.tweets.get(i).split(" ");
            String idTweet = values[0];
            String resultTweet = "";
            if("0".equals(this.result.get(i))){
                resultTweet = "autre";
            }else if ("1".equals(this.result.get(i))){
                resultTweet = "negatif";
            }else if ("2".equals(this.result.get(i))){
                resultTweet = "positif";
            }else if ("3".equals(this.result.get(i))){
                resultTweet = "mixte";
            }
            this.data_final.add(idTweet+" "+resultTweet);
        }
    }
    
    public void exportToCsv(String filename) throws IOException {
        File file = new File(filename);
        Files.deleteIfExists(file.toPath());
        try {
            FileWriter fstream = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(fstream);
            for (String temp : this.data_final) {
                out.write(temp);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
