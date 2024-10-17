package com.example.gym.Data;


public class Coach extends User {
    private int yearOfExperience;

    // Constructor with additional yearOfExperience
    public Coach(int id, String name, String email, String password, int age, String phoneNumber, int yearOfExperience) {
        // Call the parent class constructor to initialize User properties
        super(id, name, email, password, age, phoneNumber, false); // Coaches are not admin by default
        this.yearOfExperience = yearOfExperience;
    }

    // Getter and Setter for yearOfExperience
    public int getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(int yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    // You can also add additional methods related to the coach if necessary
}
