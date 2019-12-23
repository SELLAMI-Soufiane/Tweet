/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
 import java.util.List;
 import javax.xml.bind.annotation.XmlElement;
 import javax.xml.bind.annotation.XmlRootElement; 

/**
 *
 * @author Soufiane.sel
 */

 @XmlRootElement
 public class Root {
   private List<Tweet> tweet;
 
   @XmlElement(name="tweet")
   public List<Tweet> getTweet() {
       return tweet;
   }
 
   public void setTweet(List<Tweet> tweetList) {
       this.tweet = tweetList;
   }

    @Override
    public String toString() {
        return "Root{" + "tweet=" + tweet + '}';
    }
   
 }