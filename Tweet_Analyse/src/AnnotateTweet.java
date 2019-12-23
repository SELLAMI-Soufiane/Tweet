

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Soufiane.sel
 */
public class AnnotateTweet {
    
    private String  filename;
    private String  type;
    private List<List<String>> messages;
    private String  wordFilename;
    private String  wordPositifFilename;
    private String  wordNegatifFilename;
    private String  wordMixteFilename;
    private Map<String, Integer> wordPolarty ;
    private List<String> wordPositif;
    private List<String> wordNegatif;
    private List<String> wordMixte;
    private List<String> TweetAnnoted;
    
    

    public AnnotateTweet(String filename, String type) throws IOException {
        this.filename = filename;
        this.type = type;
        this.TweetAnnoted = new ArrayList<>();
        if ("Annote".equals(this.type)){
            this.CsvTestToArray();
            Collections.shuffle(this.TweetAnnoted);
            this.exportToCsv("src/result/annotated_tp_tweet.csv");
        } else if ("NonAnnote".equals(this.type)) {
            this.messages = new ArrayList<>();
            this.wordFilename = "src/data/words.txt";
            this.wordPositifFilename = "src/data/positif_words.txt";
            this.wordNegatifFilename = "src/data/negatif_words.txt";
            this.wordMixteFilename = "src/data/mixte_words.txt";
            this.wordPolarty = new HashMap<>();
            this.wordPositif = new ArrayList<>();
            this.wordNegatif = new ArrayList<>();
            this.wordMixte = new ArrayList<>();
            this.CsvToArray();
            this.setWordPolarty();
            this.annoteTweet();
            Collections.shuffle(this.TweetAnnoted);
            this.exportToCsv("src/result/annotated_tweet.csv");
        }
        
        
    }
    
    public void CsvToArray() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\r?\\n");
                messages.add(Arrays.asList(values));
            }
        }
    }

    public void CsvTestToArray() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\r?\\n");
                String[] values1 = values[0].split("\\t");
                if ("objective".equals(values1[1])) {
                    this.TweetAnnoted.add(values1[0] + "\t" + "autre");
                } else if ("mixed".equals(values1[1])) {
                    this.TweetAnnoted.add(values1[0] + "\t" + "mixte");
                }

            }
        }
    }
    
    public void setWordPolarty () throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.wordFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\r?\\n");
                String[] values1 = values[0].split("\\t");
                this.wordPolarty.put(values1[0], Integer.parseInt(values1[1]));
            }
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(this.wordPositifFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                this.wordPositif.add(line);
            }
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(this.wordNegatifFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                this.wordNegatif.add(line);
            }
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(this.wordMixteFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                this.wordMixte.add(line);
            }
        }
    }
    
    public void annoteTweet (){
        for (List<String> temp : this.messages) {
            for(String tweet : temp){
                int score = this.annoteTweetByWordPolarty(tweet);
                if(score == 0){
                    String respons = this.annoteTweetByWord(tweet);
                    if(respons != null ){
                        this.TweetAnnoted.add(tweet+"\t"+respons);
                    }
                }else if(score < -1){
                    this.TweetAnnoted.add(tweet+"\t"+"negative");
                }else if(score <= 1 && score >= -1){
                    this.TweetAnnoted.add(tweet+"\t"+"autre");
                }else if (score <= 5 && score >= 1){
                    this.TweetAnnoted.add(tweet+"\t"+"mixte");
                }else if (score > 5){
                    this.TweetAnnoted.add(tweet+"\t"+"positive");
                }
            }
        }
    }
    
    public int annoteTweetByWordPolarty (String tweet){
        String[] words = tweet.split(" ");
        int somme = 0;
        for(int i=0; i<words.length; i++){
            somme = somme+ this.getWordPolarty(words[i]);
        }
        return somme;
    }
    
    public int getWordPolarty (String word){
        if(this.wordPolarty.containsKey(word)){
           return this.wordPolarty.get(word);
        }
        return 0;
    }
    
    public String annoteTweetByWord (String tweet){
        String[] words = tweet.split(" ");
        int postif = 0;
        int negatif = 0;
        int mixte = 0;
        for(int i=0; i<words.length; i++){
            if(this.wordPositif.contains(words[i])){
                postif++;
            }else if(this.wordNegatif.contains(words[i])){
                negatif++;
            }else if(this.wordMixte.contains(words[i])){
                mixte++;
            }
        }
        if(postif == 0 &&  negatif == 0 && mixte == 0){
            return "autre";
        }else if (postif > negatif && postif > mixte){
            return "positive";
        }else if (negatif > postif && negatif > mixte){
            return "negative";
        }else if (mixte > postif && mixte > negatif){
            return "mixte";
        }
        return null;
    }

    @Override
    public String toString() {
        return "AnnotateTweet{" + "filename=" + filename + ", records=" + messages + '}';
    }

    public void exportToCsv(String filename) throws IOException {
        File file = new File(filename);
        Files.deleteIfExists(file.toPath());
        try {
            FileWriter fstream = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(fstream);
            for (String temp : this.TweetAnnoted) {
                out.write(temp);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
