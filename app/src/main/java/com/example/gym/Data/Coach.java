package com.example.gym.Data;

public class Coach extends User {
    private int yearOfExperience;
    private String image; // New field for storing the coach's image (URL or file path)

    // Constructor with additional yearOfExperience and image
    public Coach(int id, String name, String email, String password, int age, String phoneNumber,String gender, int yearOfExperience, String image) {
        // Call the parent class constructor to initialize User properties
        super(id, name, email, password, age, phoneNumber, gender,false); // Coaches are not admin by default
        this.yearOfExperience = yearOfExperience;
        this.image = image; // Initialize the image field
    }

    // Getter and Setter for yearOfExperience
    public int getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(int yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    // Getter and Setter for image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // You can also add additional methods related to the coach if necessary
}
