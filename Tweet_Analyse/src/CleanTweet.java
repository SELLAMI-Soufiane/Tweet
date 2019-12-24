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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Soufiane.sel
 */
public final class CleanTweet {

    private String filename;
    private String  wordUntilFilename;
    private String  wordUntilTestFilename;
    private Root root;
    private String type;
    private List<Tweet> tweets;
    private List<String> polarity;
    private List<String> wordUntil;
    private List<String> wordUntilTest;

    public CleanTweet(String filename, String type) throws JAXBException, IOException {
        this.filename = filename;
        this.type = type;
        this.tweets = new ArrayList<>();
        if ("NonAnnote".equals(this.type)) {
        	this.wordUntilFilename = "src/data/until_word.txt";
            this.wordUntil = new ArrayList<>();
            this.root = this.xmlfiletoObject(this.filename);
            this.tweets = this.root.getTweet();
            this.TxttoArray(this.wordUntilFilename);
        } else if ("Annote".equals(this.type)){
        	this.wordUntilFilename = "src/data/until_word.txt";
            this.wordUntil = new ArrayList<>();
            this.polarity = new ArrayList<>();
            this.CsvTpToObject();
            this.TxttoArray(this.wordUntilFilename);
        }else if ("Test".equals(this.type)){
        	this.wordUntilTestFilename = "src/data/until_word_test.txt";
            this.wordUntilTest = new ArrayList<>();
            this.CsvTestToObject();
            this.TxttoArray(this.wordUntilTestFilename);
        }
        
        for (Tweet temp : this.tweets) {
            temp.removeUrl();
            temp.removeAllNumber();
            temp.removePunctuation();
            temp.lowerCase();
        }
        
        if("Test".equals(this.type)){
        	this.removeAllUnitlWord(this.wordUntilTest);
            this.exportToCsv("src/result/clean_test_tweet.csv");
        }else if ("NonAnnote".equals(this.type)) {
        	this.removeAllUnitlWord(this.wordUntil);
        	this.CleanList();
            this.removeDuplicatesForNotAnnotetd();
            this.exportToCsv("src/result/clean_tweet.csv");
        } else if ("Annote".equals(this.type)){
            this.SetPolarityForTestTweet();
            this.removeAllUnitlWord(this.wordUntil);
            this.CleanList();
            this.removeDuplicates();
            this.exportToCsv("src/result/clean_tp_tweet.csv");
        }
    }

    public String getFilename() {
        return filename;
    }

    public Root getRoot() {
        return root;
    }
    
    public void TxttoArray (String filename) throws IOException {
    	try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                this.wordUntil.add(line);
            }
        }
    }
    
    public void CleanList (){
        List<Tweet> tempList = new ArrayList<>();
        for (Tweet temp : this.tweets) {
            int count = this.countWordsUsingSplit(temp.getMessage());
            if(count == 0 || count <= 2){
                tempList.add(temp);
            }
        }
        this.tweets.removeAll(tempList);
    }
    public void removeDuplicatesForNotAnnotetd() 
    { 
        List<Tweet> tempList = new ArrayList<>();
        for (Tweet temp : this.tweets) {
            tempList.add(new Tweet(temp.getMessage()));
        }
        this.tweets.clear();
        
        
        for (Tweet temp : tempList) {
            if(!this.tweets.contains(temp)){
                this.tweets.add(temp);
            }
        }
    }
    
    public void removeDuplicates() 
    { 

        List<Tweet> tempList = new ArrayList<>();
        
        for (Tweet temp : this.tweets) {
            if(!tempList.contains(temp)){
                tempList.add(temp);
            }
        }
        this.tweets = tempList;
    } 
    
    public int countWordsUsingSplit(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        String[] words = input.split("\\s+");
        return words.length;
    }

    public Root xmlfiletoObject(String filename) throws JAXBException {
        File file = new File(filename);
        JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Root root = (Root) jaxbUnmarshaller.unmarshal(file);

        return root;

    }
    
    public void CsvTpToObject() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values1 = line.split("\\t");
                this.tweets.add(new Tweet(values1[1]));
                this.polarity.add(values1[2]);
            }
        }
    }
    
    public void SetPolarityForTestTweet() {
        for (int i = 0; i < this.tweets.size(); i++) {
            this.tweets.get(i).setMessage(this.tweets.get(i).getMessage()+"\t"+this.polarity.get(i));
        }
    }
    
    public void CsvTestToObject() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                this.tweets.add(new Tweet(line));
            }
        }
    }
    
    public void removeAllUnitlWord (List<String> words) {
    	String s="";
    	for (Tweet temp : this.tweets) {
    		String[] arr = temp.getMessage().split("\\s+"); 
    		for ( String ss : arr) {
        		if(!words.contains(ss)) {
        			s = s + " "+ss;
        		}
        	    
        	}
    		temp.setMessage(s);
    		temp.removeSpaces();
        }
    }

    public void exportToCsv(String filename) throws IOException {
        File file = new File(filename);
        Files.deleteIfExists(file.toPath());
        try {
            FileWriter fstream = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(fstream);
            for (Tweet temp : this.tweets) {
                out.write(temp.getMessage());
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
