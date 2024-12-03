package com.example.carmate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {
    private String firstName, lastName;
    private int profileImageId;
    public String[] driverOrPassenger; //for printing if you know which is their main one
    private String mainStatus;

    private double rating;
    private int ratingCount;
    List<Message> messages;
    //it may be easier for the User to store an actual image object - we'll see
    //just a template for us to use...
    //add any more fields and their respective setters and getters as needed
    //will need more arraylist/linkedlist for previous rides, filters etc.

    public User(String firstName, String lastName, int imgId, String mainStatus){
        setFirstName(firstName);
        setLastName(lastName);
        setImgId(imgId);
        driverOrPassenger = new String[]{"driver", "passenger"};
        setMainStatus(mainStatus);
        ratingCount = 0;
        messages = new LinkedList<>(Collections.singletonList(new Message("You: Hello", true)));
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }

    public void setImgId(int profileImageId){
        this.profileImageId = profileImageId;
    }

    public int getImgId(){
        return this.profileImageId;
    }

    public String getMainStatus(){
        return this.mainStatus;
    }

    public String getLastMessage(){
        return messages.get(messages.size() -1).getContent();
    }

    public void setMainStatus(String mainStatus){
        this.mainStatus = mainStatus;
    }

    public double getRating(){
        return this.rating;
    }

    public void setRating(int rating){
        //treating rating as average, so multiply by count for sum, increment count, then divide
        if(ratingCount == 0){
            this.rating = rating;
            ++ratingCount;
        }
        else{
            double sumRating = ratingCount * this.rating;
            ++ratingCount;
            sumRating += rating;
            this.rating = sumRating / ratingCount;
        }

    }

}
