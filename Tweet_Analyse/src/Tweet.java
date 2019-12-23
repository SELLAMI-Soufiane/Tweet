/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Soufiane.sel
 */
@XmlRootElement(name="tweet")
public class Tweet {

    private String username;
    private String url;
    private String message;
    private int favoris;
    private int retweet;
    private Date date;

    public Tweet() {
    }

    public Tweet(String message) {
        this.message = message;
    }
    
    public Tweet(String username, String url, String message, int favoris, int retweet, Date date) {
        super();
        this.username = username;
        this.url = url;
        this.message = message;
        this.favoris = favoris;
        this.retweet = retweet;
        this.date = date;
    }

    @XmlElement(name="username")
    public String getUsername() {
        return username;
    }

    @XmlElement(name="url")
    public String getUrl() {
        return url;
    }

    @XmlElement(name="message")
    public String getMessage() {
        return message;
    }

    @XmlElement(name="favoris")
    public int getFavoris() {
        return favoris;
    }

    @XmlElement(name="retweet")
    public int getRetweet() {
        return retweet;
    }

    @XmlElement(name="date")
    public Date getDate() {
        return date;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFavoris(int favoris) {
        this.favoris = favoris;
    }

    public void setRetweet(int retweet) {
        this.retweet = retweet;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void removeUrl (){
        String regex = "(http|https|ftp|ftps)?://\\S+\\s?";
        this.message = this.message.replaceAll(regex, "");
    }
    
    public void removePunctuation  (){
        String regex = "[\\p{Punct}&&[^']]+";
        this.message = this.message.replaceAll(regex, " ");
        this.message = this.message.replace("...", "");
        this.message = this.message.replace("…", "");
        //regex = "[-+.^:,]+";
        //this.message = this.message.replaceAll(regex, "");
    }
    
    public void removeAllNumber (){
        String regex = "[0123456789]";
        this.message = this.message.replaceAll(regex, "");
    } 
    
    public void lowerCase (){
        this.message = this.message.toLowerCase();
    }
    
    public void removeSpaces (){
        String regex = "\\s+";
        this.message = this.message.replaceAll(regex, " ");
        regex = "^\\s+";
        this.message = this.message.replaceAll(regex, "");
        regex = "\\s+$";
        this.message = this.message.replaceAll(regex, "");
    }
    
    @Override
    public String toString() {
        return "Tweet{" + "username=" + username + ", url=" + url + ", message=" + message + ", favoris=" + favoris + ", retweet=" + retweet + ", date=" + date + '}';
    }
    
}
