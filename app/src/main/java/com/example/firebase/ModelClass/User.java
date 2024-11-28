package com.example.firebase.ModelClass;

public class User {
    private String email;

    public User() { } // Firestore requires an empty constructor

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
