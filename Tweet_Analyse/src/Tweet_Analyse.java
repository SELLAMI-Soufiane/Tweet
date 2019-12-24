/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Soufiane.sel
 */
public class Tweet_Analyse {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     */
    public static void main(String[] args) throws IOException, JAXBException{
//    	libs/liblinear-2.30/train -c 4 -e 0.1 -s 2 result/data_train.svm result/model
//        libs/liblinear-2.30/predict result/data_test.svm result/model result/predect
        CleanTweet clean;
        clean = new CleanTweet ("src/data/donne_Non_Annote.xml", "NonAnnote");
        clean = new CleanTweet ("src/data/donne_TP_Annote.csv", "Annote");
        clean = new CleanTweet ("src/data/donne_Test.csv", "Test");
        AnnotateTweet annotate;
        annotate = new AnnotateTweet("src/result/clean_tweet.csv", "NonAnnote");
        annotate = new AnnotateTweet("src/result/clean_tp_tweet.csv", "Annote");
        PrepareData prepare;
        prepare = new PrepareData("src/result/annotated_tweet.csv", "src/result/annotated_tp_tweet.csv");
        prepare = new PrepareData("src/result/clean_test_tweet.csv");
        PrepareSVM svm;
        svm = new PrepareSVM("src/result/data_to_train.csv", "src/result/data_to_test.csv");
//        Predect predect;
//        predect = new Predect("src/data/donne_Test.csv", "src/result/predect");
    }
    
}
