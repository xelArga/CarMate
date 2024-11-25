package com.example.carmate;

public class User {
    private String firstName, lastName;
    private int age;
    private String imgLocation;
    //it may be easier for the User to store an actual image object - we'll see
    //just a template for us to use...
    //add any more fields and their respective setters and getters as needed

    public User(String firstName, String lastName, String imgLocation, int age){
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setImgLocation(imgLocation);
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





}
