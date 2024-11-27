package com.example.carmate;

import java.util.LinkedList;

public class User {
    private String firstName, lastName;
    private int age;
    private String imgLocation;
    public String[] driverOrPassenger; //for printing if you know which is their main one
    private String mainStatus;
    private double rating;
    private int ratingCount;
    private LinkedList<User> friendsList;
    //it may be easier for the User to store an actual image object - we'll see
    //just a template for us to use...
    //add any more fields and their respective setters and getters as needed
    //will need more arraylist/linkedlist for previous rides, filters etc.

    public User(String firstName, String lastName, String imgLocation, int age, String mainStatus){
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setImgLocation(imgLocation);
        driverOrPassenger = new String[]{"driver", "passenger"};
        setMainStatus(mainStatus);
        ratingCount = 0;
        friendsList = new LinkedList<User>();
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

    public void setLastName(String firstName){
        this.lastName = lastName;
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    public int getAge(){
        return this.age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setImgLocation(String imgLocation){
        this.imgLocation = imgLocation;
    }

    public String getImgLocation(){
        return this.imgLocation;
    }

    public String getMainStatus(){
        return this.mainStatus;
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

    public LinkedList<User> getFriendsList(){
        return this.friendsList;
    }

    public void addFriend(User friend){
        if (!friendsList.contains(friend)) {
            friendsList.add(friend);
        }

    }







}
